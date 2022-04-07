package com.kiit.eee.ragini.refreshnews.model;

import com.google.gson.annotations.SerializedName;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsRoot implements IAppModel {
    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("articles")
    private List<Article> articleList;

    public String getStatus() {
        return status;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public List<Article> getArticleList() {
        return articleList;
    }
}
