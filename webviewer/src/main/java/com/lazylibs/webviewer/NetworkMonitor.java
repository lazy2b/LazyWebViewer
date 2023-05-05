package com.lazylibs.webviewer;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NetworkMonitor extends ConnectivityManager.NetworkCallback {

    private static NetworkMonitor networkMonitor;

    public static void startMonitor(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (networkMonitor == null) {
                networkMonitor = new NetworkMonitor(application);
            }
        }
    }

    public static void onTerminate(Application application) {
        if (networkMonitor != null) {
            networkMonitor.stopMonitor(application);
            networkMonitor = null;
        }
    }

    public static boolean isNetworkConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return networkMonitor != null && networkMonitor.isConnected;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) IGlobal.globalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        }
    }

    private boolean isConnected = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private NetworkMonitor(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(this);
    }

    private void stopMonitor(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        isConnected = true;
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        isConnected = false;
    }
}
