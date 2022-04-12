package com.kiit.eee.ragini.refreshnews.sharedPreference;

import android.content.Context;

/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class SessionMangarHelper {
    public static SessionManager mSessionManagerInstance;

    public SessionMangarHelper() {
    }

    public static SessionManager getInstance(Context context) {
        if (mSessionManagerInstance == null) {
            mSessionManagerInstance = new SessionManager(context);
        }
        return mSessionManagerInstance;
    }

}
