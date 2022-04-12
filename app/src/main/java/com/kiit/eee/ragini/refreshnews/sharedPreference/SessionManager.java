package com.kiit.eee.ragini.refreshnews.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class SessionManager {
    public static final String PREF_FILE_NAME = "com.kiit.eee.ragini.refreshnews.pref_file";
    private Context mCtx;
    private static String  TAG = "SessionManager";
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Shared Preferences
    private SharedPreferences mSharedPrefFile;
    // Editor for Shared preferences
    private SharedPreferences.Editor mEditor;
    public static final String PREF_KEY_IS_LIBRARY_LOADED = "pref_key_is_library_loaded";



    public SessionManager(Context context) {
        this.mCtx = context;
        mSharedPrefFile = mCtx.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        mEditor = mSharedPrefFile.edit();

    }

    public void setPrefBooleanData(String key, boolean value){
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public SharedPreferences getPrefFile(Context context) {
        mSharedPrefFile = mCtx.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        return mSharedPrefFile;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public void setPrefData(String KEY, String VAL) {
        Log.e(TAG, "setPrefData: "+KEY);
        Log.e(TAG, "setPrefData: "+VAL );
        mEditor.putString(KEY, VAL);
        mEditor.commit();
    }


    public String getPrefData(String KEY) {
        return mSharedPrefFile.getString(KEY, "");
    }
    public boolean getPrefBoolean(String KEY){
       return mSharedPrefFile.getBoolean(KEY, false);
    }

}
