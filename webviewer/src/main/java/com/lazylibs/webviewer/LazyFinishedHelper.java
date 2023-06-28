package com.lazylibs.webviewer;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LazyFinishedHelper {

    private final HashMap<String, AtomicBoolean> mIsFinished = new HashMap<>();
    private boolean mIsReceivedError = false;

    private synchronized AtomicBoolean getFinished(String url) {
        if (!mIsFinished.containsKey(url)) {
            mIsFinished.put(url, new AtomicBoolean(false));
        }
        return mIsFinished.get(url);
    }

    public void toPageStarted(String url) {
        mIsReceivedError = false;
        getFinished(url);
    }

    public void toReceivedError() {
        mIsReceivedError = true;
    }

    public boolean isRealFinished(String url) {
        return !getFinished(url).compareAndSet(false, true);
    }

    public boolean isReceivedError() {
        return mIsReceivedError;
    }
}

