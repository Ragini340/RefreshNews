package com.kiit.eee.ragini.refreshnews.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;

import com.kiit.eee.ragini.refreshnews.adapter.NewsRecyclerViewAdapter;
import com.kiit.eee.ragini.refreshnews.apicall.WebServiceController;
import com.kiit.eee.ragini.refreshnews.databinding.FragmentNewsListBinding;
import com.kiit.eee.ragini.refreshnews.interfaces.IAppModel;
import com.kiit.eee.ragini.refreshnews.interfaces.ICommunicator;
import com.kiit.eee.ragini.refreshnews.interfaces.IListCommunicator;
import com.kiit.eee.ragini.refreshnews.interfaces.IDialogUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IProgressBarUtilityInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IWebInterface;
import com.kiit.eee.ragini.refreshnews.interfaces.IWebViewClientCommunicator;
import com.kiit.eee.ragini.refreshnews.model.Article;
import com.kiit.eee.ragini.refreshnews.model.NewsRoot;
import com.kiit.eee.ragini.refreshnews.network.NetworkStatus;
import com.kiit.eee.ragini.refreshnews.utils.Constants;
import com.kiit.eee.ragini.refreshnews.utils.LocaleUtils;
import com.kiit.eee.ragini.refreshnews.utils.NewsURL;
import com.kiit.eee.ragini.refreshnews.views.HorizentalItemsDividerDecorator;
import com.kiit.eee.ragini.refreshnews.webview.NewsWebViewClient;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements IProgressBarUtilityInterface, IDialogUtilityInterface , IWebInterface, IListCommunicator, IWebViewClientCommunicator {

    private static final String TAG = "NewsListFragment";
    private static final String ARG_TABNAME = "arg_tabname";

    private Context mCtx;

    private String mTabName;

    private FragmentNewsListBinding mFragNewsListBinding;
    private List<Article> mList;
    private ICommunicator mICommunicator;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mCtx = context;
        mICommunicator = (ICommunicator) context;
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
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        mFragNewsListBinding = FragmentNewsListBinding.inflate(inflater, container, false);
        View view = mFragNewsListBinding.getRoot();
        // Enable java script to webView
        WebSettings webSettings = mFragNewsListBinding.newsWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mFragNewsListBinding.newsWeb.setWebViewClient(new NewsWebViewClient(NewsListFragment.this));
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
            mList = newsRoot.getArticleList();
            Log.i(TAG, "getParsedResponse: " + newsRoot.getArticleList());
            setAdapter();

        }
    }

    private void setAdapter() {

        if (mFragNewsListBinding.recyclerView != null && mList != null && mList.size() > 0) {
            mFragNewsListBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragNewsListBinding.newsWeb.setVisibility(View.GONE);
            if (mFragNewsListBinding.recyclerView.getAdapter() == null) {
                NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(NewsListFragment.this, mCtx, mList);
                LinearLayoutManager lytManager = new LinearLayoutManager(mCtx);
                lytManager.setOrientation(LinearLayoutManager.VERTICAL);
                mFragNewsListBinding.recyclerView.addItemDecoration(new HorizentalItemsDividerDecorator(mCtx));
                mFragNewsListBinding.recyclerView.setLayoutManager(lytManager);
                mFragNewsListBinding.recyclerView.setAdapter(adapter);
            } else {
                mFragNewsListBinding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void communicate(String stingData, String extraData) {
        Log.i(TAG, "communicate: String url " + stingData);
        mICommunicator.communicate(true, stingData, extraData);
        mFragNewsListBinding.recyclerView.setVisibility(View.GONE);
        loadFullCoverageNews(stingData);
    }

    private void loadFullCoverageNews(String url){
        mFragNewsListBinding.newsWeb.setVisibility(View.VISIBLE);
        if(NetworkStatus.isNetwrokAvailable(mCtx)) {
            mFragNewsListBinding.newsWeb.loadUrl(url);

        } else{
            // Todo show network dialog
          //  showDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: fragment " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
              //  mFragNewsListBinding.newsWeb.clearCache(true);
              //  mFragNewsListBinding.newsWeb.clearFormData();
                mFragNewsListBinding.newsWeb.clearHistory();
              //  mFragNewsListBinding.newsWeb.destroy();
                mFragNewsListBinding.newsWeb.setVisibility(View.GONE);
                mFragNewsListBinding.recyclerView.setVisibility(View.VISIBLE);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageStarted(boolean isPageStarted) {
        showProgress();
    }

    @Override
    public void onPageFinished(boolean isPageFinished) {
        hideProgress();
    }
}

