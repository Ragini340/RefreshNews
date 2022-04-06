package com.kiit.eee.ragini.refreshnews.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kiit.eee.ragini.refreshnews.network.NetworkStatus;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static NetworkStatusListener networkStatusListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (networkStatusListener != null) {
            networkStatusListener.onNetworkConnectionChanged(NetworkStatus.isNetwrokAvailable(context));
        }
    }

    public interface NetworkStatusListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
