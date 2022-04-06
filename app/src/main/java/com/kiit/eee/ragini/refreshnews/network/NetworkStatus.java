package com.kiit.eee.ragini.refreshnews.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 1807340_RAGINI on 05,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NetworkStatus {
    private static String TAG = "Hello";
    public static boolean isWiFi;
    public static boolean isMobileData;

    public static boolean isNetwrokAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            isMobileData = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        boolean isConnected = activeNetwork != null &&
                (activeNetwork.isConnected() || (isWiFi || isMobileData));
        return isConnected;

    }


}
