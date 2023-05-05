package com.lazylibs.webviewer.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSON;
import com.lazylibs.updater.model.UpdateResult;
import com.lazylibs.webviewer.NetworkMonitor;
import com.lazylibs.webviewer.R;
import com.lazylibs.webviewer.model.As;
import com.lazylibs.webviewer.model.Us;
import com.lazylibs.webviewer.update.IVersionChecker;
import com.lazylibs.webviewer.update.VersionCheckViewModel;
import com.lazylibs.webviewer.utils.OneSignalUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class WebViewer extends AppCompatActivity {
    private Toast exitToast;
    private WebView webView;
    private WebViewHelper webViewHelper;
    private TringJsInterface jsInterface;
    private String cLoadUrl = "";
    private boolean isBlank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.wb_view);
        String settings = getIntent().getStringExtra(APP_SETTINGS_EXTRA_KEY);
        As as = JSON.parseObject(settings, As.class);
//        WebViewHelper.logE("settingsString",JSON.toJSONString(settings));
//        WebViewHelper.logE("appStrings",JSON.toJSONString(appSettings));
        jsInterface = new TringJsInterface(as.currency, as.eventTknMap);

        webViewHelper = new WebViewHelper();
        webViewHelper.initWebView(webView, new IWebHandler() {

            void doReload(String failingUrl) throws MalformedURLException {
                webView.stopLoading();
                if (NetworkMonitor.isNetworkConnected()) {
                    cLoadUrl = Us.w(true) + as.link;
                    URL cURL = new URL(cLoadUrl), fURL = new URL(failingUrl);
                    if (!cURL.getHost().equals(fURL.getHost())) {
//                        WebViewHelper.logE("doReload", "cLoadUrl:" + cLoadUrl + "<<<<" + "failingUrl:" + failingUrl);
                        webView.postDelayed(()-> webView.loadUrl(cLoadUrl), 200L);
                    } else {
//                        WebViewHelper.logE("doReload", "about:blank");
                        isBlank = true;
                        webView.loadUrl("about:blank");
//                        webView.postDelayed(()-> , 200L);
                    }
                }
            }

            @Override
            public void onRealPageFinished(String url, boolean isReceivedError) {
                if (isReceivedError) {
                    try {
                        doReload(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            boolean firstLoadOver = false;

            @Override
            public void doProgressed(String url) {
                if (!firstLoadOver) {
                    firstLoadOver = true;
                    afterVersionChecked();
                }
            }

            @Override
            public Activity requireActivity() {
                return WebViewer.this;
            }
        }, getIntent().getBooleanExtra(USE_AEM_EXTRA_KEY, false), new HashMap<String, Object>() {{
            // 添加js交互接口类，并起别名 AndroidWebView
            put("AndroidWebView", jsInterface);
        }});

        webView.loadUrl(cLoadUrl = Us.w(false) + as.link);

        WebViewHelper.logE("cLoadUrl", cLoadUrl);

        exitToast = Toast.makeText(this, R.string.tips_exit, Toast.LENGTH_SHORT);

        new ViewModelProvider(this).get(VersionCheckViewModel.class).startCheckVersion(new IVersionChecker() {
            @Override
            public void vHelperCallBack(UpdateResult result) {
                if (result != UpdateResult.Destroy && result != UpdateResult.Success) {
//                    afterVersionChecked();
                }
            }

            @Override
            public Activity requireActivity() {
                return WebViewer.this;
            }
        }, Us.v(false));
    }

    void afterVersionChecked() {
        webView.postDelayed(() -> {
            findViewById(R.id.splash_view).setVisibility(View.GONE);
            LottieAnimationView animationView = findViewById(R.id.animationView);
            animationView.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.clearAnimation();
            OneSignalUtils.showPrompt();
        }, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (webViewHelper != null) {
            webViewHelper.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }


    // add in 1109
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isBlank && keyCode == 4 && webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        if ((this.exitToast.getView() != null) && (this.exitToast.getView().isShown())) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.exitToast.getView() == null) {
            return super.onKeyDown(keyCode, event);
        }
        this.exitToast.show();
        return true;
    }

    /***
     *  启动浏览器，
     * @param activity
     * @param link 首页地址
     * @param currency 币种代
     * @param useAndroidEM 历史遗留问题：是否需要拦截并使用本地AndroidEM.js文件
     * @param eMap 事件列表
     */
    public static void start(Activity activity, String link, String currency, boolean useAndroidEM, HashMap<String, String> eMap) {
        try {
            start(activity, JSON.toJSONString(new As(link, currency, eMap)), useAndroidEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *  启动浏览器，
     * @param activity
     * @param link 首页地址
     * @param currency 币种代码
     * @param eMap 事件列表
     */
    public static void start(Activity activity, String link, String currency, HashMap<String, String> eMap) {
        start(activity, link, currency, false, eMap);
    }

    /***
     *  启动浏览器，
     * @param activity
     * @param dats 配置加密串
     * @param useAndroidEM 历史遗留问题：是否需要拦截并使用本地AndroidEM.js文件
     */
    public static void start(Activity activity, String dats, boolean useAndroidEM) {
//        WebViewHelper.logE("start", dats);
        Intent intent = new Intent(activity, WebViewer.class);
        intent.putExtra(APP_SETTINGS_EXTRA_KEY, dats);
        intent.putExtra(USE_AEM_EXTRA_KEY, useAndroidEM);
        activity.startActivity(intent);
        activity.finish();
    }

    /***
     *  启动浏览器，
     * @param activity
     * @param dats 配置串
     */
    public static void start(Activity activity, String dats) {
        start(activity, dats, false);
    }

    public static final String APP_SETTINGS_EXTRA_KEY = "app.settings";
    /***
     * useAndroidEM 历史遗留问题：是否需要拦截并使用本地AndroidEM.js文件
     */
    public static final String USE_AEM_EXTRA_KEY = "use.android.em";

}
