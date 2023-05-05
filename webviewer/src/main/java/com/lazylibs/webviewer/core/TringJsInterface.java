package com.lazylibs.webviewer.core;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("JavascriptInterface")
interface JsInterface {
    @android.webkit.JavascriptInterface
    void firebaseEvent(String eventName, String key);
}

@SuppressLint("JavascriptInterface")
public class TringJsInterface {
    private static final String TAG = "JsiLog";
    private String currency = "";
    private Map<String, String> eventTknMap = new HashMap<>();

    public TringJsInterface(String currency, Map<String, String> eventMap) {
        this.currency = currency;
        this.eventTknMap = eventMap;
    }

    /**
     * JS 调用 Android
     */
    @android.webkit.JavascriptInterface
    public void firebaseEvent(String eventName, String key) {
        if (TextUtils.isEmpty(eventName)) return;
        WebViewHelper.logI(TAG, String.format("firebaseEvent('%s','...');", eventName));
        if (eventTknMap.containsKey(eventName)) {
            String eventTkn = eventTknMap.get(eventName);
            if (eventTkn != null) {
                AdjustEvent adjustEvent = new AdjustEvent(eventTkn);
                if (!TextUtils.isEmpty(key)) {
//                    WebViewHelper.logI(TAG, String.format("firebaseEvent('%s','%s');", eventName, key));
                    switch (eventTkn) {
                        case "FIRST_RECHARGE":
                        case "SECOND_RECHARGE":
                        case "PAY_RECHARGE":
                            adjustEvent.setRevenue(Double.parseDouble(key), currency);
                        case "LOGIN":
                        case "REGISTER":
                            WebViewHelper.logI(TAG,"LOGIN || REGISTER ...");
                            WebViewHelper.logI(TAG, "to bind  ...");
                            OneSignal.setExternalUserId(key, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
                                void checkResult(JSONObject results, String checkProperties) {
                                    JSONObject checkObj = results.optJSONObject(checkProperties);
                                    if (checkObj != null) {
                                        String log = String.format("Set external user id for %s status: %b", checkProperties, checkObj.optBoolean("success", false)) ;
                                        WebViewHelper.logI(TAG, log);
                                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, log);
                                    }
                                }

                                @Override
                                public void onSuccess(JSONObject results) {
                                    checkResult(results, "push");
                                    checkResult(results, "email");
                                    checkResult(results, "sms");
                                }

                                @Override
                                public void onFailure(OneSignal.ExternalIdError error) {
                                    String log = "Set external user id done with error: " + error.toString();
                                    WebViewHelper.logI(TAG, log);
                                    OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, log);
                                }
                            });
                    }
                }
                Adjust.trackEvent(adjustEvent);
            }
        }

    }

}