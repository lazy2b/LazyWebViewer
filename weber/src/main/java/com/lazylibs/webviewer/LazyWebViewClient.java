package com.lazylibs.webviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LazyWebViewClient extends WebViewClient {

    public LazyWebViewClient(LazyFinishedHelper finishedHelper) {
        this.finishedHelper = finishedHelper;
    }

    LazyFinishedHelper finishedHelper;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        finishedHelper.toPageStarted(url);
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
        finishedHelper.toReceivedError();
//        WebViewHelper.logE("onReceivedError", String.format("=>%s:%s:%s", errorCode, description, view.getUrl() +" >>> " + fUrl));
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

    private boolean filterUrlScheme(WebView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                LazyWebHelper.logE("OverrideUrl", url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else {
                    LazyWebHelper.logE("OverrideUrl", "Application is not installed. Please install it to use this feature.");
//                    Toast.makeText(view.getContext(), "Application is not installed. Please install it to use this feature.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        }
        return false;
    }
}