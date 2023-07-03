package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.lazylibs.utils.FragmentUtils;

import java.io.Serializable;
import java.util.HashMap;

public class LazyWebActivity extends AppCompatActivity {
    LazyWebFragment web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_borswer);
        initWebView(getIntent().getStringExtra(WEB_URL), getIntent().getSerializableExtra(WEB_BRIDGE));
    }

    protected void initWebView(String stringExtra, Serializable jsBridge) {
        HashMap<String,Object> objectHashMap = getJsBridge();
        if(jsBridge instanceof HashMap){
            objectHashMap = (HashMap<String, Object>) jsBridge;
        }
        FragmentUtils.replaceFragmentToActivity(R.id.fl_web, getSupportFragmentManager(), web = LazyWebFragment.newInstance(stringExtra, objectHashMap));
    }

    protected HashMap<String, Object> getJsBridge() {
        return new HashMap<>();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (web.canGoBack(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void start(Activity activity, String url, boolean finish) {
        start(activity, url, finish, new HashMap<>());
    }

    public static void start(Context context, String url) {
        start(context, url, new HashMap<>());
    }

    public static void start(Context context, String url, HashMap<String, Object> jsInterface) {
        Intent intent = new Intent(context, LazyWebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_BRIDGE, jsInterface);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public static void start(Activity activity, String url, boolean finish, HashMap<String, Object> jsInterface) {
        Intent intent = new Intent(activity, LazyWebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_BRIDGE, jsInterface);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);
        if (finish) activity.finish();
    }

    public static final String WEB_URL = "WEB.URL";
    public static final String WEB_BRIDGE = "WEB.BRIDGE";

}
