package com.robotemplates.webviewapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class TabBaseFragment extends Fragment implements RecyclerViewAdapter.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String MY_PREFS_NAME = "MYLOGIN";
    private  ArrayList<PaidSport> mListOfPaidSport;
    private TextView mNTV;
    private InterstitialAd mInterstitialAd;
    private String bank="Details";
    private TextView detaillbl;

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    public boolean loginpage=true;
    public boolean paid=false;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Team> mListOfTeam = new ArrayList<>();
    private ArrayList<PaidUser> mListOfUser = new ArrayList<>();
    private ArrayList<Sport> mListOfSport = new ArrayList<>();
    private int mSport;
    private RecyclerView mRecyclerView;
    public boolean premium=false;
    private ArrayList<Posts> mListOfPost = new ArrayList<>();
    private String mParam2;
    private Context mcontext;

    private OnFragmentInteractionListener mListener;
    private Intent intent;
    PaidInterface paidInterface;


    public TabBaseFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @SuppressLint("ValidFragment")
    public TabBaseFragment(ArrayList<Posts> mList, ArrayList<Team> mListT, int sport,Context context,boolean ispaid,ArrayList<PaidUser> pu,PaidInterface pi,ArrayList<PaidSport> ps,String mb,ArrayList<Sport> s) {
        // Required empty public constructor
        mListOfPost = mList;
        mListOfTeam = mListT;
        mSport = sport;
        mcontext = context;
        paid = ispaid;
        mListOfUser = pu;
        paidInterface = pi;
        mListOfPaidSport = ps;
        bank = mb;
        mListOfSport = s;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment TabBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabBaseFragment newInstance(ArrayList<Posts> mList, ArrayList<Team> mlt, int sport, Context context, boolean ppaid, ArrayList<PaidUser> paidUsers, PaidInterface pi, ArrayList<PaidSport> paidSports, String bank, ArrayList<Sport> s) {
        TabBaseFragment fragment = new TabBaseFragment(mList,mlt,sport,context,ppaid,paidUsers,pi,paidSports,bank,s);

      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mcontext!=null){
            mInterstitialAd = new InterstitialAd(mcontext);
            mInterstitialAd.setAdUnitId("ca-app-pub-8920798231622156/5239993552");
            //  mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*try{
            SharedPreferences prefs = mcontext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString(String.valueOf(mSport), null);
            if (restoredText != null) {
                for(PaidUser paidUser : mListOfUser){
                    Date currentTime = Calendar.getInstance().getTime();
                    Date expDate = null;
                    try {
                        expDate = new SimpleDateFormat("yyyy-MM-dd").parse(paidUser.getStartdate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Calendar sDate = Calendar.getInstance();
                    Calendar eDate = Calendar.getInstance();
                    eDate.setTime(currentTime);
                    sDate.setTime(expDate);
                    int difInMonths = sDate.get(Calendar.MONTH) - eDate.get(Calendar.MONTH);
                     int diffInDays = (int) ((expDate.getTime() - currentTime.getTime()) / (1000 * 60 * 60 * 24));

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY,0);
                    c.set(Calendar.MINUTE,0);
                    c.set(Calendar.SECOND,0);
                    c.set(Calendar.MILLISECOND,0);

                    int todaymon = c.get(Calendar.MONTH);
                    Date today = c.getTime();

             //       long todayinmillis = c.getTimeInMillis();
                 //   Calendar cd = Calendar.getInstance();
               //   cd.setTime(expDate);
                  int y = cd.get(Calendar.YEAR);
                  int m = cd.get(Calendar.MONTH);
                  int d = cd.get(Calendar.DAY_OF_MONTH);
                  c.set(Calendar.MONTH,m);
                  c.set(Calendar.YEAR,y);
                  c.set(Calendar.DAY_OF_MONTH,d);
                  Date expinmilli = c.getTime();
                  int diffmonth = getMonthInt(expDate)-getMonthInt(currentTime);

                ///    Toast.makeText(mcontext, "Difff "+expDate.toString(), Toast.LENGTH_LONG).show();



                    if(diffmonth>0) {
                        if(paidUser.getPassword().equals(restoredText)){
                            loginpage = false;
                            premium = true;
                            View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
                            mRecyclerView = view.findViewById(R.id.mRecyclerView);
                            mNTV = view.findViewById(R.id.mEmptyTXT);
                            mNTV.setVisibility(View.INVISIBLE);
                            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mListOfPost,mListOfTeam,this,getContext());
                            myAdapter.setSport(mSport);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            mRecyclerView.setAdapter(myAdapter);
                            mRecyclerView.setLayoutManager(linearLayoutManager);

                            mRecyclerView.setHasFixedSize(true);
                            myAdapter.notifyDataSetChanged();


                            if(mListOfPost==null || mListOfPost.size() ==0){
                                mNTV.setVisibility(View.VISIBLE);
                            }

                            paidInterface.savedLogin(mSport);
                            return view;
                        }
                    }
                      else if(today.before(expinmilli))
                    {
                        if(paidUser.getPassword().equals(restoredText)){

                            loginpage = false;
                            premium = true;
                            View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
                            mRecyclerView = view.findViewById(R.id.mRecyclerView);
                            mNTV = view.findViewById(R.id.mEmptyTXT);
                            mNTV.setVisibility(View.INVISIBLE);
                            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mListOfPost,mListOfTeam,this,getContext());
                            myAdapter.setSport(mSport);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            mRecyclerView.setAdapter(myAdapter);
                            mRecyclerView.setLayoutManager(linearLayoutManager);

                            mRecyclerView.setHasFixedSize(true);
                            myAdapter.notifyDataSetChanged();


                            if(mListOfPost==null || mListOfPost.size() ==0){
                                mNTV.setVisibility(View.VISIBLE);
                            }

                            paidInterface.savedLogin(mSport);
                            return view;
                        }
                    }

                }
            }
        }catch (Exception e){

        }*/

        /*if(!paid){
            loginpage = false;
            View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
            mRecyclerView = view.findViewById(R.id.mRecyclerView);
            mNTV = view.findViewById(R.id.mEmptyTXT);
            mNTV.setVisibility(View.INVISIBLE);
            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mListOfPost,mListOfTeam,this,getContext());
            myAdapter.setSport(mSport);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setAdapter(myAdapter);
            mRecyclerView.setLayoutManager(linearLayoutManager);

            mRecyclerView.setHasFixedSize(true);

            myAdapter.notifyDataSetChanged();


            if(mListOfPost==null || mListOfPost.size() ==0){
                mNTV.setVisibility(View.VISIBLE);
            }
            return view;
        }else{

            View view = inflater.inflate(R.layout.premium, container, false);
            premium = true;
            EditText email,password;
            detaillbl = view.findViewById(R.id.details);
            detaillbl.setText(bank);
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            Button login = view.findViewById(R.id.login);
            Button winning = view.findViewById(R.id.winningProof);
            winning.setOnClickListener( v -> {
                String data = "";
                for(PaidSport ps : mListOfPaidSport){
                    if(ps.getSportid()==mSport){
                        data = ps.getTestemonials();
                    }
                }

                if(!data.equals("")){
                    Intent intent = new Intent(mcontext,PostActivity.class);
                    intent.putExtra("content",data);
                    intent.putExtra("title","");
                    intent.putExtra("date","11-11-1111");
                    if(premium){

                        intent.putExtra("DOMAIN","139.59.86.238");
                    }
                    startActivity(intent);
                }
            });

            login.setOnClickListener(v -> {
                boolean haslogin=false;
                if(email.getText().toString().length()>0 && password.getText().toString().length()>0){
                    if(mListOfUser.size()>0){
                        for(PaidUser paidUser : mListOfUser){
                            if(paidUser.getEmail().equals(email.getText().toString())){
                                if(paidUser.getPassword().equals(password.getText().toString())){


                                    for(Sport sport:mListOfSport){
                                        if(sport.getId()==mSport){
                                            if(paidUser.getPaidsportname().contains(sport.getName())){
                                                Date currentTime = Calendar.getInstance().getTime();
                                                Date expDate = null;
                                                try {
                                                    expDate = new SimpleDateFormat("yyyy-MM-dd").parse(paidUser.getStartdate());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Calendar sDate = Calendar.getInstance();
                                                Calendar eDate = Calendar.getInstance();
                                                sDate.setTime(currentTime);
                                                eDate.setTime(expDate);
                                                int difInMonths = sDate.get(Calendar.MONTH) - eDate.get(Calendar.MONTH);

                                                if(difInMonths>0){
                                                    loginpage = false;
                                                    haslogin = true;
                                                    //   premium = true;
                                                    //   onCreate(null);
                                                    //   paidInterface.onLogin(mSport);

                                                    //  loginpage = true;

                                                    SharedPreferences.Editor editor = mcontext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                    editor.putString(String.valueOf(mSport), password.getText().toString());
                                                    editor.apply();
                                                    Toast.makeText(mcontext, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent i = mcontext.getPackageManager()
                                                            .getLaunchIntentForPackage(mcontext.getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    ActivityCompat.finishAfterTransition((Activity) mcontext);

                                                }else if(currentTime.before(expDate) && expDate.after(currentTime)){
                                                    //     loginpage = false;
                                                    haslogin = true;
                                                    //    premium = true;
                                                    //   onCreate(null);
                                                    //   paidInterface.onLogin(mSport);
                                                    //     loginpage = true;
                                                    SharedPreferences.Editor editor = mcontext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                    editor.putString(String.valueOf(mSport), password.getText().toString());
                                                    editor.apply();
                                                    Toast.makeText(mcontext, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent i = mcontext.getPackageManager()
                                                            .getLaunchIntentForPackage(mcontext.getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    ActivityCompat.finishAfterTransition((Activity) mcontext);

                                                }else{
                                                    Toast.makeText(mcontext, "Account Validity Expired", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }
                                    }




                                }else{
                               //     Toast.makeText(mcontext, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                             //   Toast.makeText(mcontext, "Wrong Username", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(!haslogin){
                            Toast.makeText(mcontext, "User Does Not Exists", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                       Toast.makeText(mcontext, "Empty User List", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(mcontext, "Username or Password is invalid", Toast.LENGTH_SHORT).show();
                }
            });




            return view;

        }*/
        // Inflate the layout for this fragment
        loginpage = false;
        View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mNTV = view.findViewById(R.id.mEmptyTXT);
        mNTV.setVisibility(View.INVISIBLE);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mListOfPost,mListOfTeam,this,getContext());
        myAdapter.setSport(mSport);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        myAdapter.notifyDataSetChanged();


        if(mListOfPost==null || mListOfPost.size() ==0){
            mNTV.setVisibility(View.VISIBLE);
        }
        return view;
    }
    public static int getMonthInt(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mInterstitialAd!=null){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(final int pos) {

        if(mInterstitialAd!=null){
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    //    if (mInterstitialAd.isLoaded())
                    //      mInterstitialAd.show();
                    //    if (mInterstitialAd.isLoaded())
                    //     mInterstitialAd.show();
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
              /*  intent = new Intent(mcontext,PostActivity.class);
                intent.putExtra("title",mListOfPost.get(pos).getTitle());
                intent.putExtra("date",mListOfPost.get(pos).getDate());
                intent.putExtra("content",mListOfPost.get(pos).getContent());
                mcontext.startActivity(intent);*/
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.

                }

                @Override
                public void onAdLeftApplication() {

                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    intent = new Intent(mcontext,PostActivity.class);
                    intent.putExtra("title",mListOfPost.get(pos).getTitle());
                    intent.putExtra("date",mListOfPost.get(pos).getDate());
                    intent.putExtra("content",mListOfPost.get(pos).getContent());



                    mcontext.startActivity(intent);
                    // Code to be executed when when the interstitial ad is closed.
                }
            });

            if(mInterstitialAd.isLoaded()){

                mInterstitialAd.show();
            }else{
                intent = new Intent(mcontext,PostActivity.class);
                intent.putExtra("title",mListOfPost.get(pos).getTitle());
                intent.putExtra("date",mListOfPost.get(pos).getDate());
                intent.putExtra("content",mListOfPost.get(pos).getContent());

                mcontext.startActivity(intent);
            }
        }else{
            intent = new Intent(mcontext,PostActivity.class);
            intent.putExtra("title",mListOfPost.get(pos).getTitle());
            intent.putExtra("date",mListOfPost.get(pos).getDate());
            intent.putExtra("content",mListOfPost.get(pos).getContent());

            mcontext.startActivity(intent);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
