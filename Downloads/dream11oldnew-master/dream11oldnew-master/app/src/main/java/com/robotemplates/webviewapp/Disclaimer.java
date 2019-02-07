package com.robotemplates.webviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

public class Disclaimer extends AppCompatActivity {
    TextView mTV;
    private Toolbar mToolbar;
    private String tips;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mTV = findViewById(R.id.mTV);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.cricket);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getData();
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
                        final String disclaimer = postObj.getString("thedisclaimer");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTV.setText(disclaimer);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }});
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
