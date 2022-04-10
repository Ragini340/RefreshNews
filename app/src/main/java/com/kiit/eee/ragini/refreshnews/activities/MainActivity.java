package com.kiit.eee.ragini.refreshnews.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.adapter.NewsListPagerAdapter;
import com.kiit.eee.ragini.refreshnews.databinding.ActivityMainBinding;
import com.kiit.eee.ragini.refreshnews.databinding.AppToolbarBinding;
import com.kiit.eee.ragini.refreshnews.interfaces.ICommunicator;
import com.kiit.eee.ragini.refreshnews.utils.LocaleUtils;
import com.kiit.eee.ragini.refreshnews.utils.OSUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class MainActivity extends AppCompatActivity implements ProviderInstaller.ProviderInstallListener, ICommunicator {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding mAactivityMainBinding;
    private NewsListPagerAdapter mNewsPagerAdapter;
    private String[] mTabArray;
    private int mSelectedPagePos  = -1;

    private static final int ERROR_DIALOG_REQUEST_CODE = 1;

    private boolean retryProviderInstall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        ProviderInstaller.installIfNeededAsync(this, this);
        checkTls();
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
        // Add tab name for current country news
        mTabArray = getResources().getStringArray(R.array.tab_name_array);
       // mTabArray[0] = LocaleUtils.getCountryName(this);

        for (String tabName: mTabArray) {
            Log.i(TAG, "onCreate: tabName" + tabName);
            tabLayout.addTab(tabLayout.newTab());
        }

        // View pager set up
        if(mNewsPagerAdapter == null) {
            mNewsPagerAdapter = new NewsListPagerAdapter(this);
            mAactivityMainBinding.viewPager.setAdapter(mNewsPagerAdapter);
            mAactivityMainBinding.viewPager.setOffscreenPageLimit(2);
            new TabLayoutMediator(tabLayout, mAactivityMainBinding.viewPager,
                    (tab, position) -> {

                        if(position == 0) {
                            tab.setText(LocaleUtils.getCountryName(this));
                            mAactivityMainBinding.viewPager.setCurrentItem(position, false);
                        } else {
                            tab.setText(mTabArray[position]);
                        }
                       }
            ).attach();

            mAactivityMainBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    Log.i(TAG, "onPageScrolled: position" + position);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.i(TAG, "onPageSelected:position " + position);
                    mSelectedPagePos = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    Log.i(TAG, "onPageScrollStateChanged: state" + state);
                }
            });

        }
    }

    protected void checkTls() {
      //  if (android.os.Build.VERSION.SDK_INT < 21) {
            try {
                ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
                    @Override
                    public void onProviderInstalled() {
                        SSLContext sslContext = null;
                        try {
                            sslContext = SSLContext.getInstance("TLSv1.2");
                            sslContext.init(null, null, null);
                            SSLEngine engine = sslContext.createSSLEngine();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onProviderInstallFailed(int i, Intent intent) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  }
    }

    @Override
    public void onProviderInstalled() {
        Log.i(TAG, "onProviderInstalled: Provider is secure app can make secure network call");
    }

    @Override
    public void onProviderInstallFailed(int errorCode, @Nullable Intent intent) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        if (availability.isUserResolvableError(errorCode)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            availability.showErrorDialogFragment(
                    this,
                    errorCode,
                    ERROR_DIALOG_REQUEST_CODE,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // The user chose not to take the recovery action
                           // onProviderInstallerNotAvailable();
                        }
                    });
        } else {
            // Google Play services is not available.
        //    onProviderInstallerNotAvailable();
        }
    }

    @Override
    public void communicate(boolean isStatus) {
        Log.i(TAG, "communicate: isStatus" + isStatus);
        if(isStatus) {
            mAactivityMainBinding.tabLyt.setVisibility(View.GONE);
            mAactivityMainBinding.lytToolbar.title.setVisibility(View.GONE);
            mAactivityMainBinding.lytToolbar.searchView.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: " + item.getItemId());
        Log.i(TAG, "onOptionsItemSelected: " + android.R.id.home);
        switch (item.getItemId()) {
            case android.R.id.home:
                mAactivityMainBinding.tabLyt.setVisibility(View.VISIBLE);
                mAactivityMainBinding.lytToolbar.title.setVisibility(View.VISIBLE);
                mAactivityMainBinding.lytToolbar.searchView.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: ");
        FragmentManager fm = getSupportFragmentManager();
        Log.i(TAG, "onBackPressed: getBackStackEntryCount" + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3500);
        }
    }

}