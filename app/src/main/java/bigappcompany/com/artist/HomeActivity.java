package bigappcompany.com.artist;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artist.adapters.CustomPagerAdapter;
import bigappcompany.com.artist.adapters.RecyclerViewDataAdapter;
import bigappcompany.com.artist.adapters.SectionListDataAdapter;
import bigappcompany.com.artist.models.PhotoModel;
import bigappcompany.com.artist.models.SectionDataModel;
import bigappcompany.com.artist.models.SingleItemModel;
import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;
import bigappcompany.com.artist.network.OnTaskCompleted;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QOlRzNIvZGvCPoUlTePnwumXP";
    private static final String TWITTER_SECRET = "b26ZiCaJ1RrJrSHGUiqcRk5uqRWr26kVpjbDSsyoMLOmEIhU3t";

    ArrayList<SectionDataModel> allSampleData;
    public static boolean active=false;
    private int previousPage;
    private int previousState;
    private ImageView img_main,navIV;
    private boolean geasturesEnable;
    private float previous;
    private float now;
    private int imgPtr = 0;
    private Button[] buttons;
    private ViewPager mViewPager;
    TextView maskTV;
    private CustomPagerAdapter mCustomPagerAdapter;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(" ");
        setStatusBarColor();
        setSupportActionBar(toolbar);
        active=true;



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //navView Width
        int width = getResources().getDisplayMetrics().widthPixels*4/5;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);
        /*LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)navigationView.getHeaderView(0).getLayoutParams();
        params1.width=width;
        params1.height=width*4/6;
        navigationView.getHeaderView(0).setLayoutParams(params1);*/

        allSampleData = new ArrayList<SectionDataModel>();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Home");

        }



        Download_web web=new Download_web(getApplicationContext(), new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                try {
                    if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
                        createData(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        showDailog();

        web.execute(ApiUrl.EVENTS+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.ARTIST_ID,"")));



        buttons = new Button[4];
        buttons[0] = (Button) findViewById(R.id.one);
        buttons[1] = (Button) findViewById(R.id.two);
        buttons[2] = (Button) findViewById(R.id.three);
        buttons[3] = (Button) findViewById(R.id.four);


        mViewPager = (ViewPager) findViewById(R.id.pager_zoom);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("onPageScrool","position "+position+" , positionoffset "+positionOffset+" , positionoffsetPixels"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //Log.e("onPageSelected", "position " + position);
                if (previousPage != position) {
                    //changed
                    buttons[previousPage].setVisibility(View.GONE);
                    buttons[position].setVisibility(View.VISIBLE);
                }
                previousPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.e("onScrollStateChanged", "state " + state);

                previousState = state;

            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("onPageScrool","position "+position+" , positionoffset "+positionOffset+" , positionoffsetPixels"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //Log.e("onPageSelected", "position " + position);
                if (previousPage != position) {
                    //changed
                    buttons[previousPage].setVisibility(View.GONE);
                    buttons[position].setVisibility(View.VISIBLE);
                }
                previousPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.e("onScrollStateChanged", "state " + state);

                previousState = state;

            }
        });

        img_main = (ImageView) findViewById(R.id.image_zoom);

        img_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (geasturesEnable == true) {
                    img_main.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previous = event.getX();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        now = event.getX();
                        if ((previous + 100) < (now)) {
                            //Log.e("action", "right");
                            previousImage();

                        } else if ((previous - 100) > now) {

                            nextImage();
                            //Log.e("action", "left");

                        } else
                            img_main.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
                }
                return false;
            }
        });
        setAdapterData();

        ((TextView)findViewById(R.id.tv_signout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).edit().clear().commit();
    
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Signout Successful",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("New Booking"));

    }

    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if(id== android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if(!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
            else
            {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
            startActivity(new Intent(this,AboutUsActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this,MyProfile.class));
        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(this, RequestsActivity.class));
        }
        else if(id==R.id.nav_events)
        {
            startActivity(new Intent(this, EventsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setStatusBarColor() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#3e4449"));
        }
    }
    String[] images_=new String[]{"https://s-media-cache-ak0.pinimg.com/564x/4e/03/15/4e0315db0aa580b8f8e794d568564d57.jpg","http://www.tollywoodshow.com/wp-content/gallery/sunitha-all-time-hot-pics/sinitha-12.jpg","http://www.hotstarz.info/wp-content/uploads/2015/10/dzxois.png","http://cdn.ytalkies.com/illusionzmedia/uploads/2015/08/Devi-Sri-Prasad-Birthday-Special-01.jpg"};
    public void createData(String response) {

        try
        {
            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Event Requests");



            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

            for (int j = 0; j < new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.PENDING).length(); j++) {
                //singleItem.add(new SingleItemModel("Event " + j, images_[j%images_.length] + j));

                    singleItem.add(new SingleItemModel(new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.PENDING).getJSONObject(j)));


            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);


            SectionDataModel dm1 = new SectionDataModel();

            dm1.setHeaderTitle("Upcoming Events");

            ArrayList<SingleItemModel> singleItem1 = new ArrayList<SingleItemModel>();
            for (int j = 0; j < new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.APPROVED).length(); j++) {
                //singleItem1.add(new SingleItemModel("Event " + j, images_[j%images_.length] + j));
                singleItem1.add(new SingleItemModel(new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.APPROVED).getJSONObject(j)));

            }

            dm1.setAllItemsInSection(singleItem1);

            allSampleData.add(dm1);
        }catch (JSONException je)
        {
            je.printStackTrace();
        }
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);
    }
    RecyclerView my_recycler_view ;
    private void nextImage() {
        if (imgPtr < 4) {
            buttons[imgPtr].setVisibility(View.GONE);
            imgPtr++;
            buttons[imgPtr].setVisibility(View.VISIBLE);
            //img_main.setImageBitmap(rowItems.get(imgPtr));


        } else {
            buttons[imgPtr].setVisibility(View.GONE);
            imgPtr = 0;
            buttons[imgPtr].setVisibility(View.VISIBLE);
            //img_main.setImageBitmap(rowItems.get(imgPtr));
        }
    }

    void previousImage() {
        if (imgPtr > 0) {
            buttons[imgPtr].setVisibility(View.GONE);
            imgPtr--;
            buttons[imgPtr].setVisibility(View.VISIBLE);
            //img_main.setImageBitmap(images.get(imgPtr));
        } else {
            buttons[imgPtr].setVisibility(View.GONE);
            imgPtr = 4;
            buttons[imgPtr].setVisibility(View.VISIBLE);
            //img_main.setImageBitmap(rowItems.get(imgPtr));
        }


    }

    void setAdapterData()
    {
        ArrayList<PhotoModel> models=new ArrayList<>();
        PhotoModel model=new PhotoModel("1","https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model1=new PhotoModel("2","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model2=new PhotoModel("3","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model3=new PhotoModel("4","https://static.pexels.com/photos/163798/motor-scooter-vespa-roller-cult-163798.jpeg","Title1","Description1","26-2-2017");
        models.add(model);
        models.add(model1);
        models.add(model2);
        models.add(model3);
        CustomPagerAdapter adapter=new CustomPagerAdapter();
        adapter.isDynamic(true);
        adapter.setData(models,this,this);
        adapter.setVP(mViewPager);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        active=false;
        ((CustomPagerAdapter)mViewPager.getAdapter()).stopTimer();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        ((CustomPagerAdapter)mViewPager.getAdapter()).pageSwitcher(3);
        active=true;
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_name)).setText("Hi, "+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString((JsonParser.AR_NAME),"Guest")));
    
    
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

    @Override
    public void onStart()
    {
        active=true;
        super.onStart();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.e("inside","reciever");
            String data = intent.getStringExtra(JsonParser.DATA);
            try {

                notifyNewBooking(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void notifyNewBooking(JSONObject object) {
        try {

            Log.e("inside","add");
            //((RecyclerViewDataAdapter)my_recycler_view.getAdapter()).addItem(new SingleItemModel(object),0,0);
            ((SectionListDataAdapter)((RecyclerView)my_recycler_view.getLayoutManager().findViewByPosition(0).findViewById(R.id.recycler_view_list)).getAdapter()).addItem(new SingleItemModel(object),0);
            my_recycler_view.getAdapter().notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
