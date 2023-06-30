package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.HashMap;

public class LazyWebActivity extends Activity {
    protected WebView webView;
    protected LazyWebHelper lazyWebHelper;
    protected String cLoadUrl = "";
    protected boolean isBlank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpl_webviewer);
        webView = findViewById(R.id.wb_view);
        cLoadUrl = getIntent().getStringExtra(WEB_URL);
        cLoadUrl = TextUtils.isEmpty(cLoadUrl) ? "https://github.com/lazy2b/LazyWebViewer" : cLoadUrl;
        loadWeb();
    }

    protected void loadWeb() {

        lazyWebHelper = new LazyWebHelper.Builder(new IWebHandler() {

            @Override
            public void onRealPageFinished(String url, boolean isReceivedError) {
                // real page finished...
                if (isReceivedError) {
                    // handle error or load backup url...
                    isBlank = true;
                    webView.loadUrl("about:blank");
                }
            }

            boolean firstLoadOver = false;

            @Override
            public void doProgressed(String url) {
                if (!firstLoadOver) {
                    firstLoadOver = true;
                    // handle first loaded...
                    findViewById(R.id.wb_loading).setVisibility(View.GONE);
                }
            }

            @Override
            public Activity requireActivity() {
                return LazyWebActivity.this;
            }
        }).build();
        lazyWebHelper.initWebView(webView, getJsBridge());

        webView.loadUrl(cLoadUrl);

        LazyWebHelper.logE("cLoadUrl", cLoadUrl);
    }

    protected HashMap<String, Object> getJsBridge() {
        return new HashMap<String, Object>() {{
            put("androidObj", new Object() {
                @JavascriptInterface
                public void androidFun(String jsParams) {

                }
            });
        }};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lazyWebHelper != null) {// handle choose file results...
            lazyWebHelper.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isBlank && keyCode == 4 && webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return true;
    }

    public static void start(Activity activity, String url, boolean finish) {
        Intent intent = new Intent(activity, LazyWebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        if (finish) activity.finish();
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, LazyWebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public static void start(Activity activity, String url, boolean finish, HashMap<String, Object> jsInterface) {
        Intent intent = new Intent(activity, LazyWebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_BRIDGE, jsInterface);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);
    }

    public static final String WEB_URL = "WEB.URL";
    public static final String WEB_BRIDGE = "WEB.BRIDGE";

}
