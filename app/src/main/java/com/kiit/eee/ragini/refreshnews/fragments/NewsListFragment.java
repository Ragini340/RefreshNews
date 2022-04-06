package com.kiit.eee.ragini.refreshnews.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.kiit.eee.ragini.refreshnews.apicall.WebServiceController;
import com.kiit.eee.ragini.refreshnews.databinding.FragmentNewsListBinding;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;
import com.kiit.eee.ragini.refreshnews.interfaces.IDialogUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IProgressBarUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IWebInterface;
import com.kiit.eee.ragini.refreshnews.model.NewsRoot;
import com.kiit.eee.ragini.refreshnews.utils.Constants;
import com.kiit.eee.ragini.refreshnews.utils.LocaleUtils;
import com.kiit.eee.ragini.refreshnews.utils.NewsURL;
import com.loopj.android.http.RequestParams;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements IProgressBarUtilityInterface, IDialogUtilityInterface , IWebInterface {

    private static final String TAG = "NewsListFragment";
    private static final String ARG_TABNAME = "arg_tabname";

    private Context mCtx;

    private String mTabName;

    private FragmentNewsListBinding mFragNewsListBinding;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mCtx = context;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewsListFragment.
     */
    public static NewsListFragment newInstance(String tabname) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TABNAME, tabname);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTabName = getArguments().getString(ARG_TABNAME);
            Log.i(TAG, "onCreate: mTabName" + mTabName);
        }
    }

    private void makeNewsApiCall() {
        switch (mTabName.toLowerCase()) {
            case NewsURL.NEWS_TYPE_COUNTRY:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCountry(), NewsURL.NEWS_TYPE_COUNTRY);
                break;
            case NewsURL.NEWS_TYPE_BUSINESS:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_BUSINESS), NewsURL.NEWS_TYPE_BUSINESS);
                break;
            case NewsURL.NEWS_TYPE_ENTERTAINMENT:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_ENTERTAINMENT), NewsURL.NEWS_TYPE_ENTERTAINMENT);
                break;
            case NewsURL.NEWS_TYPE_GENERAL:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_GENERAL), NewsURL.NEWS_TYPE_GENERAL);
                break;
            case NewsURL.NEWS_TYPE_HEALTH:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_HEALTH), NewsURL.NEWS_TYPE_HEALTH);
                break;
            case NewsURL.NEWS_TYPE_SCIENCE:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_SCIENCE), NewsURL.NEWS_TYPE_SCIENCE);
                break;
            case NewsURL.NEWS_TYPE_SPORTS:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_SPORTS), NewsURL.NEWS_TYPE_SPORTS);
                break;
            case NewsURL.NEWS_TYPE_TECHNOLOGY:
                makeApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, getParamsForCategory(NewsURL.NEWS_TYPE_TECHNOLOGY), NewsURL.NEWS_TYPE_TECHNOLOGY);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragNewsListBinding = FragmentNewsListBinding.inflate(inflater, container, false);
        View view = mFragNewsListBinding.getRoot();
        makeNewsApiCall();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragNewsListBinding = null;
    }

    private RequestParams getParamsForCountry() {
        RequestParams params = null;
        if (mCtx != null) {
            params = new RequestParams();
            params.put("country", LocaleUtils.getCountryCodeOnly(getActivity()));
            params.put("apiKey", Constants.API_KEY);
        }
        Log.i(TAG, "getParamsForCountry: params" + params);
        return params;
    }

    private RequestParams getParamsForCategory(String categoryName) {
        RequestParams params = null;
        if (mCtx != null) {
            params = new RequestParams();
            params.put("category", categoryName);
            params.put("apiKey", Constants.API_KEY);
        }
        Log.i(TAG, "getParamsForCountry: params" + params);
        return params;
    }
    private void makeApiCall(String url, RequestParams params, String whichApiCall) {
        if (URLUtil.isValidUrl(url)) {
            WebServiceController webServiceController = new WebServiceController(mCtx, this);
            webServiceController.getRequestWithParams(url, params, whichApiCall, NewsRoot.class);
        }
    }

    @Override
    public void showProgress() {
        mFragNewsListBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mFragNewsListBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showDialog(String msg, String whichApiCall) {

    }

    @Override
    public void getParsedResponse(IAppModel model, String whichUrl) {
        NewsRoot newsRoot = (NewsRoot) model;
        if(newsRoot instanceof  IAppModel){
            Log.i(TAG, "getParsedResponse: " + newsRoot.getArticleList());
        }
    }
}

