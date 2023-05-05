package com.sHiTop;

import android.app.Application;

import com.lazylibs.webviewer.IGlobal;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        IGlobal.onCreate(this, BuildConfig.DATP);
//        OneSignalUtils.onCreate(this, BuildConfig.ONESIGNAL_APPID);
//        AdjustUtils.onCreate(this, BuildConfig.ADJUST_TOKEN);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        AdjustUtils.onTerminate(this);
//        IGlobal.onTerminate();
    }

}
