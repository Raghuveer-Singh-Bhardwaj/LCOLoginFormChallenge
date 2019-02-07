package com.robotemplates.webviewapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Leaderboard extends AppCompatActivity {
    TextView mTV;
    private Toolbar mToolbar;
    private RecyclerView mRV;
    private ProgressBar mPB;
    private ArrayList<User> mListOfUser = new ArrayList<>();

    private String tips;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        mAdView = findViewById(R.id.adView);
        mRV = findViewById(R.id.mRecyclerView);
        mPB = findViewById(R.id.mProgressBar);
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
        mPB.setVisibility(View.VISIBLE);

        getData();
    }
    private void sortList(ArrayList<User> list) {
        Collections.sort(list, (User u1, User u2) -> u1.getmRefferals()-u2.getmRefferals());
    }
    private void getData(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Refferers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> mListOfUserT = new ArrayList<>();

                    if(task.getResult()!=null){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mListOfUserT.add(document.toObject(User.class));
                        }

                        if(mListOfUserT.size()>0){
                            mPB.setVisibility(View.INVISIBLE);
                            Collections.sort(mListOfUserT, (User u1, User u2) -> u2.getmRefferals()-u1.getmRefferals());
                            LeaderBoardAdapter leaderBoardAdapter = new LeaderBoardAdapter(mListOfUserT);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Leaderboard.this);
                            mRV.setLayoutManager(linearLayoutManager);
                            mRV.setAdapter(leaderBoardAdapter);
                        }
                    }

                } else {

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
