package com.lazylibs.webviewer.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lazylibs.webviewer.IGlobal;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class WebViewHelper {
    public static String WEB_LOG = "WebLog";

    public static void logE(String tag, String msg) {
        IGlobal.logE(WEB_LOG, tag + ":" + msg);
    }

    public static void logI(String tag, String msg) {
        IGlobal.logI(WEB_LOG, tag + ":" + msg);
    }

    public WebViewHelper() {
    }

    TringWebChromeClient tringWebChromeClient;
    TringWebViewClient tringWebViewClient;
    private final HashMap<String, AtomicBoolean> mIsFinished = new HashMap<>();
    private boolean mIsReceivedError = false;

    private synchronized AtomicBoolean getFinished(String url) {
        if (!mIsFinished.containsKey(url)) {
            mIsFinished.put(url, new AtomicBoolean(false));
        }
        return mIsFinished.get(url);
    }

    @SuppressLint("JavascriptInterface")
    public void initWebView(WebView webView, IWebHandler mainWebHandler, boolean useAndroidEM, HashMap<String, Object> interfaces) {
        webView.setInitialScale(0);
        webView.setWebViewClient(tringWebViewClient = new TringWebViewClient(useAndroidEM) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mIsReceivedError = false;
                getFinished(url);
                super.onPageStarted(view, url, favicon);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String fUrl) {
                mIsReceivedError = true;
                super.onReceivedError(view, errorCode, description, fUrl);
            }
        });
        webView.setWebChromeClient(tringWebChromeClient = new TringWebChromeClient(new IWebHandler() {

            @Override
            public void doProgressed(String url) {
                mainWebHandler.doProgressed(url);
//                WebViewHelper.logE("doProgressed", "11111");
                if (!getFinished(url).compareAndSet(false, true)) {
//                    WebViewHelper.logE("doProgressed", "22222");
                    return;
                }
//                WebViewHelper.logE("doProgressed", "33333");
                mainWebHandler.onRealPageFinished(url, mIsReceivedError);
            }

            @Override
            public Activity requireActivity() {
                return mainWebHandler.requireActivity();
            }
        }));
        if (interfaces != null) {
            for (String key : interfaces.keySet()) {
                webView.addJavascriptInterface(interfaces.get(key), key);
            }
        }
        initSettings(webView.getSettings());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (tringWebChromeClient != null) {
            tringWebChromeClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    static void initSettings(WebSettings settings) {
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
    }

}
