package bigappcompany.com.artist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;

public class SplashActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QOlRzNIvZGvCPoUlTePnwumXP";
    private static final String TWITTER_SECRET = "b26ZiCaJ1RrJrSHGUiqcRk5uqRWr26kVpjbDSsyoMLOmEIhU3t";


    private SharedPreferences sp;
    
    public static final int RC_SIGN_IN = 1;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "AplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp=getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE);
        //changing

       

        /*new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (Digits.getActiveSession()==null) {
                    Toast.makeText(getApplicationContext(), "not initialized", Toast.LENGTH_LONG).show();
                    Digits.authenticate(authConfigBuilder.build());
                } else if (!getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
                    login(Digits.getActiveSession().getPhoneNumber());
                } else {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }


        }, 1500);*/
    
        new Handler().postDelayed(new Runnable() {
        
            @Override
            public void run() {
            
                checksession();
            
            }
        }, 3000);
    
    }
    
    private void checksession() {
        AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
            new OnSuccessListener() {
                
                @Override
                public void onSuccess(Object o) {
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    if (u != null) {
                        // Either a user was already logged in or token exchange succeeded
                        Log.d("MyApp", "Digits id preserved:" + u.getUid());
                        Log.d("MyApp", "Digits phone number preserved: " + u.getPhoneNumber());
                        //  login(u.getPhoneNumber().toString(), "0");
                        if (!getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
                            login(u.getPhoneNumber().toString());
                        } else {
                            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        // No tokens were found to exchange and no Firebase user logged in.
                        startActivityForResult(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                
                                ))
                                .build(),
                            RC_SIGN_IN);
                    }
                }
            }).addOnFailureListener(this,
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    // Error migrating Digits token
                }
            });
        
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                PhoneAuthCredential ph = phoneAuthCredential;
                Log.e("phoneAuthCredential",""+phoneAuthCredential);
            }
            
            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
            }
        };
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                login(response.getPhoneNumber());
//				return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User");
//					return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login", "No Internet Connection");
//					return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
//					return;
                }
            }
            Log.e("Login", "Unknown sign in response");
        }
    }
    
    

    void login(final String phoneNumber)
    {
        Download_web web=new Download_web(SplashActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    if (new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
                        JSONObject data=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean(JsonParser.GO, true);
                        edit.putString(JsonParser.AR_NAME, data.getString(JsonParser.AR_NAME));
                        edit.putString(JsonParser.ARTIST_ID, data.getString(JsonParser.ID));
                        edit.putString(JsonParser.AR_MOB_NUM,phoneNumber);
                        edit.commit();
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                        i.putExtra(JsonParser.AR_MOB_NUM,phoneNumber);
                        startActivity(i);
                        finish();
                    }

                }
                catch (JSONException je)
                {
                    je.printStackTrace();
                }
            }
        });
        web.setReqType(false);
        try {
            web.setData(new JSONObject().put(JsonParser.AR_MOB_NUM, phoneNumber).put(JsonParser.DEVICE_ID, FirebaseInstanceId.getInstance().getToken()).toString());
            web.execute(ApiUrl.LOGIN);
        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }
    }

}
