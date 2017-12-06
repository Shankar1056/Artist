package bigappcompany.com.artist.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bigappcompany.com.artist.PotosVideosActivity;
import bigappcompany.com.artist.R;
import bigappcompany.com.artist.ResultActivity;
import bigappcompany.com.artist.adapters.PhAdapter;
import bigappcompany.com.artist.adapters.VideoAdapter;
import bigappcompany.com.artist.models.ImageObj;
import bigappcompany.com.artist.models.VideoObj;
import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;

import static android.content.Context.MODE_PRIVATE;


public class VideosFragment extends Fragment implements  View.OnClickListener {
	int position;
	ImageView img;
	RecyclerView recyclerView;
	FloatingActionButton fab;
	private ImageView img_photo;
	private String js_res="";

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

		recyclerView=(RecyclerView)rootView.findViewById(R.id.photos_RV);
		fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
		LinearLayoutManager manager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(manager);

		img_photo=(ImageView)rootView.findViewById(R.id.img_add);
		img_photo.setImageResource(R.drawable.video_48dp);

		recyclerView.setAdapter(new VideoAdapter(new ArrayList<VideoObj>()));
		if(recyclerView.getAdapter().getItemCount()>0)
		{
			recyclerView.setVisibility(View.VISIBLE);
		}
		else
		{
			Download_web web=new Download_web(getContext(), new OnTaskCompleted() {
				@Override
				public void onTaskCompleted(String response) {
					try
					{
						js_res=response;
						JSONObject object=new JSONObject(response);
						if(object.getBoolean(JsonParser.RESPONSE_STATUS))
						{
							JSONArray array=object.getJSONArray(JsonParser.DATA);
							for(int i=0;i<array.length();i++)
							{
								VideoObj obj=new VideoObj(array.getJSONObject(i));
								((VideoAdapter)recyclerView.getAdapter()).addItem(obj);

							}
							recyclerView.getAdapter().notifyDataSetChanged();
						}
						else
						{
							Toast.makeText(getContext(),"No data",Toast.LENGTH_LONG).show();
						}
						recyclerView.getAdapter().notifyDataSetChanged();
						recyclerView.setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			web.execute(ApiUrl.VIDEOS+(getContext().getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.ARTIST_ID,"")));
		}
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		return rootView;
	}



	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(int pos) {
		VideosFragment fragment=new VideosFragment();
		fragment.position=pos;
		return fragment;
	}
	AlertDialog.Builder builder;
	private void showDialog() {

		builder = new AlertDialog.Builder(getContext());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		try {
			View view = inflater.inflate(R.layout.dialog_item, null);
			builder.setCustomTitle(view);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("setTitleError",e.getMessage().toString());
		}
		try {
			final View view1 = inflater.inflate(R.layout.dialog_body, null);
			builder.setView(view1);
			et_date=(TextView)view1.findViewById(R.id.et_date);
			myCalendar= Calendar.getInstance();
			date = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
									  int dayOfMonth) {
					// TODO Auto-generated method stub
					myCalendar.set(Calendar.YEAR, year);
					myCalendar.set(Calendar.MONTH, monthOfYear);
					myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					updateLabel();
				}

			};
			et_date.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new DatePickerDialog(getContext(), date, myCalendar
							.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
							myCalendar.get(Calendar.DAY_OF_MONTH)).show();

				}
			});
			final AlertDialog dialog=builder.show();
			((Button)view1.findViewById(R.id.bt_upload)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean canSend=true;
					String ID=check(((EditText)view1.findViewById(R.id.txt_video)).getText().toString().trim());
					if(ID.equals(""))
					{
						canSend=false;
						Toast.makeText(getContext(),"INVALID URL",Toast.LENGTH_LONG).show();
					}
					if(((EditText)view1.findViewById(R.id.txt_video)).getText().toString().trim().equals(""))
					{
						canSend=false;
					}
					if(et_date.getText().toString().trim().equals(""))
						canSend=false;
					if(canSend)
					{
						Download_web web=new Download_web(getContext(), new OnTaskCompleted() {
							@Override
							public void onTaskCompleted(String response) {
								dialog.dismiss();
							}
						});
						web.setReqType(false);
						try {
							web.setData(
									(new JSONObject().put(JsonParser.ARTIST_ID,(getContext().getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.ARTIST_ID,"")))
									.put(JsonParser.VIDEO,((EditText)view1.findViewById(R.id.txt_video)).getText().toString().trim())
									.put(JsonParser.TITLE,((EditText)view1.findViewById(R.id.et_title)).getText().toString().trim())
									.put(JsonParser.VIDEO_ID,ID)
									.put(JsonParser.DATE,et_date.getText().toString())).toString()
							);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						web.execute(ApiUrl.UPLOAD_VID);
						//{"artist_id": "1","video":"https://www.youtube.com/watch?v=_MnIwx8eHhk","title":"","date":"","video_id":"" }
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	String check(String url)
	{
		//String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|\u200C\u200B%2Fvideos%2F|embed%2\u200C\u200BF|youtu.be%2F|%2Fv%2\u200C\u200BF)[^#\\&\\?\\n]*";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);

		if(matcher.find()){
			return matcher.group();
		}
		return "";
	}
	Calendar myCalendar;
	TextView et_date;
	DatePickerDialog.OnDateSetListener date;
	private void updateLabel() {

		String myFormat = "yyyy-MM-dd"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

		et_date.setText(sdf.format(myCalendar.getTime()));
	}

}


