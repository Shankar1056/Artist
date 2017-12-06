package bigappcompany.com.artist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artist.adapters.EventsAdapter;
import bigappcompany.com.artist.adapters.RequestsAdapter;
import bigappcompany.com.artist.models.RequestsModel;
import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;


public class EventsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String TAG_BOTTOM="Bottom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.TITILE));

        recyclerView=(RecyclerView)findViewById(R.id.list_recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                if(!response.equals("")) {
                    ArrayList<RequestsModel> models = new ArrayList<>();
                    try {
                        for (int j = 0; j < new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.APPROVED).length(); j++) {
                            //singleItem1.add(new SingleItemModel("Event " + j, images_[j%images_.length] + j));
                            models.add(new RequestsModel(new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.APPROVED).getJSONObject(j)));

                        }

                        setValues(models);
                        if(models.size()<=0)
                        {
                            Toast.makeText(getApplicationContext(),"No Events",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }
            }
        });
        showDailog();
        web.execute(ApiUrl.EVENTS+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.ARTIST_ID,"")));

    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    void setValues(ArrayList<RequestsModel> models)
    {
        recyclerView.setAdapter(new EventsAdapter(models));
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
