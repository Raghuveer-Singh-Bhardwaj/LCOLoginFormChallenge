package com.robotemplates.webviewapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerMessageActivity extends AppCompatActivity {
    TextView mTV,mTitle;
    private Toolbar mToolbar;
    private String tips;
    private AdView mAdView;
    private Button mOK,mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_message);
 // Force a crash

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mTV = findViewById(R.id.mTV);
        mTitle = findViewById(R.id.mTitle);
        mOK = findViewById(R.id.mOkBtn);
        mCancel = findViewById(R.id.mCancel);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.cricket);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getData();

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ServerMessageActivity.this, MainActivity.class);
                finish();  //Kill the activity from which you will go to next activity
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
       finish();
       System.exit(0);
    }

    private void getData(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L2V4dHJhcw==", Base64.DEFAULT)))
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
                        final String link = postObj.getString("serverMessageLink");

                        if (link.equals("Empty")){

                            mOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(ServerMessageActivity.this, MainActivity.class);
                                    finish();  //Kill the activity from which you will go to next activity
                                    startActivity(i);
                                }
                            });
                        }else{

                            mOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                    finish();
                                    startActivity(browserIntent);

                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                    mTV.setText(message);
                                    mTitle.setText(titletxt);
                                    SharedPreferences.Editor editor = getSharedPreferences("SERVER_MESSAGE", MODE_PRIVATE).edit();
                                    editor.putBoolean(titletxt, false);
                                    editor.apply();


                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }});
    }
}
