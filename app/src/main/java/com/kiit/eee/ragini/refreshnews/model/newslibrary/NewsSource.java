package com.kiit.eee.ragini.refreshnews.model.newslibrary;

import com.google.gson.annotations.SerializedName;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;

/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsSource implements IAppModel {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDesscription;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("category")
    private String mCategory;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("country")
    private String mCountry;

    public String getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmDesscription() {
        return mDesscription;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public String getmCountry() {
        return mCountry;
    }
}
