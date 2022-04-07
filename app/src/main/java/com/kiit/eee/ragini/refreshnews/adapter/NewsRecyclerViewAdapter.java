package com.kiit.eee.ragini.refreshnews.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiit.eee.ragini.refreshnews.databinding.NewsItemListAdapterBinding;
import com.kiit.eee.ragini.refreshnews.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 1807340_RAGINI on 07,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private Context mCtx;
    private List<Article> mList;

    public NewsRecyclerViewAdapter(Context mCtx, List<Article> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(NewsItemListAdapterBinding.inflate(LayoutInflater.from(mCtx),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
      Article article = mList.get(position);
      if(article != null){
          if(URLUtil.isValidUrl(article.getUrlToImage())){
              Picasso.get().load(article.getUrlToImage()).into(holder.mBinding.imgNews);
          }

          if(!TextUtils.isEmpty(article.getAuthor())){
              holder.mBinding.txtNewsSource.setText(article.getAuthor());
          }

          if(!TextUtils.isEmpty(article.getTitle())){
              holder.mBinding.txtNewsTitle.setText(article.getTitle());
          }

          if(article.getPublishedAt() != null){
              holder.mBinding.txtNewsTime.setText(article.getPublishedAt().toString());
          }




      }
    }

    @Override
    public int getItemCount() {
        if(mList != null && mList.size() > 0)
            return mList.size();
        return 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        private NewsItemListAdapterBinding mBinding;
        public NewsViewHolder(@NonNull NewsItemListAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
