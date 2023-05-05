package com.lazylibs.webviewer.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lazylibs.webviewer.IGlobal;
import com.lazylibs.webviewer.cache.ICache;
import com.lazylibs.webviewer.utils.SimpleOkHttp;
import com.lazylibs.webviewer.utils.Xc;


public interface Us {
    String SPF = "ucs";

    /***
     * urls缓存
     */
    String SP_KEY_UC =
            getAppVersionName(IGlobal.globalContext()) + "sp_uc";

    /***
     * 上次获取动态链接配置时间
     */
    String SP_KEY_UCT =
            getAppVersionName(IGlobal.globalContext()) + "sp_uct";
    String SP_KEY_UC_V = "sp_uc_v";
    String SP_KEY_UC_W = "sp_uc_w";
    long UCT = 15 * 60 * 1000;

    static void load() {
        if (!isCacheTime()) {
        String du = IGlobal.pz.zdu();
//        IGlobal.logE("load()" + du);
        if (TextUtils.isEmpty(du)) return;
        SimpleOkHttp simpleOkHttp = new SimpleOkHttp() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                try {
                    U u = JSON.parseObject(data, U.class);
                    if (u != null) {
                        Us.icu(u);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        simpleOkHttp.get(du);
        }
    }

    /**
     * 初始化动态链接的配置信息
     *
     * @param u 动态链接的内容
     */
    static void icu(U u) {
        ICache iCache = getCache();
        String json = u == null ? IGlobal.pz.zdp() : JSON.toJSONString(u);
        iCache.put(SP_KEY_UC, json);
        iCache.put(SP_KEY_UCT, System.currentTimeMillis());
    }
    static String v(boolean nextUrl) {
        ICache iCache = getCache();
        String vUrl = iCache.getString(SP_KEY_UC_V, "");
        if (IGlobal.isEmptyOrNull(vUrl) || nextUrl) {
            String cu = iCache.getString(SP_KEY_UC);
            U u = JSON.parseObject(IGlobal.isEmptyOrNull(cu) ? IGlobal.pz.zdp() :cu , U.class);
            Rur rur = u.rzv(vUrl);
            if(!IGlobal.isEmptyOrNull(rur.replace)){
                // 重新加密后替换本地子串
                u.v = Xc.akCode(rur.replace, u.k);
                iCache.put(SP_KEY_UC, JSON.toJSONString(u));
            }
            vUrl = rur.url;
            iCache.put(SP_KEY_UC_V, vUrl);
        }
        return IGlobal.removeZWSP(vUrl);
    }
    static String w(boolean nextUrl) {
        ICache iCache = getCache();
        String wUrl = iCache.getString(SP_KEY_UC_W, "");
        if (IGlobal.isEmptyOrNull(wUrl) || nextUrl) {
            String cu = iCache.getString(SP_KEY_UC);
            U u = JSON.parseObject(IGlobal.isEmptyOrNull(cu) ? IGlobal.pz.zdp() :cu , U.class);
            Rur rur = u.rzw(wUrl);
            if(!IGlobal.isEmptyOrNull(rur.replace)){
                // 重新加密后替换本地子串
                u.w = Xc.akCode(rur.replace, u.k);
                iCache.put(SP_KEY_UC, JSON.toJSONString(u));
            }
            wUrl = rur.url;
            iCache.put(SP_KEY_UC_W, wUrl);
        }
        return IGlobal.removeZWSP(wUrl);
    }

    static boolean isCacheTime() {
        ICache iCache = getCache();
        long cacheTime = iCache.getLong(SP_KEY_UCT);
        return !(cacheTime == -1 || Math.abs(System.currentTimeMillis() - cacheTime) > UCT);
    }

    static ICache getCache() {
        return ICache.create(SPF);
    }

    static void foreReload() {
        ICache iCache = getCache();
        iCache.put(SP_KEY_UCT, System.currentTimeMillis() - UCT - 1000);
    }

    /**
     * 获取 App 版本号
     *
     * @param context context
     * @return App 版本号
     */
    static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取 App 版本号
     *
     * @param context     context
     * @param packageName 包名
     * @return App 版本号
     */
    static String getAppVersionName(Context context, final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
