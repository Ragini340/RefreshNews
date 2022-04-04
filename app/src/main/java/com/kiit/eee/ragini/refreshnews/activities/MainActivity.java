package com.kiit.eee.ragini.refreshnews.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.adapter.NewsListPagerAdapter;
import com.kiit.eee.ragini.refreshnews.databinding.ActivityMainBinding;
import com.kiit.eee.ragini.refreshnews.databinding.AppToolbarBinding;
import com.kiit.eee.ragini.refreshnews.utils.OSUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mAactivityMainBinding;
    private NewsListPagerAdapter mNewsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        OSUtils.setStatusBarTransparent(this);
        mAactivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = mAactivityMainBinding.getRoot();
        setContentView(rootView);

        // Set toolbar
        Toolbar toolbar = mAactivityMainBinding.lytToolbar.getRoot();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set tab layout
        TabLayout tabLayout = mAactivityMainBinding.tabLyt;
        for (String tabName:getResources().getStringArray(R.array.tab_name_array)) {
            Log.i(TAG, "onCreate: tabName" + tabName);
            tabLayout.addTab(tabLayout.newTab().setText(tabName));
        }

        // View pager set up
        if(mNewsPagerAdapter == null) {
            mNewsPagerAdapter = new NewsListPagerAdapter(this);
            mAactivityMainBinding.viewPager.setAdapter(mNewsPagerAdapter);
            mAactivityMainBinding.viewPager.setOffscreenPageLimit(2);
            new TabLayoutMediator(tabLayout, mAactivityMainBinding.viewPager,
                    (tab, position) -> {
                        mAactivityMainBinding.viewPager.setCurrentItem(position,true);
                        tab.setText(getResources().getStringArray(R.array.tab_name_array)[position]);
                       }
            ).attach();
        }
    }
}