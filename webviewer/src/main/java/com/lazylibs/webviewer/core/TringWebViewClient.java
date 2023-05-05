package com.lazylibs.webviewer.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TringWebViewClient extends WebViewClient {

    public TringWebViewClient(boolean useAndroidEM) {
        this.useAndroidEM = useAndroidEM;
    }

    private boolean useAndroidEM = false;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
//        WebViewHelper.logE("onPageStarted", url);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        WebViewHelper.logE("onPageFinished", url);
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        super.onReceivedSslError(view, handler, error);
        // 忽略ssl错误
        handler.proceed();
//        WebViewHelper.logE("onReceivedSslError", view.getUrl() +" >>> " + fUrl);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String fUrl) {
//        WebViewHelper.logE("onReceivedError", String.format("=>%s:%s:%s", errorCode, description, view.getUrl() +" >>> " + fUrl));
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        try {
            String url = request.getUrl().toString();
            if (url.contains("androidEM.js")) {
                if (useAndroidEM) {
                    WebViewHelper.logE("Intercept", url);
                    String file_type = "application/x-javascript";
                    // 解决css、js的跨域问题
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Access-Control-Allow-Origin", "*");
                    headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
                    headers.put("Access-Control-Max-Age", "3600");
                    headers.put("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization");
                    // add in 0914
                    WebResourceResponse response = new WebResourceResponse(file_type, "utf-8", view.getContext().getAssets().open("androidEM.js"));
                    response.setResponseHeaders(headers);
                    return response;
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return filterUrlScheme(view, url) || super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
        return (uri != null && filterUrlScheme(view, uri.toString())) || super.shouldOverrideUrlLoading(view, request);
    }

    private boolean filterUrlScheme(WebView view, @Nullable String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                WebViewHelper.logE("OverrideUrl", url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else {
                    WebViewHelper.logE("OverrideUrl", "Application is not installed. Please install it to use this feature.");
//                    Toast.makeText(view.getContext(), "Application is not installed. Please install it to use this feature.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        }
        return false;
    }
}