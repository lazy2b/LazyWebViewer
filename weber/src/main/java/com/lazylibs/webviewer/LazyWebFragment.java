package com.lazylibs.webviewer;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.activity.result.ActivityResultCaller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class LazyWebFragment extends Fragment {

    protected WebView webView;
    protected LazyWebHelper lazyWebHelper;
    protected String cLoadUrl = "";
    protected HashMap<String, Object> interfaces;
    protected boolean isBlank = false;

    public boolean canGoBack(int keyCode, KeyEvent event) {
        if (!isBlank && keyCode == 4 && webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return false;
    }

    protected void loadWeb() {

        lazyWebHelper = new LazyWebHelper.Builder(new IWebHandler() {

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
                    requireView().findViewById(R.id.loading_container).setVisibility(View.GONE);
                }
            }

            @Override
            public ActivityResultCaller requireActivityResultCaller() {
                return LazyWebFragment.this;
            }

            @Override
            public Activity requireActivity() {
                return getActivity();
            }
        }).build();
        lazyWebHelper.initWebView(webView, interfaces);

        webView.loadUrl(cLoadUrl);

        LazyWebHelper.logE("cLoadUrl", cLoadUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tpl_webviewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.wb_view);
        cLoadUrl = TextUtils.isEmpty(cLoadUrl) ? "https://github.com/lazy2b/LazyWebViewer" : cLoadUrl;
        loadWeb();
    }

    public static LazyWebFragment newInstance(String url, HashMap<String, Object> interfaces) {
        Bundle args = new Bundle();
        LazyWebFragment fragment = new LazyWebFragment();
        fragment.setUrl(url);
        fragment.setJsInterfaces(interfaces);
        fragment.setArguments(args);
        return fragment;
    }

    protected void setJsInterfaces(HashMap<String, Object> interfaces) {
        this.interfaces = interfaces;
    }

    protected void setUrl(String url) {
        cLoadUrl = TextUtils.isEmpty(url) ? "about:blank" : url;
    }
}
