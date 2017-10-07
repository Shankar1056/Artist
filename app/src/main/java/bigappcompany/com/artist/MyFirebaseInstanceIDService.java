package bigappcompany.com.artist;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dell on 11/18/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences sp;
    boolean isSeller;
    String token="";
    String ref_token="";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Refreshed token: " + refreshedToken);
        sp=getSharedPreferences("Seller",MODE_PRIVATE);
        isSeller=sp.getBoolean("isSeller",false);
        token=sp.getString("token","");
        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        if(!token.equals("")) {
            ref_token=refreshedToken;
            SharedPreferences shared=getSharedPreferences("Seller",MODE_PRIVATE);
            SharedPreferences.Editor edit=shared.edit();
            edit.putString("fcm_token",ref_token);
            edit.commit();
            Download_web web = new Download_web();
            web.execute("");
        }
    }

    private class Download_web extends AsyncTask<String, String, Integer>
    {

        private String jsondata;
        private String[] citiescontent;
        int otp=0;
        private int ind;
        private JSONObject jsonRootObject;


        @Override
        protected Integer doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            //LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            String response="";
            String show="";

            for(String url:params)
            {

                try
                {
                    JSONObject sending_data=new JSONObject();
                    //1 seller 2 user 3 buyer
                    if(isSeller)
                    sending_data.put("type",1);
                    else
                    sending_data.put("type",2);

                    Log.e("token",ref_token);
                    URL u=new URL(url);
                    URLConnection http_connection=(URLConnection) u.openConnection();

                    http_connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    http_connection.setRequestProperty("Accept", "application/json; charset=UTF-8");

                    http_connection.setDoOutput(true);

                    OutputStreamWriter Out_wr=new OutputStreamWriter(http_connection.getOutputStream());
                    Out_wr.write(sending_data.toString());
                    Out_wr.flush();
                    BufferedReader buf=new BufferedReader(new InputStreamReader(http_connection.getInputStream()));
                    String sr="";
                    Boolean wait=false;
                    while((sr=buf.readLine())!=null)
                    {
                        response+=sr;
                    }
                    Log.e("json", response);

                    //jsonroot=new JSONArray(response);
                    JSONObject jobj=new JSONObject(response);
                    boolean status=jobj.getBoolean("success");
                    if(status)
                        otp=jobj.getJSONObject("data").getInt("otp");

                }
                catch(JSONException je)
                {
                    je.printStackTrace();
                    Log.e("JsonException",je.getMessage());
                    return otp;

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Log.e("Exception",e.getMessage());
                    return otp;
                }
            }

            return otp;
        }
        @Override
        protected void onPostExecute(Integer result)
        {
            //Toast.makeText(getApplicationContext(),result+"",Toast.LENGTH_LONG).show();

            if(result!=0)
            {

                Log.e("token","sent to server");

            }
            else
            {
                Log.e("token","sending failed");
            }

        }
    }

}
