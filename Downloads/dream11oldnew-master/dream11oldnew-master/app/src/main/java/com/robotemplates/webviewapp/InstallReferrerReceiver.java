package com.robotemplates.webviewapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final int REQUEST_READ_PHONE_STATE = 178;
    Context mc;

    public void onReceive(Context context, Intent intent) {
        this.mc = context;

        if (intent != null) {
            String str = intent.getExtras().getString("referrer");
            Intent intent2 = new Intent();
            intent2.setClassName(context.getPackageName(), MainActivity.class.getName());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://test?referrer=");
            stringBuilder.append(str);
            intent2.setData(Uri.parse(stringBuilder.toString()));
            intent2.setFlags(268435456);
            context.startActivity(intent2);
        }
    }

    private void createAnonymousAccountWithReferrerInfo(final String str) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mc.getSystemService("phone");
        if (ActivityCompat.checkSelfPermission(this.mc, "android.permission.READ_PHONE_STATE") != 0) {
            ActivityCompat.requestPermissions((Activity) this.mc, new String[]{"android.permission.READ_PHONE_STATE"}, REQUEST_READ_PHONE_STATE);
            return;
        }
        final String deviceId = telephonyManager.getDeviceId();
        FirebaseFirestore.getInstance().collection("Refferers").document(str).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User obj = (User) ((DocumentSnapshot) task.getResult()).toObject(User.class);
                ArrayList arrayList = new ArrayList();
                if (obj != null) {
                    if (obj.getmListOfRefferers() != null) {
                        arrayList = obj.getmListOfRefferers();
                        if (!arrayList.contains(deviceId)) {
                            arrayList.add(deviceId);
                            obj.setmRefferals(obj.getmRefferals() + 1);
                        }
                    } else {
                        arrayList.add(deviceId);
                        obj.setmRefferals(obj.getmRefferals() + 1);
                    }
                    obj.setmListOfRefferers(arrayList);
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        FirebaseFirestore.getInstance().collection("Refferers").document(str).set(obj);
                        return;
                    } else if (FirebaseAuth.getInstance().getUid() != str) {
                        FirebaseFirestore.getInstance().collection("Refferers").document(str).set(obj);
                        return;
                    } else {
                        Toast.makeText(InstallReferrerReceiver.this.mc, "You can't refer yourself", 0).show();
                        return;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append("dosen't exist");
                Log.d("YOYO", stringBuilder.toString());
            }
        });
    }
}