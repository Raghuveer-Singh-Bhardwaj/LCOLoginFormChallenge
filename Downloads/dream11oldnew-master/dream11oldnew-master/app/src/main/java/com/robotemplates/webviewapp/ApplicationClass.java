package com.robotemplates.webviewapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
  //      Ads.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        MultiDex.install(this);

    }
}
