package com.kiit.eee.ragini.refreshnews.model.newslibrary;

import com.google.gson.annotations.SerializedName;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;

import java.util.List;

/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsSourceRoot implements IAppModel
{
    @SerializedName("status")
    private String status;
    @SerializedName("sources")
    private List<NewsSource> mSourceList;

    public String getStatus() {
        return status;
    }

    public List<NewsSource> getmSourceList() {
        return mSourceList;
    }
}
