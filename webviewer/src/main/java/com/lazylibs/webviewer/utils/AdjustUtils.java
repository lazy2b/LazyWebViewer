package com.lazylibs.webviewer.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;

public class AdjustUtils {

    static Application.ActivityLifecycleCallbacks callbacks = null;

    public static void onCreate(Application application, String appToken) {
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;      //AdjustConfig.ENVIRONMENT_SANDBOX;  AdjustConfig.ENVIRONMENT_PRODUCTION;
        AdjustConfig config = new AdjustConfig(application, appToken, environment);
        Adjust.onCreate(config);
        config.setLogLevel(LogLevel.VERBOSE);
        application.registerActivityLifecycleCallbacks(callbacks = new AdjustLifecycleCallbacks());
    }

    public static void onTerminate(Application application) {
        if (callbacks != null) {
            application.unregisterActivityLifecycleCallbacks(callbacks);
            callbacks = null;
        }
    }

    static final class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }
    }

}