package com.kiit.eee.ragini.refreshnews.interfaces;

import org.json.JSONObject;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public interface IWebInterface {
   // public void getJsonResponse(JSONObject jsonObject, String whichUrl);
    public void   getParsedResponse(IAppModel model,int whichUrl);
}
