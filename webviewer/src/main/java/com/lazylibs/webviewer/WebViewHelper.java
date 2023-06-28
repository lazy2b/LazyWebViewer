package com.lazylibs.webviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;


public class WebViewHelper {

    public static class Builder {

        public Builder(@Nullable IWebHandler webHandler) {
            this(webHandler, null);
        }

        public Builder(@Nullable LazyFinishedHelper finishedHelper) {
            this(null, finishedHelper);
        }

        public Builder(@Nullable IWebHandler webHandler, @Nullable LazyFinishedHelper finishedHelper) {
            this.webHandler = webHandler;
            this.finishedHelper = finishedHelper;
        }

        public Builder setWebChromeClient(WebChromeClient webChromeClient) {
            this.webChromeClient = webChromeClient;
            return this;
        }

        public Builder setWebViewClient(WebViewClient webViewClient) {
            this.webViewClient = webViewClient;
            return this;
        }

        IWebHandler webHandler;
        WebChromeClient webChromeClient;
        WebViewClient webViewClient;
        LazyFinishedHelper finishedHelper;

        public WebViewHelper build() {
            finishedHelper = finishedHelper != null ? finishedHelper : new LazyFinishedHelper();
            webViewClient = webViewClient != null ? webViewClient : new LazyWebViewClient(finishedHelper);
            if (webHandler != null) {
                webChromeClient = webChromeClient != null ? webChromeClient : new LazyWebChromeClient(webHandler, finishedHelper);
            }
            return new WebViewHelper(webChromeClient, webViewClient);
        }
    }

    public static String WEB_LOG = "WebLog";

    public static void logE(String tag, String msg) {
        Log.e(WEB_LOG, tag + ":" + msg);
    }

    public static void logI(String tag, String msg) {
        Log.i(WEB_LOG, tag + ":" + msg);
    }

    private WebChromeClient webChromeClient = null;
    private WebViewClient webViewClient = null;

    private WebViewHelper(@Nullable WebChromeClient webChromeClient, WebViewClient webViewClient) {
        this.webChromeClient = webChromeClient;
        this.webViewClient = webViewClient;
    }

    @SuppressLint("JavascriptInterface")
    public void initWebView(@NonNull WebView webView, @Nullable HashMap<String, Object> interfaces) {
        webView.setInitialScale(0);
        webView.setWebViewClient(webViewClient);
        if (webChromeClient != null) {
            webView.setWebChromeClient(webChromeClient);
        }
        if (interfaces != null) {
            for (String key : interfaces.keySet()) {
                webView.addJavascriptInterface(interfaces.get(key), key);
            }
        }
        initSettings(webView.getSettings());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (webChromeClient != null && webChromeClient instanceof LazyWebChromeClient) {
            ((LazyWebChromeClient) webChromeClient).onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void initSettings(WebSettings settings) {
        settings.setBuiltInZoomControls(false);//设置支持缩放
        settings.setSupportZoom(false);//支持缩放
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setJavaScriptEnabled(true);//支持js
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setSupportMultipleWindows(true);
        settings.setLoadsImagesAutomatically(true);//支持自动加载图片
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setDomStorageEnabled(true);//开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
        if (Build.VERSION.SDK_INT > 21) {
            settings.setMixedContentMode(0);
        }
        settings.setBlockNetworkImage(false);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        ///////// 2023。6。28 新增！！！！未知影响
        settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 开启缓存
        settings.setLoadWithOverviewMode(true);// 自适应屏幕
        settings.setDatabaseEnabled(true); // 开启数据库
    }

}
