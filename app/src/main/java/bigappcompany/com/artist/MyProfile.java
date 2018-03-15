package bigappcompany.com.artist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;
import bigappcompany.com.artist.network.PicassoTrustAll;

public class MyProfile extends BaseActivity implements View.OnClickListener{
String url="http://www.hotstarz.info/wp-content/uploads/2015/10/dzxois.png";
    ImageView img;
    RippleView rp_edit;
    Button bt_photos,bt_videos;
    private TextView tv_address,tv_contact,bt_name;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");
        img= (ImageView) findViewById(R.id.img_profile);






        img.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            number = u.getPhoneNumber();
        }
        
        ((RippleView)findViewById(R.id.rp_edit)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(MyProfile.this, RegisterActivity.class);
                i.putExtra(JsonParser.AR_MOB_NUM, number);
                i.putExtra("isEdit",true);
                startActivity(i);
            }
        });

        bt_photos=(Button)findViewById(R.id.bt_photos);
        bt_videos=(Button)findViewById(R.id.bt_videos);
        bt_photos.setOnClickListener(this);
        bt_videos.setOnClickListener(this);
        tv_address=(TextView)findViewById(R.id.tv_addr);
        tv_contact=(TextView)findViewById(R.id.tv_ph);
        bt_name=(TextView)findViewById(R.id.txt_name);
        ((TextView)findViewById(R.id.txt_name)).setText(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.AR_NAME,""));

        Download_web web=new Download_web(getApplicationContext(), new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                if(!response.equals(""))
                setupData(response);
            }
        });
        showDailog();
        web.execute(ApiUrl.PROFILE+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.ARTIST_ID,"")));
    }

    private void setupData(String response) {
        try {
            if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS))
            {
                JSONObject data=new JSONObject(response).getJSONObject(JsonParser.DATA);
                tv_address.setText(data.getString(JsonParser.AR_CITY));
                tv_contact.setText(data.getString(JsonParser.AR_MOB_NUM));
                bt_name.setText(data.getString(JsonParser.AR_NAME));
                try{
                ((TextView)findViewById(R.id.bio)).setText(data.getString(JsonParser.BIO));
                }
                catch (JSONException e){

                }
                try {
                    ((TextView) findViewById(R.id.desig)).setText(new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONObject(JsonParser.SKILLCATG).getString(JsonParser.CAT_NAME));
                }
                catch (JSONException e){

                }
                url=(ApiUrl.BASE_URL)+(data.getString(JsonParser.IMG_PROFILE));
                Picasso.with(MyProfile.this).load(url).into(img);
                /*ViewTreeObserver vto = img.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        Picasso.with(MyProfile.this).load(url).into(img);
                       // PicassoTrustAll.getInstance(getApplicationContext()).load(url).placeholder(R.drawable.profile).resize(img.getMeasuredWidth(), img.getMeasuredHeight()).centerCrop().into(img);
                        return true;
                    }
                });*/
            }
        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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

    private static final String TAG = "SampleActivity";

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(MyProfile.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }
    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            ResultActivity.startWithUri(MyProfile.this, resultUri,true);
        } else {
            Toast.makeText(MyProfile.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(MyProfile.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyProfile.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".jpg";


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(MyProfile.this);
    }
    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.withAspectRatio(1, 1);
        return uCrop;
    }
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();


        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(UCropActivity.DEFAULT_COMPRESS_QUALITY);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        return uCrop.withOptions(options);
    }

    @Override
    public void onClick(View v) {

        startActivity(new Intent(this,PotosVideosActivity.class));
    }

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
