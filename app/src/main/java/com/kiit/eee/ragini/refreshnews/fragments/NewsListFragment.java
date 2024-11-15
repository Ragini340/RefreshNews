package com.kiit.eee.ragini.refreshnews.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;

import com.kiit.eee.ragini.refreshnews.adapter.NewsRecyclerViewAdapter;
import com.kiit.eee.ragini.refreshnews.adapter.NewsSourceRecyclerViewAdapter;
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
import com.kiit.eee.ragini.refreshnews.model.Source;
import com.kiit.eee.ragini.refreshnews.model.newslibrary.NewsSource;
import com.kiit.eee.ragini.refreshnews.model.newslibrary.NewsSourceRoot;
import com.kiit.eee.ragini.refreshnews.network.NetworkStatus;
import com.kiit.eee.ragini.refreshnews.sharedPreference.SessionManager;
import com.kiit.eee.ragini.refreshnews.sharedPreference.SessionMangarHelper;
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
    private List<Article> mSearchList;
    private List<NewsSource> mSourceList;
    private ICommunicator mICommunicator;
    private boolean isLibraryLoaded;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mCtx = context;
        mICommunicator = (ICommunicator) context;
        Log.i(TAG, "onAttach: this" + this);
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
        Log.i(TAG, "onCreate: " + this);
        if (getArguments() != null) {
            mTabName = getArguments().getString(ARG_TABNAME);
            Log.i(TAG, "onCreate: mTabName" + mTabName);
        }
    }

    private void makeNewsApiCall(String baseURL,Class rootClass, int whichFeatureApi) {
        switch (mTabName.toLowerCase()) {
            case NewsURL.NEWS_TYPE_COUNTRY:
                makeApiCall(baseURL, getParamsForCountry(), NewsURL.NEWS_TYPE_COUNTRY, whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_BUSINESS:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_BUSINESS), NewsURL.NEWS_TYPE_BUSINESS,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_ENTERTAINMENT:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_ENTERTAINMENT), NewsURL.NEWS_TYPE_ENTERTAINMENT,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_GENERAL:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_GENERAL), NewsURL.NEWS_TYPE_GENERAL,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_HEALTH:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_HEALTH), NewsURL.NEWS_TYPE_HEALTH,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_SCIENCE:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_SCIENCE), NewsURL.NEWS_TYPE_SCIENCE,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_SPORTS:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_SPORTS), NewsURL.NEWS_TYPE_SPORTS,whichFeatureApi, rootClass);
                break;
            case NewsURL.NEWS_TYPE_TECHNOLOGY:
                makeApiCall(baseURL, getParamsForCategory(NewsURL.NEWS_TYPE_TECHNOLOGY), NewsURL.NEWS_TYPE_TECHNOLOGY,whichFeatureApi, rootClass);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: " + this);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        mFragNewsListBinding = FragmentNewsListBinding.inflate(inflater, container, false);
        View view = mFragNewsListBinding.getRoot();
        // Enable java script to webView
        WebSettings webSettings = mFragNewsListBinding.newsWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mFragNewsListBinding.newsWeb.setWebViewClient(new NewsWebViewClient(NewsListFragment.this));
     //   isLibraryLoaded = SessionMangarHelper.getInstance(mCtx).getPrefBoolean(SessionManager.PREF_KEY_IS_LIBRARY_LOADED);
        if(isLibraryLoaded){
            makeNewsApiCall(NewsURL.NEWS_SOURCES, NewsSourceRoot.class, NewsURL.SOURCE_API_FEATURE);
        } else{
            makeNewsApiCall(NewsURL.NEWS_BASED_TOP_HEADLINE, NewsRoot.class, NewsURL.NEWS_API_FEATURE);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mFragNewsListBinding = null;
        isLibraryLoaded = false;
       // SessionMangarHelper.getInstance(mCtx).setPrefBooleanData(SessionManager.PREF_KEY_IS_LIBRARY_LOADED,isLibraryLoaded);
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
    private void makeApiCall(String url, RequestParams params, String whichApiCall, int whichFeatureApiCall,Class rootClass) {
        if (URLUtil.isValidUrl(url)) {
            WebServiceController webServiceController = new WebServiceController(mCtx, this);
            webServiceController.getRequestWithParams(url, params, whichApiCall,whichFeatureApiCall, rootClass);
        }
    }

    @Override
    public void showProgress() {
        if(mFragNewsListBinding.progressBar != null) {
            mFragNewsListBinding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if(mFragNewsListBinding.progressBar != null) {
            mFragNewsListBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDialog(String msg, String whichApiCall) {

    }

    @Override
    public void getParsedResponse(IAppModel model, String whichUrl,int whichFeaturApiResponse) {
        Log.i(TAG, "getParsedResponse: which feature" + whichFeaturApiResponse);
        if(whichFeaturApiResponse == NewsURL.NEWS_API_FEATURE) {
            NewsRoot newsRoot = (NewsRoot) model;
            if (newsRoot instanceof IAppModel) {
                mList = newsRoot.getArticleList();
                Log.i(TAG, "getParsedResponse:news " + newsRoot.getArticleList());
                Log.i(TAG, "getParsedResponse:mList " + newsRoot.getArticleList());
                setAdapter(mList);
            }
        } else if(whichFeaturApiResponse == NewsURL.SEARCH_API_FEATURE){

            NewsRoot newsRoot = (NewsRoot) model;
            if (newsRoot instanceof IAppModel) {
                mSearchList = newsRoot.getArticleList();
                Log.i(TAG, "getParsedResponse:news " + newsRoot.getArticleList());
                Log.i(TAG, "getParsedResponse:mList " +mSearchList.size());
                setAdapter(mSearchList);
            }
        }else if(whichFeaturApiResponse == NewsURL.SOURCE_API_FEATURE){
            NewsSourceRoot newsSourceRoot = (NewsSourceRoot) model;
            if(newsSourceRoot instanceof  IAppModel){
                mSourceList = newsSourceRoot.getmSourceList();
                setNewsSourceAdapter();
            }
            Log.i(TAG, "getParsedResponse: source api" + mSourceList);

        }
    }

    private void setNewsSourceAdapter() {
        if (mFragNewsListBinding.recyclerView != null && mSourceList != null && mSourceList.size() > 0) {
            mFragNewsListBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragNewsListBinding.newsWeb.setVisibility(View.GONE);

            if (mFragNewsListBinding.recyclerView.getAdapter() == null || (mFragNewsListBinding.recyclerView.getAdapter() != null && !(mFragNewsListBinding.recyclerView.getAdapter() instanceof NewsSourceRecyclerViewAdapter))) {
                mFragNewsListBinding.recyclerView.setAdapter(null);
                NewsSourceRecyclerViewAdapter adapter = new NewsSourceRecyclerViewAdapter(NewsListFragment.this, mCtx, mSourceList);
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

    private void setAdapter(List<Article> list) {

        if (mFragNewsListBinding.recyclerView != null && list != null && list.size() > 0) {
            mFragNewsListBinding.recyclerView.setVisibility(View.VISIBLE);
            mFragNewsListBinding.newsWeb.setVisibility(View.GONE);
            Log.i(TAG, "setAdapter: mFragNewsListBinding.recyclerView.getAdapter(" + mFragNewsListBinding.recyclerView.getAdapter());
            if (mFragNewsListBinding.recyclerView.getAdapter() == null || (mFragNewsListBinding.recyclerView.getAdapter() != null && !(mFragNewsListBinding.recyclerView.getAdapter() instanceof NewsRecyclerViewAdapter))) {
                Log.i(TAG, "setAdapter: if**********");

                // mFragNewsListBinding.recyclerView.invalidate();
                NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(NewsListFragment.this, mCtx, list);
                LinearLayoutManager lytManager = new LinearLayoutManager(mCtx);
                lytManager.setOrientation(LinearLayoutManager.VERTICAL);
                mFragNewsListBinding.recyclerView.setLayoutManager(lytManager);
                mFragNewsListBinding.recyclerView.addItemDecoration(new HorizentalItemsDividerDecorator(mCtx));
                mFragNewsListBinding.recyclerView.setAdapter(adapter);
            } else {
                Log.i(TAG, "setAdapter: else");
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

    public void onClickOfLibrary(boolean isLibraryNeedToLoad, int selectedPos){
        Log.i(TAG, "onClickOfLibrary: isLibraryNeedToLoad" + isLibraryNeedToLoad + "Position" + selectedPos);
        isLibraryLoaded = isLibraryNeedToLoad;
      //  SessionMangarHelper.getInstance(mCtx).setPrefBooleanData(SessionManager.PREF_KEY_IS_LIBRARY_LOADED,isLibraryNeedToLoad);
        if(isLibraryNeedToLoad){
            makeNewsApiCall(NewsURL.NEWS_SOURCES, NewsSourceRoot.class, NewsURL.SOURCE_API_FEATURE);
        }
    }

    public void onClickOfSearch(String searchKey){
        makeSearchApiCall(searchKey);
    }
    private  void makeSearchApiCall(String searchKey){
        makeNewsApiCall(NewsURL.NEWS_SEARCH, NewsRoot.class, NewsURL.SOURCE_API_FEATURE);
        makeApiCall(NewsURL.NEWS_SEARCH, getParamsSearch(searchKey), NewsURL.NEWS_SEARCH_API, NewsURL.SEARCH_API_FEATURE, NewsRoot.class);
    }

    private RequestParams getParamsSearch(String searchKey) {
        RequestParams params = null;
        if (mCtx != null) {
            params = new RequestParams();
            params.put("q", searchKey);
            params.put("apiKey", Constants.API_KEY);
            params.put("sortBy", "popularity");
        }
        Log.i(TAG, "getParamsForCountry: params" + params);
        return params;
    }

}

