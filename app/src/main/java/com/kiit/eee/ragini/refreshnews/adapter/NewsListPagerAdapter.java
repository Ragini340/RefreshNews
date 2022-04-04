package com.kiit.eee.ragini.refreshnews.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.fragments.NewsListFragment;

/**
 * Created by 1807340_RAGINI on 04,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class NewsListPagerAdapter extends FragmentStateAdapter {

    private Context mContext;

    public NewsListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mContext = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NewsListFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getStringArray(R.array.tab_name_array).length;
    }
}
