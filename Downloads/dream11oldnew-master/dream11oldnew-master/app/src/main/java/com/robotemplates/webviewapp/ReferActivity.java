package com.robotemplates.webviewapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReferActivity extends AppCompatActivity {

    private String phone = "";
    private ProgressBar mProgressBar;
    private ConstraintLayout mMainLayout;

    public static final int RC_SIGN_IN = 0;// 0 = Yes, 1 = No
    private static final int REQUEST_INVITE = 52;
    private Button mWithdrawl,mEditNumber,mRefer,mSignIn,mCopy;
    private TextView mBalanceTitle,mBalance,mPaytmNumberLabel,mPaytmNumber,mSignInText;
    private Uri mInvitationUrl;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);


        Button info = findViewById(R.id.mInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReferActivity.this);
                builder.setTitle("Rules and Info");
                builder.setMessage(
                        "\n 1) If you want fast payment then as soon as you withdraw, follow these steps \n" +
                        "\n           a) Go to the app's main screen \n" +
                        "\n           b) Click on the three line icon at the top-left corner of the screen \n" +
                        "\n           c) Click on the Contact button \n" +
                        "\n           d) Mail us your Paytm Number \n" +
                        "\n Thank You and Enjoy Earning Money \n");
                AlertDialog dialog = builder.create();
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.cricket);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       mSignIn = findViewById(R.id.mSignIn);
        mWithdrawl = findViewById(R.id.withdrawlBtn);
        mBalanceTitle = findViewById(R.id.balanceTitle);
        setFinishOnTouchOutside(false);
        mBalance = findViewById(R.id.balance);
        mMainLayout = findViewById(R.id.mMainLayout);
        mProgressBar = findViewById(R.id.mProgressBar);
        mSignInText = findViewById(R.id.mSignInText);
        mEditNumber = findViewById(R.id.mEditNumber);
        mPaytmNumber = findViewById(R.id.mPaytmNumber);
        mPaytmNumberLabel = findViewById(R.id.mPaytmNumberLabel);
        mRefer = findViewById(R.id.mShareReferral);
        mCopy = findViewById(R.id.mCopyReferral);
        mWithdrawl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                User mUser = task.getResult().toObject(User.class);
                                if(mUser!=null){
                                    mUser.setWithdraw(true);
                                    if(mUser.getmRefferals()>0){
                                        mWithdrawl.setEnabled(false);
                                        mWithdrawl.setText("Withdrawl Under Process");
                                        FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getUid()).set(mUser);

                                    }else{
                                        Toast.makeText(ReferActivity.this,"You must have balance of atleast ₹ 5",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(ReferActivity.this,"You must have balance of atleast ₹ 5",Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });
                }

            }
        });

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            mMainLayout.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult() != null){

                            User user = task.getResult().toObject(User.class);
                            if(user!=null){
                                if(user.isWithdraw()){
                                    mWithdrawl.setEnabled(false);
                                    mWithdrawl.setText("Withdrawl Under Process");
                                }
                                int balanceStr = user.getmRefferals();
                                int balance = balanceStr * 5;
                                String balanceTXT = "₹ "+balance;
                                mBalance.setText(balanceTXT);
                            }
                        }
                    }
                }
            });
        }

        mRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onInvitationClick();
            }
        });
        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCopyLink();
            }
        });
        final FirebaseAuth auth = FirebaseAuth.getInstance();



        mEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReferActivity.this);
                builder.setTitle("Enter your paytm nummber");
                final EditText paytmNumber = new EditText(ReferActivity.this);
                builder.setView(paytmNumber);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phone = paytmNumber.getText().toString();
                        firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                User mUser = task.getResult().toObject(User.class);
                                if(mUser!=null){
                                    mUser.setmPhone(phone);
                                    firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).set(mUser);

                                }else{
                                    mUser = new User();
                                    mUser.setmPhone(phone);
                                    firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).set(mUser);

                                }

                                firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        User mUser = task.getResult().toObject(User.class);
                                        if(mUser!=null){

                                            if(mUser.getmPhone()!=null && mUser.getmPhone().length()>0){

                                                mPaytmNumber.setText(mUser.getmPhone());
                                            }
                                        }else{

                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        if(auth.getCurrentUser()==null){
            mSignIn.setVisibility(View.VISIBLE);
           mSignInText.setVisibility(View.VISIBLE);
            mWithdrawl.setVisibility(View.INVISIBLE);
            mBalanceTitle.setVisibility(View.INVISIBLE);

            mBalance.setVisibility(View.INVISIBLE);
            mPaytmNumberLabel.setVisibility(View.INVISIBLE);
            mPaytmNumber.setVisibility(View.INVISIBLE);
            mEditNumber.setVisibility(View.INVISIBLE);
            mWithdrawl.setVisibility(View.INVISIBLE);
            mRefer.setVisibility(View.INVISIBLE);
            mCopy.setVisibility(View.INVISIBLE);
            mSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                       //     new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.FacebookBuilder().build()
                            )
                            ;


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            });

        }else{

                mMainLayout.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User mUser = task.getResult().toObject(User.class);
                    if(mUser!=null){

                        if(mUser.getmPhone()!=null){
                            int balanceStr = mUser.getmRefferals();
                            int balance = balanceStr * 5;
                            String balanceTXT = "₹ "+balance;
                            mBalance.setText(balanceTXT);
                            mPaytmNumber.setText(mUser.getmPhone());


                            mMainLayout.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mBalance.setVisibility(View.VISIBLE);
                            mBalanceTitle.setVisibility(View.VISIBLE);
                            mWithdrawl.setVisibility(View.VISIBLE);
                            mSignIn.setVisibility(View.INVISIBLE);
                            mSignInText.setVisibility(View.INVISIBLE);
                            mPaytmNumberLabel.setVisibility(View.INVISIBLE);
                            mPaytmNumber.setVisibility(View.INVISIBLE);
                            mEditNumber.setVisibility(View.INVISIBLE);
                            mRefer.setVisibility(View.VISIBLE);
                            mCopy.setVisibility(View.VISIBLE);
                        }
                    }else{
                        mMainLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mBalance.setVisibility(View.VISIBLE);
                        mBalanceTitle.setVisibility(View.VISIBLE);
                        mWithdrawl.setVisibility(View.VISIBLE);
                        mSignIn.setVisibility(View.INVISIBLE);
                        mSignInText.setVisibility(View.INVISIBLE);
                        mPaytmNumberLabel.setVisibility(View.INVISIBLE);
                        mPaytmNumber.setVisibility(View.INVISIBLE);
                        mEditNumber.setVisibility(View.INVISIBLE);
                        mRefer.setVisibility(View.VISIBLE);
                        mCopy.setVisibility(View.VISIBLE);
                    }

                    mMainLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mBalance.setVisibility(View.VISIBLE);
                    mBalanceTitle.setVisibility(View.VISIBLE);
                    mWithdrawl.setVisibility(View.VISIBLE);
                    mSignIn.setVisibility(View.INVISIBLE);
                    mSignInText.setVisibility(View.INVISIBLE);
                    mPaytmNumberLabel.setVisibility(View.INVISIBLE);
                    mPaytmNumber.setVisibility(View.INVISIBLE);
                    mEditNumber.setVisibility(View.INVISIBLE);
                    mRefer.setVisibility(View.VISIBLE);
                    mCopy.setVisibility(View.VISIBLE);
                }
            });
        }

        /*mSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

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
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_INVITE) {
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
        }else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().exists()){
                                User user1 = new User();
                                user1.setName(user.getDisplayName());
                                user1.setEmailId(user.getEmail());
                                FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user1);

                            }
                        }else{
                            User user1 = new User();
                            user1.setName(user.getDisplayName());
                            user1.setEmailId(user.getEmail());
                            FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user1);

                        }

                    }
                });
           //     FirebaseFirestore.getInstance().collection("Refferers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(new User());

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Have you been referred by anyone?");
                final EditText mRefferCode = new EditText(this);
                builder.setView(mRefferCode);
                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User mUser = task.getResult().toObject(User.class);
                            if(mUser!=null){

                                if(mUser.isWithdraw()){
                                    mWithdrawl.setEnabled(false);
                                    mWithdrawl.setText("Under Process");
                                }
                                if(mUser.getmPhone()!=null && mUser.getmPhone().length()>0){

                                    int balanceStr = mUser.getmRefferals();
                                    int balance = balanceStr * 5;
                                    String balanceTXT = "₹ "+balance;
                                    mBalance.setText(balanceTXT);
                                    mPaytmNumber.setText(mUser.getmPhone());

                                }
                            }else{

                            }
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.v("dav","Dsfsfw" + mRefferCode.getText().toString() + " "+FirebaseAuth.getInstance().getUid());
                        if(!mRefferCode.getText().toString().equals("tfXMgctCPHRC160qxaDo8NBkvUn2")){
                            firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    User mUser = task.getResult().toObject(User.class);
                                    if(mUser!=null){
                                        if(mUser.isWithdraw()){
                                            mWithdrawl.setEnabled(false);
                                            mWithdrawl.setText("Under Process");
                                        }
                                        if(mUser.getmPhone()!=null && mUser.getmPhone().length()>0){

                                            mPaytmNumber.setText(mUser.getmPhone());
                                        }
                                    }else{

                                    }
                                }
                            });
                        }else{
                            Toast.makeText(ReferActivity.this,"You can't refer yourself",Toast.LENGTH_SHORT).show();
                        }
                        if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid())) {
                            firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task != null){
                                        if(task.getResult() != null){
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
                                                                    user.setmId(FirebaseAuth.getInstance().getUid());
                                                                    ArrayList<String> mListOfRefferers = new ArrayList<>();
                                                                    if(user.getmListOfRefferers()!=null){

                                                                        if(!(user.getmListOfRefferers().contains(mRefferCode.getText().toString())) && !(user.getmListOfRefferers().size()>1) && !mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid())){
                                                                            if(user.getmListOfRefferers()!=null){
                                                                                mListOfRefferers = user.getmListOfRefferers();
                                                                                if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                    mListOfRefferers.add(mRefferCode.getText().toString());

                                                                            }else{
                                                                                if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                    mListOfRefferers.add(mRefferCode.getText().toString());
                                                                            }
                                                                            user.setmListOfRefferers(mListOfRefferers);
                                                                            if (user.getmRefferals()!=0) {
                                                                                int referrals = user.getmRefferals();
                                                                                Log.d("YUVA"," "+referrals);

                                                                                referrals++;


                                                                                Log.d("YUVA"," "+referrals);
                                                                                user.setmRefferals(referrals);
                                                                                if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                    firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                                //firestore.collection("Users").document(mRefferCode.getText().toString()).set(user);
                                                                            }else{
                                                                                user.setmRefferals(1);
                                                                                if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                    firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                            }


                                                                            firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if(task != null){
                                                                                        if(task.getResult() != null){
                                                                                            User user = task.getResult().toObject(User.class);
                                                                                            if(user!=null){

                                                                                            /*    int balanceStr = user.getmRefferals();
                                                                                                int balance = balanceStr * 5;
                                                                                                String balanceTXT = "₹ "+balance;
                                                                                                mBalance.setText(balanceTXT);*/
                                                                                            }else{
                                                                                                user.setmRefferals(user.getmRefferals()+1);
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
                                                                    }else {
                                                                        firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                User mUser = task.getResult().toObject(User.class);
                                                                                if(mUser!=null){

                                                                                    if(mUser.isWithdraw()){
                                                                                        mWithdrawl.setEnabled(false);
                                                                                        mWithdrawl.setText("Under Process");
                                                                                    }
                                                                                    if(mUser.getmPhone()!=null && mUser.getmPhone().length()>0){


                                                                                        mPaytmNumber.setText(mUser.getmPhone());
                                                                                    }
                                                                                }else{

                                                                                }
                                                                            }
                                                                        });
                                                                        if(user.getmListOfRefferers()!=null){
                                                                            mListOfRefferers = user.getmListOfRefferers();
                                                                            if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                mListOfRefferers.add(mRefferCode.getText().toString());

                                                                        }else{
                                                                            if(!mRefferCode.getText().toString().equals(FirebaseAuth.getInstance().getUid()))
                                                                                mListOfRefferers.add(mRefferCode.getText().toString());
                                                                        }
                                                                        user.setmListOfRefferers(mListOfRefferers);
                                                                        if (user.getmRefferals()!= 0) {
                                                                            int referrals = user.getmRefferals();
                                                                            Log.d("YUVA"," "+referrals);

                                                                            referrals++;


                                                                            Log.d("YUVA"," "+referrals);
                                                                            user.setmRefferals(referrals);

                                                                            firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                        }else{
                                                                            user.setmRefferals(1);
                                                                            firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(user);
                                                                        }

                                                                        firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if(task != null){
                                                                                    if(task.getResult() != null){
                                                                                        User user = task.getResult().toObject(User.class);
                                                                                        if(user!=null){

                                                                                            user.setmRefferals(user.getmRefferals()+1);
                                                                                          /*  int balanceStr = user.getmRefferals();
                                                                                            int balance = balanceStr * 5;
                                                                                            String balanceTXT = "₹ "+balance;
                                                                                            mBalance.setText(balanceTXT);*/
                                                                                            firestore.collection("Refferers").document(mRefferCode.getText().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                }
                                                                                            });
                                                                                        }else{
                                                                                           /* user = new User();
                                                                                            user.setmRefferals(user.getmRefferals()+1);*/
                                                                                            firestore.collection("Refferers").document(mRefferCode.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    if(task != null){
                                                                                                        if(task.getResult() != null){
                                                                                                            User user = task.getResult().toObject(User.class);
                                                                                                            if(user!=null){

                                                                                                           /*     int balanceStr = user.getmRefferals();
                                                                                                                int balance = balanceStr * 5;
                                                                                                                String balanceTXT = "₹ "+balance;
                                                                                                                mBalance.setText(balanceTXT);*/
                                                                                                            }else{
                                                                                                                user = new User();
                                                                                                                user.setmRefferals(user.getmRefferals()+1);
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
                        }else{
                            Toast.makeText(ReferActivity.this,"You can't refer yourself",Toast.LENGTH_SHORT).show();
                        }



                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                try{
                    firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult()!=null){
                                User user = task.getResult().toObject(User.class);
                                if(user!= null){
                                    if (user.getmListOfRefferers()!=null){

                                        if(user.getmListOfRefferers().size()==0){
                                            // dialog.show();
                                            mMainLayout.setVisibility(View.INVISIBLE);
                                            mProgressBar.setVisibility(View.VISIBLE);
                                            firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task != null){
                                                        if(task.getResult() != null){

                                                            User user = task.getResult().toObject(User.class);
                                                            if(user!=null){
                                                                if(user.isWithdraw()){
                                                                    mWithdrawl.setEnabled(false);
                                                                    mWithdrawl.setText("Withdrawl Under Process");
                                                                }
                                                                int balanceStr = user.getmRefferals();
                                                                int balance = balanceStr * 5;
                                                                String balanceTXT = "₹ "+balance;
                                                                mBalance.setText(balanceTXT);

                                                                mMainLayout.setVisibility(View.VISIBLE);
                                                                mProgressBar.setVisibility(View.INVISIBLE);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }else{
                                    //      dialog.show();
                                    mMainLayout.setVisibility(View.INVISIBLE);
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    firestore.collection("Refferers").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task != null){
                                                if(task.getResult() != null){

                                                    User user = task.getResult().toObject(User.class);
                                                    if(user!=null){
                                                        if(user.isWithdraw()){
                                                            mWithdrawl.setEnabled(false);
                                                            mWithdrawl.setText("Withdrawl Under Process");
                                                        }
                                                        int balanceStr = user.getmRefferals();
                                                        int balance = balanceStr * 5;
                                                        String balanceTXT = "₹ "+balance;
                                                        mBalance.setText(balanceTXT);

                                                        mMainLayout.setVisibility(View.VISIBLE);
                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            }else{
                                                mMainLayout.setVisibility(View.VISIBLE);
                                                mProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }

            }


                mBalance.setVisibility(View.VISIBLE);
                mBalanceTitle.setVisibility(View.VISIBLE);
                mWithdrawl.setVisibility(View.VISIBLE);
                mSignIn.setVisibility(View.INVISIBLE);
             mSignInText.setVisibility(View.INVISIBLE);
                mPaytmNumberLabel.setVisibility(View.INVISIBLE);
                mPaytmNumber.setVisibility(View.INVISIBLE);
                mEditNumber.setVisibility(View.INVISIBLE);

            mRefer.setVisibility(View.VISIBLE);

            mCopy.setVisibility(View.VISIBLE);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }


        private void onInvitationClick() {
        Log.d("YOYO","Invite Sending");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = FirebaseAuth.getInstance().getUid();
       // String link = "https://play.google.com/store/apps/details/?invitedby=" + uid;

        String link = new String(Base64.decode("aHR0cHM6Ly9zaXlhLnh5ei8/cmVmZXJyZXI9",Base64.DEFAULT)) + uid;

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("dream11prediction.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {

                     //   Log.d("YOYO","Invite Sending...." + task.getResult().getShortLink());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {


               /*         mInvitationUrl = shortDynamicLink.getShortLink();
                        Log.d("YOYO","Invite Send");
                        // ...
                       // String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        String subject = String.format("Do you want to win every Dream 11 Match?");
                        String invitationLink = mInvitationUrl.toString();
                        String msg = "Download Dream11 Expert Prediction Tips and News"
                                + invitationLink;
                        String msgHtml = String.format("<p>Download Dream11 Expert Prediction Tips and News using this link"
                                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                  //      intent.setData(Uri.parse("mailto:")); // only email apps should handle this

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                         //   startActivity(intent);
                        }
                      //  startActivity(intent);*/

                    }
                });

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://dream-11-dd231.firebaseapp.com?referrer="+FirebaseAuth.getInstance().getUid()))
                .setDomainUriPrefix("https://propredection.page.link")

                // Open links with this app on Android
                .setAndroidParameters(
                new DynamicLink.AndroidParameters.Builder("com.robotemplates.webviewapp")
                        .setMinimumVersion(125)
                        .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?referrer="+FirebaseAuth.getInstance().getUid()+"&id=com.propredection.fantasypro&hl=en?"))
                        .build())

                .buildDynamicLink();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink();
        shortLinkTask.addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
            @Override
            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                if (task.isSuccessful()) {

                    mInvitationUrl = task.getResult().getShortLink();
                    Log.d("YOYO","Invite Send");
                    // ...
                    // String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    String subject = String.format("Ignore this?");
                    String invitationLink = String.valueOf(mInvitationUrl);
                    String msg = "Download Dream11 Expert Prediction Tips and News "
                            +invitationLink;
                    String msgHtml = String.format("<p>Download Dream11 Expert Prediction Tips and News using this link<br>"
                            + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    //    intent.setData(Uri.parse("mailto:")); // only email apps should handle this


                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    Uri shortLink = task.getResult().getShortLink();
                    Log.d("YOYO", String.valueOf(shortLink));
                }
                else {

                    Log.d("YOYO","Not shared link");
                }
            }
        });
        Uri dynamicLinkUri = dynamicLink.getUri();





    }

    private void onCopyLink() {
        Log.d("YOYO","Invite Sending");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = FirebaseAuth.getInstance().getUid();
        // String link = "https://play.google.com/store/apps/details/?invitedby=" + uid;

        String link = "https://dream-11-dd231.firebaseapp.com?referrer="+FirebaseAuth.getInstance().getUid();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("dream11prediction.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder().setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?referrer="+FirebaseAuth.getInstance().getUid()+"&id=com.propredection.fantasypro&hl=en?"))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {

                        //   Log.d("YOYO","Invite Sending...." + task.getResult().getShortLink());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {


               /*         mInvitationUrl = shortDynamicLink.getShortLink();
                        Log.d("YOYO","Invite Send");
                        // ...
                       // String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        String subject = String.format("Do you want to win every Dream 11 Match?");
                        String invitationLink = mInvitationUrl.toString();
                        String msg = "Download Dream11 Expert Prediction Tips and News"
                                + invitationLink;
                        String msgHtml = String.format("<p>Download Dream11 Expert Prediction Tips and News using this link"
                                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                  //      intent.setData(Uri.parse("mailto:")); // only email apps should handle this

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                         //   startActivity(intent);
                        }
                      //  startActivity(intent);*/

                    }
                });


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
              //  .setLink(Uri.parse("https://dream-11-dd231.firebaseapp.com?referrer="+FirebaseAuth.getInstance().getUid()+"&id=com.propredection.fantasypro&hl=en?"))
                .setLink(Uri.parse("https://dream-11-dd231.firebaseapp.com?referrer="+FirebaseAuth.getInstance().getUid()))
                .setDomainUriPrefix("https://propredection.page.link")
                // Open links with this app on Android
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.proprediction.fantasypro")
                                .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?referrer="+FirebaseAuth.getInstance().getUid()+"&id=com.propredection.fantasypro&hl=en?"))
                                .setMinimumVersion(125)
                                .build())

                .buildDynamicLink();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink();
        shortLinkTask.addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
            @Override
            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                if (task.isSuccessful()) {

                    mInvitationUrl = task.getResult().getShortLink();
                    Log.d("YOYO","Invite Send");
                    // ...
                    // String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    String subject = String.format("Ignore this?");
                    String invitationLink = String.valueOf(mInvitationUrl);
                    String msg = "Download Dream11 Expert Prediction Tips and News "
                            +invitationLink;
                    String msgHtml = String.format("<p>Download Dream11 Expert Prediction Tips and News using this link<br>"
                            + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);


                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Refer code", String.valueOf(mInvitationUrl));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ReferActivity.this,"Refer link copied",Toast.LENGTH_SHORT).show();
                }
                else {

                    Log.d("YOYO","Not shared link");
                }
            }
        });
        Uri dynamicLinkUri = dynamicLink.getUri();





    }
}
