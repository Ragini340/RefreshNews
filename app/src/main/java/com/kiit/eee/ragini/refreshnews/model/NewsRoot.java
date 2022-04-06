package com.kiit.eee.ragini.refreshnews.model;

import com.google.gson.annotations.SerializedName;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;

import java.util.ArrayList;

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
    private ArrayList<Article> articleList;

    public String getStatus() {
        return status;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public ArrayList<Article> getArticleList() {
        return articleList;
    }
}
