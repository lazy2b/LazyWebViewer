package com.lazylibs.webviewer.core;

import com.lazylibs.webviewer.IActivityGetter;

interface IWebHandler extends IActivityGetter {
    void doProgressed(String url);
    default void onRealPageFinished(String url, boolean isReceivedError){}
}
