package com.kiit.eee.ragini.refreshnews.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kiit.eee.ragini.refreshnews.fragments.NewsListFragment;
import com.kiit.eee.ragini.refreshnews.interfaces.IWebViewClientCommunicator;


/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsWebViewClient extends WebViewClient {
    private static String TAG = "NewsWebViewClient";
    private IWebViewClientCommunicator mIWebViewClientCommunicator;

    public NewsWebViewClient(NewsListFragment newsListFragment) {
        this.mIWebViewClientCommunicator = (IWebViewClientCommunicator) newsListFragment;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if(request.getUrl()  != null) {
            view.loadUrl(request.getUrl().toString());
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i(TAG, "onPageStarted: url" + url);
        mIWebViewClientCommunicator.onPageStarted(true);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i(TAG, "onPageFinished: url" + url);
        mIWebViewClientCommunicator.onPageFinished(true);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.i(TAG, "onReceivedError: error");
    }
}
