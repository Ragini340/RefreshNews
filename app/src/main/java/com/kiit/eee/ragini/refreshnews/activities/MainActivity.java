package com.kiit.eee.ragini.refreshnews.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.adapter.NewsListPagerAdapter;
import com.kiit.eee.ragini.refreshnews.databinding.ActivityMainBinding;
import com.kiit.eee.ragini.refreshnews.databinding.AppToolbarBinding;
import com.kiit.eee.ragini.refreshnews.utils.LocaleUtils;
import com.kiit.eee.ragini.refreshnews.utils.OSUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class MainActivity extends AppCompatActivity implements ProviderInstaller.ProviderInstallListener{
    private static final String TAG = "MainActivity";

    private ActivityMainBinding mAactivityMainBinding;
    private NewsListPagerAdapter mNewsPagerAdapter;
    private String[] mTabArray;

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
        mTabArray[0] = LocaleUtils.getCountryName(this);

        for (String tabName: mTabArray) {
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

                        if(position == 0) {
                            mAactivityMainBinding.viewPager.setCurrentItem(position, false);
                        }
                        tab.setText(mTabArray[position]);
                       }
            ).attach();
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
}