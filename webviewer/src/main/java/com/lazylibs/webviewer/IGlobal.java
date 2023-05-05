package com.lazylibs.webviewer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.lazylibs.webviewer.cache.ICache;
import com.lazylibs.webviewer.model.Pz;
import com.lazylibs.webviewer.model.Us;
import com.lazylibs.webviewer.utils.Xc;

public final class IGlobal {
    private static Application application = null;
    public static boolean DEBUGGABLE = false;
    public static final String pk = "TheLittleRedDotOfAllEvil";
    public static Pz pz = null;

    public static void onCreate(Application app) {
        application = app;
        DEBUGGABLE = app != null && ((app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        ICache.onCreate(app);
        NetworkMonitor.startMonitor(application);
    }

    public static void onCreate(Application app, String datp) {
        onCreate(app);
        if (pz == null) {
            pz = JSON.parseObject(Xc.akRaw(datp, pk), Pz.class);
        }
        if (isMain()) {
            Us.load();
        }
    }

    public static boolean isEmptyOrNull(@Nullable String raw) {
        return raw == null || raw.length() == 0;
    }

    public static String removeZWSP(String raw) {
        return isEmptyOrNull(raw) ? "" : raw.replaceAll("\\s", "").replace("​", "");
    }

    public static Application globalContext() {
        if (application == null) {
            throw new RuntimeException("application 尚未初始化！");
        }
        return application;
    }

    public static void onTerminate() {
        NetworkMonitor.onTerminate(application);
        ICache.onTerminate(application);
        application = null;
    }

    public static boolean isMain() {
        return isMain(application);
    }

    /**
     * 判断是否是主进程
     *
     * @param application 当前的Application
     * @return 是否是主进程
     */
    private static boolean isMain(Application application) {
        if (application == null) {
            return false;
        }
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        String packageName = application.getPackageName();
        return processName.equals(packageName);
    }

    public static void logE(String msg) {
        logE("Tring.IGlobal", msg);
    }

    public static void logI(String msg) {
        logI("Tring.IGlobal", msg);
    }

    public static void logD(String msg) {
        logD("Tring.IGlobal", msg);
    }

    public static void logE(String tag, String msg) {
        if (DEBUGGABLE) Log.e(tag, msg);
    }

    public static void logI(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void logD(String tag, String msg) {
        if (DEBUGGABLE) Log.d(tag, msg);
    }

    public static void logP(String tag, String msg) {
        System.out.println(((tag == null || tag.length() == 0) ? msg : String.format("%s:%s", tag, msg)));
    }

    public static void logP(String msg) {
        logP(null, msg);
    }
}
