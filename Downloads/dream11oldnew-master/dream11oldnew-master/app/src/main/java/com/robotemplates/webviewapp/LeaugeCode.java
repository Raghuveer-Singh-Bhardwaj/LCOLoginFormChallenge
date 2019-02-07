package com.robotemplates.webviewapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeaugeCode extends AppCompatActivity {
    TextView mTV;
    private Toolbar mToolbar;
    private String leaugeCode;
    private ProgressBar mProgressBar;
    private TextView mEmptyText;
    private AdView mAdView;
    private Button mAddContestCode;
    private TableLayout mTableLayout;
    private LinearLayout mLinearLayout;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leauge_code);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mTV = findViewById(R.id.mTV);
        mEmptyText = findViewById(R.id.mEmptyText);
        mProgressBar = findViewById(R.id.mProgressBar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.cricket);
        mAddContestCode = (Button)findViewById(R.id.mAddContest);
        mTableLayout = findViewById(R.id.mTable);
        mLinearLayout = findViewById(R.id.mDashboard);
        mAddContestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder
                        builder = new AlertDialog.Builder(LeaugeCode.this);
                builder.setTitle("Add Contest Code");
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.add_contest,null);
                final EditText matchName = view.findViewById(R.id.mMatchName);
                final EditText winningAmount = view.findViewById(R.id.mWinningAmount);
                final EditText contestSize = view.findViewById(R.id.mContestSize);

                final EditText entryFee = view.findViewById(R.id.mEntryFee);
                final EditText contestCode = view.findViewById(R.id.mContestCode);
             //   final EditText date = view.findViewById(R.id.mDate);
                final EditText winner = view.findViewById(R.id.mWinner);

                final Switch mSwitch = view.findViewById(R.id.multipleTeamSwitch);
                builder.setView(view);
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("OK",null);
                final AlertDialog mDialog  = builder.create();
                mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(matchName.getText().length()>0 && winningAmount.length()>0 && contestSize.length()>0 && entryFee.length()>0 && winner.length()>0 && contestCode.getText().toString().length()>=13){
                                    String multiTeam = "No";
                                    if(mSwitch.isChecked()){
                                        multiTeam = "Yes";
                                    }
                                    TextView mHeading = new TextView(LeaugeCode.this);
                                    mHeading.setText("Contest Size - Entry Fee - Winning Amount - Winner - Multiple Team");

                                  //  mLinearLayout.addView(mHeading);
                                   // mLinearLayout.addView(new TextView(LeaugeCode.this));
                                    TextView matchnametv = new TextView(LeaugeCode.this);
                                    matchnametv.setText(matchName.getText().toString());
                                    matchnametv.setGravity(Gravity.CENTER);
                                    matchnametv.setTypeface(null, Typeface.BOLD);

                                 //   TextView datetv = new TextView(LeaugeCode.this);
                                 //   datetv.setText(date.getText().toString());
                                //    datetv.setGravity(Gravity.CENTER);
                                //    datetv.setTypeface(null, Typeface.BOLD);

                                    TextView lasttv = new TextView(LeaugeCode.this);
                                    lasttv.setText(contestSize.getText().toString()+"-"+entryFee.getText().toString()+"-"+winningAmount.getText().toString()+"-"+winner.getText().toString()+"-"+multiTeam.substring(0,1));
                                    lasttv.setTypeface(null, Typeface.BOLD);

                                    lasttv.setGravity(Gravity.CENTER);

                                    TextView codetv = new TextView(LeaugeCode.this);
                                    codetv.setText(contestCode.getText().toString());
                                    codetv.setGravity(Gravity.CENTER);
                                  //  codetv.setText(contestcode);
                                    codetv.setTextColor(Color.RED);
                                    TextView copytv = new TextView(LeaugeCode.this);
                                    copytv.setText("Copy Code");
                                    copytv.setTextColor(Color.BLUE);
                                    copytv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("label", contestCode.getText().toString());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(LeaugeCode.this,"Contest code copied to clipboard!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    copytv.setGravity(Gravity.CENTER);

                                  //  mLinearLayout.addView(datetv);


                                    try{
                                        mLinearLayout.addView(copytv,1);
                                        mLinearLayout.addView(codetv,1);
                                        mLinearLayout.addView(lasttv,1);
                                        mLinearLayout.addView(matchnametv,1);
                                        mLinearLayout.addView(new TextView(LeaugeCode.this),0);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    TableRow row = new TableRow(LeaugeCode.this);
                                    final TextView mMatchNameView = new TextView(LeaugeCode.this);
                                    final TextView mWinningAmount = new TextView(LeaugeCode.this);
                                    final TextView mMultipleTeams = new TextView(LeaugeCode.this);
                                    final TextView mContestSize = new TextView(LeaugeCode.this);
                                    final TextView mEntryFee = new TextView(LeaugeCode.this);
                                    final TextView mContestCode = new TextView(LeaugeCode.this);

                                    final ImageView imageView = new ImageView(LeaugeCode.this);
                                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_copy));
                                    mMatchNameView.setText(matchName.getText());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mMatchNameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mMatchNameView.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mMatchNameView.setPadding(8,8,8,8);
                                    mWinningAmount.setText(winningAmount.getText());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mWinningAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mWinningAmount.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mWinningAmount.setPadding(8,8,8,8);
                                    mContestSize.setText(contestSize.getText());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mContestSize.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mContestSize.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mContestSize.setPadding(8,8,8,8);
                                    mEntryFee.setText(entryFee.getText());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mEntryFee.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mEntryFee.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mEntryFee.setPadding(8,8,8,8);
                                    mContestCode.setText(contestCode.getText());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mContestCode.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mContestCode.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }

                                    mContestCode.setPadding(8,8,8,8);

                                    imageView.setPadding(8,8,8,8);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        imageView.setBackground(getResources().getDrawable(R.drawable.data__cell));
                                    }

                                    multiTeam = "No";
                                    if(mSwitch.isChecked()){
                                        multiTeam = "Yes";
                                    }
                                    mMultipleTeams.setText(multiTeam);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mMultipleTeams.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mMultipleTeams.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mMultipleTeams.setPadding(8,8,8,8);


                                    ViewTreeObserver vto = imageView.getViewTreeObserver();
                                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                        public boolean onPreDraw() {
                                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                                            final float scale = getResources().getDisplayMetrics().density;
                                            //   int pixels = (int) (imageView.getMeasuredHeight() * scale + 0.5f);
                                            int pixels = imageView.getMeasuredHeight();
                                            mEntryFee.setHeight(pixels);
                                            mMatchNameView.setHeight(pixels);
                                            mMultipleTeams.setHeight(pixels);
                                            mContestCode.setHeight(pixels);
                                            mContestSize.setHeight(pixels);
                                            mWinningAmount.setHeight(pixels);
                                            return true;
                                        }
                                    });



                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("label", mContestCode.getText());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(LeaugeCode.this,"Contest code copied to clipboard!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    row.addView(mMatchNameView);
                                    row.addView(mContestCode);
                                    row.addView(mMultipleTeams);
                                    row.addView(mContestSize);
                                    row.addView(mWinningAmount);
                                    row.addView(mEntryFee);
                                    row.addView(imageView);
                                    mTableLayout.addView(row);



                                    final RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("name",matchName.getText().toString())
                                            .addFormDataPart("winningamount",winningAmount.getText().toString())
                                            .addFormDataPart("contest_size",contestSize.getText().toString())
                                            .addFormDataPart("contest_code",contestCode.getText().toString())
                                            .addFormDataPart("entry_fee",entryFee.getText().toString())
                                            .addFormDataPart("multiple_teams",multiTeam)
                                         //   .addFormDataPart("date",date.getText().toString())
                                            .addFormDataPart("winner",winner.getText().toString())

                                            .build();
                                    Request request = new Request.Builder()
                                            .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L2xlYXVnZV9jb2RlLw==",Base64.DEFAULT)))
                                            .post(requestBody)
                                            .build();
                                    OkHttpClient client = new OkHttpClient();
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LeaugeCode.this,"Contest Code adding failed",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (!response.isSuccessful()) {
                                                throw new IOException("Unexpected code " + response.body().string());
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(LeaugeCode.this,"Contest Code Added",Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }
                                        }
                                    });

                                    mTableLayout.setVisibility(View.VISIBLE);
                                    mEmptyText.setVisibility(View.INVISIBLE);
                                    dialog.cancel();
                                }else{
                                    if(!(contestCode.getText().toString().startsWith("1QX") && contestCode.getText().toString()
                                    .length()>=13)){
                                        Toast.makeText(LeaugeCode.this,"Please enter valid contest code",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LeaugeCode.this,"Enter all the information",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                    }
                });


                mDialog.show();
            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getData();
    }

    private void getData(){
        OkHttpClient client = new OkHttpClient();

        mProgressBar.setVisibility(View.VISIBLE);
        Request request = new Request.Builder()
                .url(new String(Base64.decode("aHR0cDovL3NpeWEueHl6L2xlYXVnZV9jb2RlLw==",Base64.DEFAULT)))
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

                        if(jsonArray.length()<=0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mEmptyText.setVisibility(View.VISIBLE);
                                    mTableLayout.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView mHeading = new TextView(LeaugeCode.this);
                                mHeading.setText("Contest Size - Entry Fee - Winning Amount - Winner - Multiple Team");
                                mHeading.setTextColor(Color.BLACK);
                                mHeading.setTypeface(null, Typeface.BOLD);
                                mLinearLayout.addView(mHeading);
                                mLinearLayout.addView(new TextView(LeaugeCode.this));

                            }
                        });
                        ArrayList<LeaugeCodeModel> mListOfLeaugeCode = new ArrayList<>();
                     //   for (int i = jsonA.length()-1; i >= 0; i--) {
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject leaugeCodeObj = jsonArray.getJSONObject(i);
                            final String id = leaugeCodeObj.getString("id");
                            final String matchname = leaugeCodeObj.getString("name");
                            final String winningamount = leaugeCodeObj.getString("winningamount");
                            final String contestsize = leaugeCodeObj.getString("contest_size");
                            final String entryfee = leaugeCodeObj.getString("entry_fee");
                            final String contestcode = leaugeCodeObj.getString("contest_code");
                            final String mulitpleteams = leaugeCodeObj.getString("multiple_teams");
                       //     final String date = leaugeCodeObj.getString("date");
                            final int winner = leaugeCodeObj.optInt("winner",0);
                            LeaugeCodeModel leaugeCodeModel = new LeaugeCodeModel();
                            leaugeCodeModel.setId(id);
                            leaugeCodeModel.setMatchName(matchname);
                            leaugeCodeModel.setWinningAmount(winningamount);
                            leaugeCodeModel.setEntryFee(entryfee);
                            leaugeCodeModel.setContestCode(contestcode);
                            leaugeCodeModel.setWinner(winner);
                            leaugeCodeModel.setContestSize(contestsize);
                            leaugeCodeModel.setMultipleteams(mulitpleteams);
                            mListOfLeaugeCode.add(leaugeCodeModel);

                        }

                        Collections.sort(mListOfLeaugeCode, new Comparator<LeaugeCodeModel>(){
                            public int compare(LeaugeCodeModel obj1, LeaugeCodeModel obj2) {
                                // ## Ascending order
                                return obj2.getId().compareToIgnoreCase(obj1.getId()); // To compare string values
                                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                                // ## Descending order
                                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                            }
                        });

                           for(final LeaugeCodeModel leaugeCodeModel: mListOfLeaugeCode){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final String id = leaugeCodeModel.getId();
                                    final String matchname = leaugeCodeModel.getMatchName();
                                    final String winningamount = leaugeCodeModel.getWinningAmount();
                                    final String contestsize = leaugeCodeModel.getContestSize();
                                    final String entryfee = leaugeCodeModel.getEntryFee();
                                    final String contestcode = leaugeCodeModel.getContestCode();
                                    final String mulitpleteams = leaugeCodeModel.getMultipleteams();
                                    //     final String date = leaugeCodeObj.getString("date");
                                    final int winner = leaugeCodeModel.getWinner();

                                    TextView matchnametv = new TextView(LeaugeCode.this);
                                    matchnametv.setText(matchname);
                                    matchnametv.setGravity(Gravity.CENTER);
                                    matchnametv.setTypeface(null, Typeface.BOLD);
                                    //  TextView datetv = new TextView(LeaugeCode.this);
                                    //   datetv.setText(date);

                                    //   datetv.setGravity(Gravity.CENTER);
                                    //   datetv.setTypeface(null, Typeface.BOLD);

                                    TextView lasttv = new TextView(LeaugeCode.this);
                                    lasttv.setText(contestsize +"-"+entryfee+"-"+winningamount+"-"+winner+"-"+mulitpleteams.substring(0,1));
                                    lasttv.setTypeface(null, Typeface.BOLD);

                                    lasttv.setGravity(Gravity.CENTER);
                                    TextView codetv = new TextView(LeaugeCode.this);

                                    codetv.setGravity(Gravity.CENTER);
                                    codetv.setText(contestcode);
                                    codetv.setTextColor(Color.RED);
                                    TextView copytv = new TextView(LeaugeCode.this);
                                    copytv.setText("Copy Code");

                                    copytv.setGravity(Gravity.CENTER);

                                    copytv.setTextColor(Color.BLUE);

                                    copytv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("label", contestcode);
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(LeaugeCode.this,"Contest code copied to clipboard!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    mLinearLayout.addView(matchnametv);
                                    //     mLinearLayout.addView(datetv);
                                    mLinearLayout.addView(lasttv);
                                    mLinearLayout.addView(codetv);
                                    mLinearLayout.addView(copytv);
                                    mLinearLayout.addView(new TextView(LeaugeCode.this));

                                    TableRow row = new TableRow(LeaugeCode.this);
                                    final TextView mMatchNameView = new TextView(LeaugeCode.this);
                                    final TextView mWinningAmount = new TextView(LeaugeCode.this);
                                    final TextView mMultipleTeams = new TextView(LeaugeCode.this);
                                    final TextView mContestSize = new TextView(LeaugeCode.this);
                                    final TextView mEntryFee = new TextView(LeaugeCode.this);
                                    final TextView mContestCode = new TextView(LeaugeCode.this);
                                    final TextView mCopyImage = new TextView(LeaugeCode.this);
                                    final ImageView imageView = new ImageView(LeaugeCode.this);
                                    // imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_copy));
                                    mMatchNameView.setText(matchname);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mMatchNameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mMatchNameView.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mMatchNameView.setPadding(8,8,8,8);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mCopyImage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mCopyImage.setBackground(getResources().getDrawable(R.drawable.ic_copy));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            mCopyImage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_copy,0,0,0);

                                        }

                                    }
                                    mCopyImage.setPadding(8,8,8,8);
                                    mWinningAmount.setText(winningamount);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mWinningAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mWinningAmount.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mWinningAmount.setPadding(8,8,8,8);
                                    mContestSize.setText(contestsize);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mContestSize.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mContestSize.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mContestSize.setPadding(8,8,8,8);
                                    mMultipleTeams.setText(mulitpleteams);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mMultipleTeams.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mMultipleTeams.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mMultipleTeams.setPadding(8,8,8,8);

                                    mEntryFee.setText(entryfee);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mEntryFee.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mEntryFee.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mEntryFee.setPadding(8,8,8,8);
                                    mContestCode.setText(contestcode);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        mContestCode.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        mContestCode.setBackground(getResources().getDrawable(R.drawable.data__cell));

                                    }
                                    mContestCode.setPadding(8,8,8,8);

                                    imageView.setPadding(8,8,8,8);


                                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(74,49);

                                    imageView.requestLayout();
                                    imageView.setAdjustViewBounds(true);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        imageView.setBackground(getResources().getDrawable(R.drawable.data__cell));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            imageView.setImageDrawable(getDrawable(R.drawable.ic_copy));
                                        }
                                    }
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setLayoutParams(layoutParams);
                                    ViewTreeObserver vto = imageView.getViewTreeObserver();
                                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                        public boolean onPreDraw() {
                                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                                            final float scale = getResources().getDisplayMetrics().density;
                                            //   int pixels = (int) (imageView.getMeasuredHeight() * scale + 0.5f);
                                            int pixels = imageView.getMeasuredHeight();
                                            mEntryFee.setHeight(convertDipToPixels(49));
                                            mMatchNameView.setHeight(convertDipToPixels(49));
                                            mContestCode.setHeight(convertDipToPixels(49));
                                            mContestSize.setHeight(convertDipToPixels(49));
                                            mCopyImage.setHeight(convertDipToPixels(49));
                                            mMultipleTeams.setHeight(convertDipToPixels(49));
                                            mWinningAmount.setHeight(convertDipToPixels(49));
                                            mContestCode.setWidth(convertDipToPixels(74));
                                            mMatchNameView.setWidth(convertDipToPixels(74));
                                            mContestCode.setWidth(convertDipToPixels(74));
                                            mContestSize.setWidth(convertDipToPixels(74));
                                            mMultipleTeams.setWidth(convertDipToPixels(74));
                                            mWinningAmount.setWidth(convertDipToPixels(74));
                                            mCopyImage.setWidth(convertDipToPixels(74));

                                            return true;
                                        }
                                    });




                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("label", mContestCode.getText());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(LeaugeCode.this,"Contest code copied to clipboard!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    row.addView(mMatchNameView);
                                    row.addView(mContestCode);
                                    row.addView(mMultipleTeams);
                                    row.addView(mContestSize);
                                    row.addView(mWinningAmount);
                                    row.addView(mEntryFee);
                                    row.addView(imageView);
                                    row.addView(mCopyImage);
                                    mTableLayout.addView(row);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }});
    }

    public int convertDipToPixels(float dips)
    {
        return (int) (dips * getResources().getDisplayMetrics().density + 0.5f);
    }
}
