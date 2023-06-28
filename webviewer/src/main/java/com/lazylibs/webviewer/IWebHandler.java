package com.lazylibs.webviewer;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

interface IWebHandler extends IActivityGetter {
    void doProgressed(String url);

    default void onProgressChanged(WebView view, int newProgress) {
    }

    default void onRealPageFinished(String url, boolean isReceivedError) {
    }

    default LazyFinishedHelper getLazyFinishedHelper() {
        return null;
    }

    default WebChromeClient getWebChromeClient() {
        return null;
    }

    default WebViewClient getWebViewClient() {
        return null;
    }
}
