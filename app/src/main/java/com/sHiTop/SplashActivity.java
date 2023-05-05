package com.sHiTop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;



import com.lazylibs.webviewer.core.WebViewer;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
//        HashMap<String, String> eMap = new HashMap<>();
//        eMap.put("FB_LOGIN", "js1pfh");
//        eMap.put("FB_REGISTER", "jhotfa");
//        eMap.put("FIRST_RECHARGE", "r59iww");
//        eMap.put("GOOGLE_LOGIN", "kcgz18");
//        eMap.put("GOOGLE_REGISTER", "yi9f2n");
//        eMap.put("LOGIN", "keq9gj");r
//        eMap.put("PAGE_VIEW", "4yfely");
//        eMap.put("PAY_RECHARGE", "kavk11");
//        eMap.put("REGISTER", "gsvqp9");
//        eMap.put("SECOND_RECHARGE", "gmh2i0");
        WebViewer.start(this, BuildConfig.DATS); // 配置基本不变，固定后使用这种方式！！！！！
//        WebActivity.start(this, BuildConfig.DATS, true); // useAndroidEM 历史遗留问题：是否需要拦截并使用本地AndroidEM.js文件
        //P3.1.pre
//        WebActivity.start(this, "https://apanalo.pre-release.xyz?fromApk=fromApk", "PHP", "activityAp", "https://apanalo-assets.pre-release.xyz/apk/version.json", eMap);
//        //P3.1
//        WebActivity.start(this, "https://apanalo.com?referralcode=64318408e893cf692c151946&fromApk=fromApk", "PHP", "activityAp", "https://assets.apanalo.com/apk/version.json", eMap);
//        //P3
//        WebActivity.start(this, "https://topphl.com?referralcode=63f82d3a132800f0f6389b98&fromApk", "PHP", "activityAj", "https://assets.topphl.com/apk/version.json", eMap);
//        //P2
//        WebActivity.start(this, "https://philucky.com/?referralcode=6426bc8ebd47d057938ab84d&fromApk=fromApk", "PHP", "activityAj", "https://assets.philucky.com/apk/version.json", eMap);
//        //P1
//        WebActivity.start(this, "https://phlwin.com?referralcode=63851941ac02be537c7aa26f&fromApk", "PHP", "activityAj", "https://assets.phlwin.com/apk/version.json", eMap);
//        // M
//        WebActivity.start(this, "https://mexlucky.com?referralcode=63d9135206f69d8fe00d83e8&fromApk", "MXN", "activityAj", "https://assets.mexlucky.com/apk/version.json", eMap);
//        // V
//        WebActivity.start(this, "https://888vang.com/?referralcode=&fromApk", "VND", "activityAj", "https://assets.888vang.com/apk/version.json", eMap);
//        // B2
//        WebActivity.start(this, "https://aajogo.com/?referralcode=64268eb30314122d457f254c&fromApk=fromApk", "BRL", "activityAj", "https://assets.aajogo.com/apk/version.json", eMap);
//        // B1
//        WebActivity.start(this, "https://betfiery.com/?&fromApk", "BRL", "activityAj", "https://assets.betfiery.com/apk/version.json",true, eMap);
    }

}