package bigappcompany.com.artist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bigappcompany.com.artist.adapters.CustomAdapter;
import bigappcompany.com.artist.models.CitiesModel;
import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;

public class RegisterActivity extends AppCompatActivity implements PaymentResultListener {
	
	private Button bt_submit;
	private SharedPreferences sp;
	private boolean canSubmit;
	EditText et_name, et_email, et_phone;
	AutoCompleteTextView at_catg, at_sub, at_city;
	List<CitiesModel> catgs, cities;
	private EditText et_about;
	
	boolean isEdit = false;
	private String TAG = "Razor";
	private String payamount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		
		et_name = (EditText) findViewById(R.id.txt_name);
		et_phone = (EditText) findViewById(R.id.txt_mobile);
		et_email = (EditText) findViewById(R.id.txt_email);
		at_catg = (AutoCompleteTextView) findViewById(R.id.txt_catg);
		at_sub = (AutoCompleteTextView) findViewById(R.id.txt_sub);
		at_city = (AutoCompleteTextView) findViewById(R.id.txt_city);
		et_about = (EditText) findViewById(R.id.et_about);
		
		catgs = new ArrayList<>();
		cities = new ArrayList<>();
		
		String phone = getIntent().getStringExtra(JsonParser.AR_MOB_NUM);
		
		if (phone != null) {
			et_phone.setText(phone);
			et_phone.setFocusable(false);
		}








        /*EditText editText = (EditText) findViewById(R.id.txt_name);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);*/
		
		load_catgs();
		load_cities();
		
		bt_submit = (Button) findViewById(R.id.bt_sign);
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		if (isEdit) {
			getSupportActionBar().setTitle("Update Profile");
			bt_submit.setText("Update");
		} else {
			getSupportActionBar().setTitle("Register");
			getpayableamount();
		}
		
		sp = getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE);
		
		bt_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				canSubmit = true;
				check(et_name);
				check(et_email);
				check(et_phone);
				check(et_about);
				
				if (at_city.getText().toString().trim().equals("")) {
					at_city.setError("Empty");
					canSubmit = false;
				}
				if (sub_id.trim().equals("")) {
					at_sub.setError("Empty");
					canSubmit = false;
				}
				if (catg_id.trim().equals("")) {
					at_catg.setError("Empty");
					canSubmit = false;
				}
				if (!isValidEmail(et_email.getText().toString().trim())) {
					canSubmit = false;
					et_email.setError("Invalid Email");
				}
				if (canSubmit) {
					if (isEdit) {
						try {
							Download_web web = new Download_web(RegisterActivity.this, new OnTaskCompleted() {
								@Override
								public void onTaskCompleted(String response) {
									try {
										closeDialog();
										if (new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
											
											SharedPreferences.Editor edit = sp.edit();
											edit.putBoolean(JsonParser.GO, true);
											edit.putString(JsonParser.AR_NAME, et_name.getText().toString().trim());
											if (!isEdit)
												edit.putString(JsonParser.ARTIST_ID, new JSONObject(response).getJSONObject(JsonParser.DATA).getString(JsonParser.ARTIST_ID));
											edit.commit();
											if (!isEdit)
												startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
											
											finish();
											
										} else {
											Toast.makeText(getApplicationContext(), "Unable to register", Toast.LENGTH_LONG).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
							
							JSONObject object = new JSONObject();
							object.put(JsonParser.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
							object.put(JsonParser.AR_NAME, et_name.getText().toString());
							object.put(JsonParser.AR_CITY, at_city.getText().toString().trim());
							object.put(JsonParser.AR_EMAIL, et_email.getText().toString());
							
							if (!isEdit)
								object.put(JsonParser.AR_MOB_NUM, et_phone.getText().toString());
							else
								object.put(JsonParser.ARTIST_ID, (getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getString(JsonParser.ARTIST_ID, "")));
							object.put(JsonParser.CATG, catg_id);
							object.put(JsonParser.SUB_CATG, sub_id);
							object.put(JsonParser.BIO, et_about.getText().toString());
							
							web.setReqType(false);
							web.setData(object.toString());
							if (!isEdit)
								web.execute(ApiUrl.REGISTER);
							else
								web.execute(ApiUrl.UPDATE);
							showDailog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					else {
						startPayment();
					}
					
				}
			}
		});
		
		
	}
	
	private void getpayableamount() {
		
		Download_web web = new Download_web(RegisterActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					if (new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
						try {
							payamount = new JSONObject(response).getJSONArray("data").getJSONObject(0).getString
							    ("amount");
							
						} catch (Exception e) {
							
						}
						bt_submit.setText("Pay : " + payamount);
					} else {
					}
					
				} catch (JSONException je) {
					je.printStackTrace();
				}
			}
		});
		web.setReqType(true);
		try {
			web.execute(ApiUrl.GETAMOUNT);
		} catch (Exception je) {
			je.printStackTrace();
		}
	}
	
	
	String json_catg;
	
	void load_catgs() {
		new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("response", response);
				closeDialog();
				json_catg = response;
				try {
					JSONObject object = new JSONObject(json_catg);
					if (object.getBoolean(JsonParser.RESPONSE_STATUS)) {
						
						final ArrayList<CitiesModel> models = new ArrayList<CitiesModel>();
						final JSONArray array = object.getJSONArray(JsonParser.DATA);
						for (int i = 0; i < array.length(); i++) {
							CitiesModel model = new CitiesModel(array.getJSONObject(i).getString(JsonParser.CAT_ID), array.getJSONObject(i).getString(JsonParser.CAT_NAME));
							models.add(model);
						}
						final CustomAdapter adapter1 = new CustomAdapter(getApplicationContext(), R.layout.simple_list_item, models);
						//chip.setAdapter(adapter1);
						at_catg.setAdapter(adapter1);
						at_catg.setThreshold(1);
						at_catg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								Toast.makeText(getApplicationContext(), adapter1.getItem(position).getCity(), Toast.LENGTH_LONG).show();
								try {
									catg_id = adapter1.getItem(position).getId();
									for (int i = 0; i < array.length(); i++) {
										if (catg_id.equals(array.getJSONObject(i).getString(JsonParser.CAT_ID))) {
											setSub(array.getJSONObject(i));
											break;
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
						
						at_catg.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								at_catg.showDropDown();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}).execute(new ApiUrl().CATEGORIES);
		showDailog();
	}
	
	private void setSub(JSONObject jsonObject) {
		try {
			final ArrayList<CitiesModel> models = new ArrayList<CitiesModel>();
			final JSONArray array = jsonObject.getJSONArray(JsonParser.SUBCATG);
			for (int i = 0; i < array.length(); i++) {
				CitiesModel model = new CitiesModel(array.getJSONObject(i).getString(JsonParser.SUB_ID), array.getJSONObject(i).getString(JsonParser.SUB_NAME));
				Log.e("sub", model.getCity());
				models.add(model);
			}
			final CustomAdapter adapter1 = new CustomAdapter(getApplicationContext(), R.layout.simple_list_item, models);
			//chip.setAdapter(adapter1);
			at_sub.setAdapter(adapter1);
			at_sub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					sub_id = adapter1.getItem(position).getId();
				}
			});
			
			at_sub.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					at_sub.showDropDown();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void load_cities() {
		new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("response", response);
				try {
					JSONObject object = new JSONObject(response);
					if (object.getBoolean(JsonParser.RESPONSE_STATUS)) {
						
						ArrayList<CitiesModel> models = new ArrayList<CitiesModel>();
						JSONArray array = object.getJSONArray(JsonParser.DATA);
						for (int i = 0; i < array.length(); i++) {
							CitiesModel model = new CitiesModel(array.getJSONObject(i));
							models.add(model);
						}
						final CustomAdapter adapter1 = new CustomAdapter(getApplicationContext(), R.layout.simple_list_item, models);
						//chipCity.setAdapter(adapter1);
						at_city.setAdapter(adapter1);
						at_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								city_id = adapter1.getItem(position).getId();
							}
						});
						
						at_city.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								at_city.showDropDown();
							}
						});
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}).execute(ApiUrl.CITIES);
	}
	
	private void check(EditText et) {
		if (et.getText().toString().trim().equals("")) {
			canSubmit = false;
			et.setError("Empty");
		}
	}
	
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	
	String catg_id = "", sub_id = "", city_id = "";
	
	ProgressDialog dialog;
	
	void showDailog() {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	void closeDialog() {
		if (dialog != null)
			dialog.cancel();
	}
	
	@Override
	public void onPaymentSuccess(String s) {
		try {
			//Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
			savedatainserver(s);
		} catch (Exception e) {
			Log.e(TAG, "Exception in onPaymentSuccess", e);
		}
	}
	
	private void savedatainserver(String s) {
		try {
			Download_web web = new Download_web(RegisterActivity.this, new OnTaskCompleted() {
				@Override
				public void onTaskCompleted(String response) {
					try {
						closeDialog();
						if (new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
							
							SharedPreferences.Editor edit = sp.edit();
							edit.putBoolean(JsonParser.GO, true);
							edit.putString(JsonParser.AR_NAME, et_name.getText().toString().trim());
							if (!isEdit)
								edit.putString(JsonParser.ARTIST_ID, new JSONObject(response).getJSONObject(JsonParser.DATA).getString(JsonParser.ARTIST_ID));
							edit.commit();
							if (!isEdit)
								startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
							
							finish();
							
						} else {
							Toast.makeText(getApplicationContext(), "Unable to register", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			
			JSONObject object = new JSONObject();
			object.put(JsonParser.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
			object.put(JsonParser.AR_NAME, et_name.getText().toString());
			object.put(JsonParser.AR_CITY, at_city.getText().toString().trim());
			object.put(JsonParser.AR_EMAIL, et_email.getText().toString());
			object.put(JsonParser.AR_AMOUNT, payamount);
			object.put(JsonParser.AR_TRANSACTION, s);
			
			if (!isEdit)
				object.put(JsonParser.AR_MOB_NUM, et_phone.getText().toString());
			else
				object.put(JsonParser.ARTIST_ID, (getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getString(JsonParser.ARTIST_ID, "")));
			object.put(JsonParser.CATG, catg_id);
			object.put(JsonParser.SUB_CATG, sub_id);
			object.put(JsonParser.BIO, et_about.getText().toString());
			
			web.setReqType(false);
			web.setData(object.toString());
			if (!isEdit)
				web.execute(ApiUrl.REGISTER);
			else
				web.execute(ApiUrl.UPDATE);
			showDailog();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void startPayment() {
		/**
		 * You need to pass current activity in order to let Razorpay create CheckoutActivity
		 */
		final Activity activity = this;
		
		final Checkout co = new Checkout();
		
		try {
			JSONObject options = new JSONObject();
			options.put("name", et_name.getText().toString());
			options.put("description", "Registration Fee");
			//You can omit the image option to fetch the image from dashboard
			options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
			options.put("currency", "INR");
			options.put("amount", "" + (Double.parseDouble(payamount)) * 100);
			
			JSONObject preFill = new JSONObject();
			preFill.put("email", et_email.getText().toString());
			preFill.put("contact", et_phone.getText().toString());
			
			options.put("prefill", preFill);
			
			co.open(activity, options);
		} catch (Exception e) {
			Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
			    .show();
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPaymentError(int i, String s) {
		try {
			Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(TAG, "Exception in onPaymentError", e);
		}
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
}
