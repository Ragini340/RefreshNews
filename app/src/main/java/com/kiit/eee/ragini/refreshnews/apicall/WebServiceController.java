package com.kiit.eee.ragini.refreshnews.apicall;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;
import com.kiit.eee.ragini.refreshnews.interfaces.IDialogUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IProgressBarUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IWebInterface;
import com.kiit.eee.ragini.refreshnews.network.NetworkStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class WebServiceController {

    private static String TAG = "WebServiceController";
    private IDialogUtilityInterface mDialogUtilityInterface;
    private IProgressBarUtilityInterface mProgressBarUtilityInterface;
    private IWebInterface mWebInterface;
    private Context mCtx;

    public WebServiceController(Context context, Object obj) {
        this.mCtx = context;
        this.mWebInterface = (IWebInterface) obj;
        this.mDialogUtilityInterface = (IDialogUtilityInterface) obj;
        this.mProgressBarUtilityInterface = (IProgressBarUtilityInterface) obj;
    }

    public void getRequestWithParams(String url, RequestParams requestParams, final int flag, Class modelClass) {
        Log.i(TAG, "getRequestWithParams: url" + url);
        if (NetworkStatus.isNetwrokAvailable(mCtx)) {
            mProgressBarUtilityInterface.showProgress();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);
            client.get(url, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = "";
                    try {
                        response = new String(responseBody, "UTF-8");
                        Log.i(TAG, "onSuccess: response" + response);
                        Gson gson = new GsonBuilder().create();
                        // Define Response class to correspond to the JSON response returned
                        IAppModel iAppModel = (IAppModel) gson.fromJson(response, modelClass);
                        if (mWebInterface != null && iAppModel != null) {
                            mWebInterface.getParsedResponse(iAppModel, flag);
                        }
                        mProgressBarUtilityInterface.hideProgress();
                    } catch (UnsupportedEncodingException e) {
                        Log.i(TAG, "Error while parsing success response: " + e);
                        mProgressBarUtilityInterface.hideProgress();
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                      Throwable arg3) {
                    Log.i(TAG, "onFailure occurs on api call " + arg3);
                    mProgressBarUtilityInterface.hideProgress();
                    mDialogUtilityInterface.showDialog(mCtx.getString(R.string.server_error), flag);
                }

            });
        } else {
            mProgressBarUtilityInterface.hideProgress();
            mDialogUtilityInterface.showDialog(mCtx.getString(R.string.netwrokr_error), flag);
        }
    }
}
