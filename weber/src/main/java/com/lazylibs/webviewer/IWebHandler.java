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
}
