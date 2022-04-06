package com.kiit.eee.ragini.refreshnews.model;

import com.google.gson.annotations.SerializedName;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class Source implements IAppModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
