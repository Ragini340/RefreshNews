package com.kiit.eee.ragini.refreshnews.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiit.eee.ragini.refreshnews.databinding.NewsItemListAdapterBinding;
import com.kiit.eee.ragini.refreshnews.databinding.SourceItemListAdapterBinding;
import com.kiit.eee.ragini.refreshnews.fragments.NewsListFragment;
import com.kiit.eee.ragini.refreshnews.interfaces.IListCommunicator;
import com.kiit.eee.ragini.refreshnews.model.Article;
import com.kiit.eee.ragini.refreshnews.model.newslibrary.NewsSource;
import com.kiit.eee.ragini.refreshnews.model.newslibrary.NewsSourceRoot;

import java.util.List;

/**
 * Created by 1807340_RAGINI on 11,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsSourceRecyclerViewAdapter extends RecyclerView.Adapter<NewsSourceRecyclerViewAdapter.NewsSourceViewHolder>  {

    private Context mCtx;
    private List<NewsSource> mList;
    private IListCommunicator mICommunicator;
    public NewsSourceRecyclerViewAdapter(NewsListFragment newsListFragment, Context mCtx, List<NewsSource> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        mICommunicator = (IListCommunicator) newsListFragment;
    }

    private static String TAG = "NewsSourceRecyclerViewAdapter";

    @NonNull
    @Override
    public NewsSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsSourceViewHolder(SourceItemListAdapterBinding.inflate(LayoutInflater.from(mCtx),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsSourceViewHolder holder, int position) {
       NewsSource newsSource = mList.get(position);
       if(newsSource != null){
           if(!TextUtils.isEmpty(newsSource.getmName())){
               holder.mBinding.txtNewsSourceName.setVisibility(View.VISIBLE);
               holder.mBinding.txtNewsSourceName.setText(newsSource.getmName());
           } else {
               holder.mBinding.txtNewsSourceName.setVisibility(View.GONE);
           }

           if(!TextUtils.isEmpty(newsSource.getmDesscription())){
               holder.mBinding.txtNewsDescription.setVisibility(View.VISIBLE);
               holder.mBinding.txtNewsDescription.setText(newsSource.getmDesscription());
           } else {
               holder.mBinding.txtNewsDescription.setVisibility(View.GONE);
           }

           if(!TextUtils.isEmpty(newsSource.getmUrl())){
               holder.mBinding.txtNewsSourceUrl.setVisibility(View.VISIBLE);
               holder.mBinding.txtNewsSourceUrl.setText(newsSource.getmUrl());
           } else {
               holder.mBinding.txtNewsSourceUrl.setVisibility(View.GONE);
           }

       }

    }

    @Override
    public int getItemCount() {
        if(mList != null && mList.size() > 0)
            return mList.size();
        return 0;
    }

    class NewsSourceViewHolder extends RecyclerView.ViewHolder {
        private SourceItemListAdapterBinding mBinding;

        public NewsSourceViewHolder(@NonNull SourceItemListAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.btnLoadSource.setOnClickListener(this::viewNewsSourcePortal);
        }

        public void viewNewsSourcePortal(View view) {
            Log.i(TAG, "viewNewsSourcePortal: ");
            NewsSource newsSource = mList.get(getBindingAdapterPosition());
            if(newsSource != null) {
                if(newsSource.getmUrl() != null && !TextUtils.isEmpty(newsSource.getmUrl())){
                    mICommunicator.communicate(newsSource.getmUrl(), "");
                }
            }
        }
    }
}
