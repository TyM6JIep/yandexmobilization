package com.arsanima.yandexmobilization.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by YuriF on 16.04.16.
 */
public class NetworkHelper {
    private static NetworkHelper instance;
    private static Context context;

    public static NetworkHelper getInstance() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized NetworkHelper getSync() {
        if (instance == null) instance = new NetworkHelper();
        return instance;
    }

    public NetworkHelper() {}

    public void initialize(Context context) {
        NetworkHelper.context = context;
    }

    // проверяем наличие интернета
    public boolean isNetworkConnect() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
