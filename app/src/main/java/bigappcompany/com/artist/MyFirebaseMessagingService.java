package bigappcompany.com.artist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import bigappcompany.com.artist.network.JsonParser;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG="FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.e("type",remoteMessage.getData().get("type")+" ");
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        title=remoteMessage.getNotification().getTitle();
        body=remoteMessage.getNotification().getBody();
        if(remoteMessage.getData().size()>0)
        {
            if(remoteMessage.getData().containsKey("type"))
            {
                boolean satisfied=false;
                Log.e("FCM",remoteMessage.getData().get("type"));
                if(remoteMessage.getData().get("type").equals("New Booking"))
                {

                    if (!HomeActivity.active) {
                        generateNotification(new JSONObject(remoteMessage.getData()),HomeActivity.class);
                    } else {
                        sendMessageToActivity((new JSONObject(remoteMessage.getData())).toString());
                    }
                    satisfied=true;
                }

                if(!satisfied)
                    generateNotification_dummy();

            }
            else
            {
                //Log.e("FCM",remoteMessage.getData().get("type"));
                generateNotification_dummy();
            }
        }

    }


    private Notification myNotication;
    NotificationManager manager;
    String title="";
    String body="";
    private int requestId;
    private String res_id="";






    private void rate(String s) {
        Intent intent = new Intent("rate");
        // You can also include some extra data.
        intent.putExtra("data", s);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private  void sendMessageToActivity( String data) {
        Intent intent = new Intent("New Booking");
        // You can also include some extra data.
        intent.putExtra(JsonParser.DATA, data);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private  void extendTime( String data) {
        Intent intent = new Intent("extendTime");
        // You can also include some extra data.
        intent.putExtra("data", data);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void generateNotification_dummy() {
        try
        {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            Log.e("time",currentDateTimeString);
            Class cls;
            if(HomeActivity.active)
                cls=HomeActivity.class;
            else
                cls=HomeActivity.class;


            Intent intent = new Intent(this, cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//modified

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setAutoCancel(true);


            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
            builder.setNumber(100);
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(defaultSoundUri);
            //builder.setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);

            myNotication = builder.build();
            manager.notify(11, myNotication);



        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
    private void generateNotification(JSONObject data,Class c) {
        try {
            requestId = data.getInt("request_id");
            if(data.getString("type").equals("completeNotification"))
            {
                res_id=data.getString("res_id");
            }
        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }

        try
        {
            //String product_id = data.getString("product_id");
            //String product_name=data.getString("pname");

            //productId=data.getInt("pid");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            Log.e("request_id",requestId+"");



            Intent intent = new Intent(this, c);
            intent.putExtra("request_id",requestId);
            if(data.getString("type").equals("completeNotification"))
                intent.putExtra("res_id",res_id);


            //intent.putExtra("pid",productId);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            //
            builder.setAutoCancel(true);

            //builder.setTicker("this is ticker text");
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);

            builder.setSubText("Click to see the Response");
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setNumber(100);
            //Vibration
            //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            //builder.setLights(Color.RED, 3000, 3000);

            //Ton
            //builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/" + R.raw.solemn));
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(defaultSoundUri);
            //builder.setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);


            myNotication = builder.build();
            manager.notify(11, myNotication);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("exception ",e.getMessage().toString());
        }
    }


}
