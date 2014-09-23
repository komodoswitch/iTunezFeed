package com.keran.marinov.itunezfeed.http;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusManager {
    private static final String LOG_TAG = "NetworkStatusManager";
    private static final boolean LOG_ENABLED = true;
    private static ConnectivityManager connectivityManager;
    private static volatile NetworkStatusManager instance;

    private NetworkStatusManager() {
    }

    public static NetworkStatusManager getInstance(Context context) {
        if (instance == null) {
            synchronized (NetworkStatusManager.class) {
                if (instance == null) {
                    instance = new NetworkStatusManager();
                    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                }
            }
        }
        return instance;
    }

    public boolean isConnectionAvailable() {
        boolean isConnectionAvailable = false;
        try {
            if (connectivityManager != null) {
                NetworkInfo mActive = connectivityManager.getActiveNetworkInfo();
                if (mActive != null) {
                    if (mActive.isConnected())
                        isConnectionAvailable = true;
                    else
                        isConnectionAvailable = false;
                } else {
                    isConnectionAvailable = false;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return isConnectionAvailable;
    }
}