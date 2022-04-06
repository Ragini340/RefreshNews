package com.kiit.eee.ragini.refreshnews.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Locale;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class LocaleUtils {
   private static final String TAG = "LocaleUtils";
    public static String getCountryName(Activity activity){
        String CountryID = "";
        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        Log.i(TAG, "CountryID" + CountryID);
        Locale loc = new Locale("", CountryID);
        String country = loc.getDisplayCountry();
        Log.e(TAG, "getCountryName: "+country );
        return country;
    }

    public static String getCountryCodeOnly(Activity activity){
        String CountryID = "";
        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        Log.e(TAG, "getCountryCodeOnly: "+CountryID );
        return CountryID;
    }

}
