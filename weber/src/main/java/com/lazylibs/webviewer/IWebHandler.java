package com.lazylibs.webviewer;

import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

interface IWebHandler extends IActivityGetter {
    void doProgressed(String url);

    default void onProgressChanged(WebView view, int newProgress) {
    }

    default void onRealPageFinished(String url, boolean isReceivedError) {
    }
}
