package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class WebViewer extends AppCompatActivity {
    protected WebView webView;
    protected WebViewHelper webViewHelper;
    protected String cLoadUrl = "";
    protected boolean isBlank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.wb_view);
        cLoadUrl = getIntent().getStringExtra(WEB_URL);
        cLoadUrl = TextUtils.isEmpty(cLoadUrl) ? "https://github.com/lazy2b/LazyWebViewer" : cLoadUrl;
        webViewHelper = new WebViewHelper.Builder(new IWebHandler() {

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
                }
            }

            @Override
            public Activity requireActivity() {
                return WebViewer.this;
            }
        }).build();
        webViewHelper.initWebView(webView, new HashMap<String, Object>() {{
            put("androidObj", new Object() {
                @JavascriptInterface
                public void androidFun(String jsParams) {

                }
            });
        }});

        webView.loadUrl(cLoadUrl);

        WebViewHelper.logE("cLoadUrl", cLoadUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (webViewHelper != null) {
            webViewHelper.onActivityResult(requestCode, resultCode, data);
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


    // add in 1109
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isBlank && keyCode == 4 && webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return true;
    }

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, WebViewer.class);
        intent.putExtra(WEB_URL, url);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewer.class);
        intent.putExtra(WEB_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public static final String WEB_URL = "WEB.URL";

}
