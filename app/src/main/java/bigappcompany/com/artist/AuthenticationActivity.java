package bigappcompany.com.artist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class AuthenticationActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "9i5LHgauCjvGoELSvRJq6TjjO";
    private static final String TWITTER_SECRET = "9RKoVQw2ShSYn2uG1tUgSrAp0NvQrf3LfGcITFqndCurhIK65o";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_authentication);
        try
        {
            Log.e("Token", FirebaseInstanceId.getInstance().getToken());
        }
        catch (Exception e)
        {

        }
        Button bt=(Button)findViewById(R.id.bt_auth);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button bt_logout=(Button)findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance()!=null)
                    FirebaseAuth.getInstance().signOut();
                else
                {
                    Log.d("digits","session_expired");
                }
            }
        });

        /*DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);


        digitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth();
            }
        });
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });*/



    }
   

}
