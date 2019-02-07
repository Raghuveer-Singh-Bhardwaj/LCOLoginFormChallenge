package com.robotemplates.webviewapp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TabBaseFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,PaidInterface {

    private static final int RC_SIGN_IN = 545;
    private static final int REQUEST_READ_PHONE_STATE = 4;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private DrawerLayout mDrawer;
    public boolean loginpage;
    private ViewPager viewPager;
    private String bank="MyDeatil";
    private TabLayout tabLayout;
    private ArrayList<Sport> mListOfSport = new ArrayList() {
    };
    private ArrayList<Posts> mListOfPost = new ArrayList() {};

    private ArrayList<Team> mListOfTeam = new ArrayList() {
    };

    private ArrayList<PaidSport> mListOfPaidSport = new ArrayList<>();
    private ArrayList<PaidUser> mListOfPaidUser = new ArrayList<>();

    private ArrayList<PaidUser> mListOUUser = new ArrayList<>();
    private ArrayList<Sport> mListOfSportD = new ArrayList() {
    };
    private ArrayList<Posts> mListOfPostD = new ArrayList() {
    };
    private ArrayList<Team> mListOfTeamD = new ArrayList() {
    };
    private MyAdapter myAdapter;
    private ActionBarDrawerToggle toggle;
    private String shareText;
    private String email;
    private String fburl;
    private AdView mAdView;
    String refferuids="";
    OkHttpClient client = new OkHttpClient();
    private AdvancedWebView mWebView;


  /*  private void getDataPaidSport() throws IOException {


        final Request request = new Request.Builder()
                .url("http://139.59.86.238/paidsport/")
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mPaidSport = jsonArray.getJSONObject(i);
                            int id = mPaidSport.getInt("id");
                            int paidsportid = mPaidSport.getInt("sportid");
                            String sportname = mPaidSport.getString("paidsportname");
                            String testemonials = mPaidSport.getString("testemonials");
                            PaidSport paidSport = new PaidSport();
                            paidSport.setId(id);
                            paidSport.setSportid(paidsportid);
                            paidSport.setPaidsportname(sportname);
                            paidSport.setTestemonials(testemonials);
                            mListOfPaidSport.add(paidSport);

                        }
                            getDataPaidUser();
                        response.close();


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
        });
    }
    private void getDataBank(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://139.59.86.238/extras")
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);

                        JSONObject postObj = jsonArray.getJSONObject(0);
                        bank = postObj.getString("bankdetails");

                        try{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        MainActivity.this.bank = bank;
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }});
    }
    private void getDataPaidUser() {

        final Request request = new Request.Builder()
                .url("http://139.59.86.238/paiduser/")
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Fail Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mPaidUser = jsonArray.getJSONObject(i);
                            String email = mPaidUser.getString("email");
                            String password = mPaidUser.getString("password");
                            int id = mPaidUser.getInt("id");
                            int psportid = mPaidUser.getInt("paidsportid");
                            String date = mPaidUser.getString("expdate");
                            String sportname = mPaidUser.getString("paidsportname");
                            PaidUser paiduser = new PaidUser();
                            paiduser.setEmail(email);
                            paiduser.setId(id);
                            paiduser.setPaidsportname(sportname);
                            paiduser.setStartdate(date);
                            paiduser.setPaidsportid(psportid);
                            paiduser.setId(id);
                            paiduser.setPassword(password);
                            mListOfPaidUser.add(paiduser);
                        }

                        response.close();

                        try {
                            getDataSport();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Error in catch"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }*/



    private void validatePaidUser(String email,String password){

    }

    private void getDataTeam() throws IOException {


        final Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L3RlYW1z",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObj = jsonArray.getJSONObject(i);
                            String name = postObj.getString("name");
                            int id = postObj.getInt("id");
                            int sport = postObj.getInt("sport");
                            String imageUrl = postObj.getString("image");
                            Team team = new Team();
                            team.setId(id);
                            team.setName(name);
                            team.setSport(sport);
                            team.setImageUrl(imageUrl);

                            mListOfTeamD.add(team);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    viewPager.getAdapter().notifyDataSetChanged();
//                                viewPager.getAdapter().notifyDataSetChanged();

                              /*  myAdapter = new MyAdapter(getSupportFragmentManager(),mListOfPost,mListOfSport,mListOfTeam);
                                viewPager = findViewById(R.id.mViewPager);
                                viewPager.setAdapter(myAdapter);
                                tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
                                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                               */

                                }
                            });
                        }
                        try {
                            getDataPost();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        response.close();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void shareToGMail(String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }

    private void getDataPost() throws IOException {
        //  OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L3Bvc3Rz",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObj = jsonArray.getJSONObject(i);
                            String title = postObj.getString("title");
                            int id = postObj.getInt("id");
                            int sportId = postObj.getInt("sport");
                            int team1id = postObj.getInt("team1");
                            int team2id = postObj.getInt("team2");
                            String date = postObj.getString("date");
                            String content = postObj.getString("content");
                            Posts post = new Posts();
                            post.setId(id);
                            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yy");

                            try {

                                String reformattedStr = myFormat.format(fromUser.parse(date));
                                post.setDate(reformattedStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            post.setContent(content);
                            post.setSport(sportId);
                            post.setTeam1(team1id);
                            post.setTeam2(team2id);
                            post.setTitle(title);
                            mListOfPostD.add(post);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    viewPager.getAdapter().notifyDataSetChanged();
                                    //  viewPager.getAdapter().notifyDataSetChanged();

                                }
                            });
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //  viewPager.getAdapter().notifyDataSetChanged();
                                swapData();
                           //     myAdapter.notifyDataSetChanged();
                                MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), mListOfPost, mListOfSport, mListOfTeam,MainActivity.this,mListOfPaidUser,MainActivity.this,mListOfPaidSport,bank);
                                viewPager.setAdapter(myAdapter);
                             //   viewPager.getAdapter().notifyDataSetChanged();

                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getDataSport() throws IOException {
        // OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L3Nwb3J0cw==",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        Sport sport;
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject sportObj = jsonArray.getJSONObject(i);
                            String name = sportObj.getString("name");
                            int id = sportObj.getInt("id");
                            sport = new Sport();
                            sport.setId(id);
                            sport.setName(name);
                            mListOfSportD.add(sport);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                     updateFrames();
                                    viewPager.getAdapter().notifyDataSetChanged();

                                }
                            });
                        }

                        try {
                            getDataTeam();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getDataServer(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L2V4dHJhcw==",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);

                        JSONObject postObj = jsonArray.getJSONObject(0);
                        final String titletxt = postObj.getString("serverMessageTitle");
                        final String message = postObj.getString("serverMessage");
                        if(titletxt!=null && message!=null){
                            if(titletxt.equals("Empty") && message.equals("Empty")){

                            }else{
                                SharedPreferences prefs = getSharedPreferences("SERVER_MESSAGE", MODE_PRIVATE);
                                boolean showIt = prefs.getBoolean(titletxt,true);
                                if(showIt){

                                    Intent i = new Intent(MainActivity.this, ServerMessageActivity.class);
                                    finish();  //Kill the activity from which you will go to next activity
                                    startActivity(i);
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }});
    }

    public void getHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.i("Base64", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        getHashkey();
        if (isEmulator()) {
            Toast.makeText(this, "This app can't run on emulator", Toast.LENGTH_LONG).show();
            finish();
        }




        if(getIntent().getData()!=null){
            Uri uri = this.getIntent().getData();
            Log.d("YOYO","Reffered by:"+uri);
            if (uri.toString().length()>0){

                String referrerUid=uri.toString().substring(uri.toString().indexOf("=")+1);
                if (referrerUid != null || referrerUid.length() > 0)
                    refferuids = referrerUid;
                    createAnonymousAccountWithReferrerInfo(referrerUid);

                Log.d("YOYO","Reffered by:"+referrerUid);
            }

        }else{
            Log.d("YOYO","Reffered empty");
        }


       /* if(FirebaseAuth.getInstance().getCurrentUser() == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build())
                    ;


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
*/
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        MobileAds.initialize(this, "ca-app-pub-8920798231622156~3808206087");

        mProgressBar = findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);


        if(!isOnline(this)){
            showAlertDialog();
        }else{
            getData("");
        }
        viewPager = findViewById(R.id.mViewPager);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        try {
            getDataSport();


        } catch (IOException e) {
            e.printStackTrace();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setLogo(R.mipmap.ic_icon_round);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawer = findViewById(R.id.drawer_layout);
        mToolbar.setNavigationIcon(R.drawable.ic_icons8_menu);
        toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.app_name,
                R.string.app_name);
        toggle.setHomeAsUpIndicator(R.drawable.ic_icons8_menu);

        mDrawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icons8_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myAdapter = new MyAdapter(getSupportFragmentManager(), mListOfPost, mListOfSport, mListOfTeam,MainActivity.this,mListOfPaidUser,MainActivity.this,mListOfPaidSport,bank);
        viewPager.setAdapter(myAdapter);
        tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    /*    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

       /*  try {
           getDataSport();
            getDataTeam();
            getDataPost();
            viewPager.getAdapter().notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }




    private void showReferDialog() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Have you been referred by anyone?");
        final EditText mRefferCode = new EditText(this);
        builder.setView(mRefferCode);
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();




        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               createAnonymousAccountWithReferrerInfo(mRefferCode.getText().toString());


            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public final void Facebook(String yourpageid) {
        final String urlFb = "fb://facewebmodal/f?href=" + yourpageid;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If Facebook application is installed, use that else launch a browser
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/pages/" + yourpageid;
            intent.setData(Uri.parse(urlBrowser));
        }

        startActivity(intent);
    }

    private void updateFrames() {
        try{
            for (Sport sports : mListOfSport) {
                Sport sport = sports;
                Log.d("ID", sport.getName());
                runOnUiThread(new Runnable() {
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                        tabLayout.addTab(tabLayout.newTab().setText(sport.getName()));

                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        }catch (Exception e){
            updateFrames();
            e.printStackTrace();
        }

    }

    private void swapData() {
    /*    Collections.sort(mListOfPostD, new Comparator<Posts>() {
            public int compare(Posts p1,Posts p2) {
                return p1.getDate().compareTo(p2.getDate());
            }
        });*/
        Collections.sort(mListOfPostD, new Comparator<Posts>() {
            public int compare(Posts o1, Posts o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });

        mListOfSport = mListOfSportD;
        for(Sport sport:mListOfSportD){
            for(PaidSport paidSport:mListOfPaidSport){
                if(paidSport.getPaidsportname().equals(sport.getName())){
                    int index = mListOfSport.indexOf(sport);
                    Sport sport1 = sport;
                    sport1.setPaid(true);
                    mListOfSport.set(index,sport1);
                }
            }
        }
        // Collections.sort(mListOfPostD, (o1, o2) -> Integer.parseInt(o1.getDate()) - Integer.parseInt(o2.getDate()));
        mListOfPost = mListOfPostD;

        myAdapter.notifyDataSetChanged();
        // Collections.reverse(mListOfPost);
        mListOfSport = mListOfSportD;
        mListOfTeam = mListOfTeamD;
        viewPager.getAdapter().notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


           /* case R.id.fb:{
                getData("FB");
                break;

            }*/


            case R.id.share:
                getData("SHARE");
                break;
            case R.id.contact:
                getData("GMAIL");
                break;
            case R.id.contest_code:
                startActivity(new Intent(this,LeaugeCode.class));
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(this,PrivacyPolicy.class));
                break;
            case R.id.referral:
                startActivity(new Intent(this,ReferActivity.class));
                break;
            case R.id.mleaderboard:
                startActivity(new Intent(this,Leaderboard.class));
                break;
            case R.id.donate:
                startActivity(new Intent(this,Donate.class));
                break;
            case R.id.disclaimer:
                startActivity(new Intent(this,Disclaimer.class));
                break;
            case R.id.rate:
                Context context = this.getApplicationContext();
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
                break;

        }
        //close navigation drawer
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getOpenFacebookIntent(Context context, String url) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/HjwgcgCo9VX"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/appdevpanda"));
        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Download Dream11 App");
        share.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void getData(final String operation) {
        // OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L2V4dHJhcw==",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("Server is under maintanenece")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        final AlertDialog dialog = builder.create();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });

                        if(!MainActivity.this.isFinishing())
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        dialog.show();
                                    }catch (Exception e){

                                    }
                                }
                            });
                            //show dialog
                        }



                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("Server is under maintanenece")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });
                            dialog.show();
                        }
                    });*/
                    // throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);

                        JSONObject postObj = jsonArray.getJSONObject(0);
                        shareText = postObj.getString("shareText");
                        fburl = postObj.getString("facebookPageLink");
                        String privacyPolicy = postObj.getString("privacyPolicy");
                        String tips = postObj.getString("tips");
                        String leaugeCode = postObj.getString("leaugeCode");
                        email = postObj.getString("contact");
                        String isServerDown = postObj.getString("isServerDown");
                        Log.d("ExtrasINFO", isServerDown);
                        if (isServerDown.contains("Yes")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("Server is under maintanenece")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            });
                                    final AlertDialog dialog = builder.create();
                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            finish();
                                        }
                                    });

                                    Handler handler = new Handler();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            dialog.show();
                                        }
                                    });
                                }
                            });


                        }

                        getDataServer();
                        if (operation == "FB") {

                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                            String facebookUrl = getFacebookPageURL(MainActivity.this, fburl);
                            facebookIntent.setData(Uri.parse(facebookUrl));
                            startActivity(facebookIntent);
                        } else if (operation == "SHARE") {
                            shareTextUrl();
                        } else if (operation == "GMAIL") {
                            shareToGMail(new String[]{email}, "Dream 11 Expert Prediction Tips,News and Teams", "");
                        }


                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("Server is under maintanenece")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                final AlertDialog dialog = builder.create();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                });

                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                });
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Error");
        builder.setMessage("No Network Connection").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static Intent openFacebook(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/376227335860239"));
        } catch (Exception e) {

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/karthikofficialpage"));
        }


    }

    public String getFacebookPageURL(Context context, String FACEBOOK_URL) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                String id = FACEBOOK_URL.replace("https://facebook.com/", "");
                return "fb://page/" + id;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void setupView() {
        // webview settings
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        //  mWebView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);

        // advanced webview settings
        //      mWebView.setListener(MainActivity.this, this);
        mWebView.setGeolocationEnabled(true);

        // webview style
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // fixes scrollbar on Froyo

        // webview hardware acceleration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // webview chrome client
      /*  View nonVideoLayout = getActivity().findViewById(R.id.activity_main_non_video_layout);
        ViewGroup videoLayout = getActivity().findViewById(R.id.activity_main_video_layout);
        View progressView = getActivity().getLayoutInflater().inflate(R.layout.placeholder_progress, null);
        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, progressView, (VideoEnabledWebView) mWebView);
        webChromeClient.setOnToggledFullscreen(new MyToggledFullscreenCallback());
       */ //mWebView.setWebChromeClient(webChromeClient);
        //mWebView.setWebChromeClient(new MyWebChromeClient()); // not used, used advanced webview instead

        // webview client
        //   mWebView.setWebViewClient(new MyWebViewClient());

        // webview key listener
        //     mWebView.setOnKeyListener(new WebViewOnKeyListener((DrawerStateListener) getActivity()));

        // webview touch listener
        mWebView.requestFocus(View.FOCUS_DOWN); // http://android24hours.blogspot.cz/2011/12/android-soft-keyboard-not-showing-on.html
        //mWebView.setOnTouchListener(new WebViewOnTouchListener());

        // webview scroll listener
        //((RoboWebView) mWebView).setOnScrollListener(new WebViewOnScrollListener()); // not used


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 8547) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("INVITE", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        } else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Have you been referred by anyone?");
                final EditText mRefferCode = new EditText(this);
                builder.setView(mRefferCode);
                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User mUser = task.getResult().toObject(User.class);
                        if (mUser != null) {

                            if (mUser.getmPhone() != null && mUser.getmPhone().length() > 0) {

                            }
                        } else {

                        }
                    }
                });
                firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() == null) {
                        }
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firestore.collection("Users").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().toObject(User.class) != null) {
                                    if (!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid())) {
                                        firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                User mUser = task.getResult().toObject(User.class);
                                                if (mUser != null) {

                                                    if (mUser.getmPhone() != null && mUser.getmPhone().length() > 0) {

                                                    }
                                                } else {

                                                }
                                            }
                                        });
                                        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task != null) {
                                                    if (task.getResult() != null) {
                                                        User user = task.getResult().toObject(User.class);
                                      /*  if(user!=null){
                                            user.setmRefferals(user.getmRefferals()+1);
                                            firestore.collection("Users").document(mRefferCode.getText().toString()).set(user);
                                        }else{*/
                                                        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task != null) {
                                                                            if (task.getResult() != null) {
                                                                                User user = task.getResult().toObject(User.class);
                                                                                ArrayList<String> mListOfRefferers = new ArrayList<>();
                                                                                if (user.getmListOfRefferers() != null) {
                                                                                    if (!(user.getmListOfRefferers().contains(mRefferCode.getText().toString())) && !(user.getmListOfRefferers().size() > 1)) {
                                                                                        if (user.getmListOfRefferers() != null) {
                                                                                            mListOfRefferers = user.getmListOfRefferers();
                                                                                            mListOfRefferers.add(mRefferCode.getText().toString());

                                                                                        } else {
                                                                                            mListOfRefferers.add(mRefferCode.getText().toString());
                                                                                        }
                                                                                        user.setmListOfRefferers(mListOfRefferers);
                                                                                        if (user.getmRefferals() != 0) {
                                                                                            int referrals = user.getmRefferals();
                                                                                            Log.d("YUVA", " " + referrals);

                                                                                            referrals++;


                                                                                            Log.d("YUVA", " " + referrals);
                                                                                            user.setmRefferals(referrals);

                                                                                            firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                                            //firestore.collection("Users").document(mRefferCode.getText().toString()).set(user);
                                                                                        } else {
                                                                                            user.setmRefferals(1);
                                                                                            firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                                        }


                                                                                        firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task != null) {
                                                                                                    if (task.getResult() != null) {
                                                                                                        User user = task.getResult().toObject(User.class);
                                                                                                        if (user != null) {

                                                                                            /*    int balanceStr = user.getmRefferals();
                                                                                                int balance = balanceStr * 10;
                                                                                                String balanceTXT = "₹ "+balance;
                                                                                                mBalance.setText(balanceTXT);*/
                                                                                                        } else {
                                                                                                            user.setmRefferals(user.getmRefferals() + 1);
                                                                                                            firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } else {
                                                                                    firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            User mUser = task.getResult().toObject(User.class);
                                                                                            if (mUser != null) {

                                                                                                if (mUser.getmPhone() != null && mUser.getmPhone().length() > 0) {


                                                                                                }
                                                                                            } else {

                                                                                            }
                                                                                        }
                                                                                    });
                                                                                    if (user.getmListOfRefferers() != null) {
                                                                                        mListOfRefferers = user.getmListOfRefferers();
                                                                                        mListOfRefferers.add(mRefferCode.getText().toString());

                                                                                    } else {
                                                                                        mListOfRefferers.add(mRefferCode.getText().toString());
                                                                                    }
                                                                                    user.setmListOfRefferers(mListOfRefferers);
                                                                                    if (user.getmRefferals() != 0) {
                                                                                        int referrals = user.getmRefferals();
                                                                                        Log.d("YUVA", " " + referrals);

                                                                                        referrals++;


                                                                                        Log.d("YUVA", " " + referrals);
                                                                                        user.setmRefferals(referrals);

                                                                                        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                                    } else {
                                                                                        user.setmRefferals(1);
                                                                                        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                                    }

                                                                                    firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task != null) {
                                                                                                if (task.getResult() != null) {
                                                                                                    User user = task.getResult().toObject(User.class);
                                                                                                    if (user != null) {

                                                                                                        user.setmRefferals(user.getmRefferals() + 1);
                                                                                          /*  int balanceStr = user.getmRefferals();
                                                                                            int balance = balanceStr * 10;
                                                                                            String balanceTXT = "₹ "+balance;
                                                                                            mBalance.setText(balanceTXT);*/
                                                                                                        firestore.collection("Refferers").document(mRefferCode.getText().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                            }
                                                                                                        });
                                                                                                    } else {
                                                                                           /* user = new User();
                                                                                            user.setmRefferals(user.getmRefferals()+1);*/
                                                                                                        firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                if (task != null) {
                                                                                                                    if (task.getResult() != null) {
                                                                                                                        User user = task.getResult().toObject(User.class);
                                                                                                                        if (user != null) {

                                                                                                           /*     int balanceStr = user.getmRefferals();
                                                                                                                int balance = balanceStr * 10;
                                                                                                                String balanceTXT = "₹ "+balance;
                                                                                                                mBalance.setText(balanceTXT);*/
                                                                                                                        } else {
                                                                                                                            user = new User();
                                                                                                                            user.setmRefferals(user.getmRefferals() + 1);
                                                                                                                            firestore.collection("Refferers").document(mRefferCode.getText().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                }
                                                                                                                            });
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }

                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }
                                                }
                                            }

                                        });


                                    } else {
                                        Toast.makeText(MainActivity.this, "You can't refer yourself", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Invalid refer code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null) {
                                if (user.getmListOfRefferers() != null) {

                                    if (user.getmListOfRefferers().size() == 0) {

                                        builder.show();
                                    }
                                } else {
                                    builder.show();
                                }
                            } else {
                                builder.show();
                            }

                        }
                    }
                });
            }
        }

    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

        }else {
            final String deviceid = telephonyManager.getDeviceId();
            try{
                FirebaseFirestore.getInstance().collection("Registered").document(deviceid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().exists()){
                                FirebaseFirestore.getInstance().collection("Refferers").document(referrerUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                                        User mUser = task.getResult().toObject(User.class);
                                        //      Log.d("YOYO","Refers"+mUser.getmRefferals());
                                        ArrayList<String> mListOfRefferers = new ArrayList<>();
                                        if (mUser != null) {
                                            if(mUser.getmListOfRefferers()!=null){
                                                mListOfRefferers = mUser.getmListOfRefferers();
                                                if(!mListOfRefferers.contains(deviceid)){
                                                    if(FirebaseAuth.getInstance().getCurrentUser()==null){
                                                        mListOfRefferers.add(deviceid);
                                                        mUser.setmRefferals(mUser.getmRefferals()+1);
                                                    }else{
                                                        if(!FirebaseAuth.getInstance().getUid().equals(task.getResult().getId())){
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    //      Toast.makeText(MainActivity.this,task.getResult().getId(),Toast.LENGTH_LONG);
                                                                }
                                                            });
                                                            mListOfRefferers.add(deviceid);
                                                            mUser.setmRefferals(mUser.getmRefferals()+1);
                                                        }else{
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(MainActivity.this,"You can't refer yourself",Toast.LENGTH_LONG);
                                                                }
                                                            });
                                                        }
                                                    }
                                                }{
                                                    Toast.makeText(MainActivity.this,"You are already registered",Toast.LENGTH_SHORT).show();

                                                }


                                                //  Log.d("YOYO","Refers"+mUser.getmRefferals());
                                            }else{
                                                mListOfRefferers.add(deviceid);
                                                mUser.setmRefferals(mUser.getmRefferals()+1);
                                                //  Log.d("YOYO","Refers"+mUser.getmRefferals());
                                            }

                                            mUser.setmListOfRefferers(mListOfRefferers);

                                            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                                                if(!FirebaseAuth.getInstance().getUid().equals(referrerUid)) {
                                                    FirebaseFirestore.getInstance().collection("Registered").document(deviceid).set(new User());
                                                    FirebaseFirestore.getInstance().collection("Refferers").document(referrerUid).set(mUser);
                                                }else{
                                                    Toast.makeText(MainActivity.this,"You can't refer yourself",Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                FirebaseFirestore.getInstance().collection("Registered").document(deviceid).set(new User());
                                                FirebaseFirestore.getInstance().collection("Refferers").document(referrerUid).set(mUser);
                                            }
                                        }else{
                                            Log.d("YOYO",referrerUid+"dosen't exist");
                                        }
                                        Log.d("YOYO",referrerUid+ FirebaseAuth.getInstance().getUid());

                                    }
                                });
                            }else{
                                Toast.makeText(MainActivity.this,"You are already registered",Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });

            }catch(Exception e){
                try{
                    FirebaseFirestore.getInstance().collection("Registered").document(deviceid).set(new User()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    });
                }catch (Exception ee){
                    ee.printStackTrace();
                }

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                createAnonymousAccountWithReferrerInfo(refferuids);
                //do ur specific task after read phone state granted
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLogin(int sportid) {

     /*   ArrayList<Sport> dupl = mListOfSport;
        for(Sport sp:dupl){
            if(sportid == sp.getId()){
                int index = mListOfSport.indexOf(sp);
                Sport s = sp;
                s.setPaid(false);
                mListOfSport.set(index,s);
                Toast.makeText(this, "Yes it is ", Toast.LENGTH_SHORT).show();
                viewPager.setAdapter(myAdapter);

            }
        }*/
        ArrayList<Posts> duplistofpost = mListOfPost;
        ArrayList<Posts> duplistofpost2 = mListOfPost;
        Iterator<Posts> iterator = mListOfPost.iterator();
        while (iterator.hasNext()){
            Posts p = iterator.next();
            if(p.getSport()==sportid){
                iterator.remove();

            }
        }
     /*   for(Posts post:duplistofpost){
            if(post.getSport()==sportid){
                duplistofpost2.remove(2);
            }
        }*/
        getPremiumPosts();
    }

    @Override
    public void savedLogin(int sportid) {

    }

    private void getPremiumPosts() {
        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovLzEzOS41OS44Ni4yMzgvcG9zdHM=",Base64.DEFAULT)))
                .get()
                .addHeader("Content-Type", "application/json")
                //  .addHeader("Authorization", "Basic UmFnaHV2ZWVyOkRSQUdPTkJBTExa")
                .addHeader("cache-control", "no-cache")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObj = jsonArray.getJSONObject(i);
                            String title = postObj.getString("title");
                            int id = postObj.getInt("id");
                            int sportId = postObj.getInt("sport");
                            int team1id = postObj.getInt("team1");
                            int team2id = postObj.getInt("team2");
                            String date = postObj.getString("date");
                            String content = postObj.getString("content");
                            Posts post = new Posts();
                            post.setId(id);
                            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yy");

                            try {

                                String reformattedStr = myFormat.format(fromUser.parse(date));
                                post.setDate(reformattedStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            post.setContent(content);
                            post.setSport(sportId);
                            post.setTeam1(team1id);
                            post.setTeam2(team2id);
                            post.setTitle(title);
                            mListOfPostD.add(post);


                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                swapData();
                                viewPager.getAdapter().notifyDataSetChanged();
                                //  viewPager.getAdapter().notifyDataSetChanged();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
