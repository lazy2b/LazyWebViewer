package com.lazylibs.webviewer.utils;

import android.app.Application;

import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

public interface OneSignalUtils {
    static void onCreate(Application application, String appId) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(application);
        OneSignal.setAppId(appId);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
//        OneSignal.promptForPushNotifications();
    }

    static void showPrompt() {
        //获取设备状态
        OSDeviceState device = OneSignal.getDeviceState();
        if(device!=null){
            boolean areNotificationsEnabled = device.areNotificationsEnabled();
            //判断后台是否已经订阅
            if (!areNotificationsEnabled) {
                //调起应用内消息提示打开通知权限，用户打开权限后，后台会显示已订阅
                OneSignal.addTrigger("showPrompt", "true");
            }
        }
    }
}
