package com.kiit.eee.ragini.refreshnews.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kiit.eee.ragini.refreshnews.R;
import com.kiit.eee.ragini.refreshnews.adapter.NewsListPagerAdapter;
import com.kiit.eee.ragini.refreshnews.databinding.ActivityMainBinding;
import com.kiit.eee.ragini.refreshnews.databinding.AppToolbarBinding;
import com.kiit.eee.ragini.refreshnews.fragments.NewsListFragment;
import com.kiit.eee.ragini.refreshnews.interfaces.ICommunicator;
import com.kiit.eee.ragini.refreshnews.sharedPreference.SessionManager;
import com.kiit.eee.ragini.refreshnews.sharedPreference.SessionMangarHelper;
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
    private String mFullCoverageUrl, mFullCoverageStoryTitle;
    private static final int ERROR_DIALOG_REQUEST_CODE = 1;
    private boolean isLibraryLoaded;
    private static final String ARG_TABNAME = "arg_tabname";

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

        //SearchView
        mAactivityMainBinding.lytToolbar.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: search" + view);
                mAactivityMainBinding.lytToolbar.title.setVisibility(View.GONE);
                mAactivityMainBinding.tabLyt.setVisibility(View.GONE);
                mAactivityMainBinding.viewPager.setVisibility(View.GONE);
                mAactivityMainBinding.appBottomBar.setVisibility(View.GONE);
                mAactivityMainBinding.container.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewsListFragment fragment = new NewsListFragment();
                Bundle args = new Bundle();
                args.putString(ARG_TABNAME, "Search");
                fragment.setArguments(args);
                fragmentTransaction.add(R.id.container, fragment, "SearchFragment");
                fragmentTransaction.commit();

            }
        });
        mAactivityMainBinding.lytToolbar.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: query" + query);


                FragmentManager fragmentManager = getSupportFragmentManager();
                NewsListFragment newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("SearchFragment");
              //  Log.i(TAG, "showNewsLibrary: Id of Fragment" + fragmentManager.findFragmentByTag("f" + newsListPagerAdapter.getItemId(mSelectedPagePos)));
                if(!TextUtils.isEmpty(query)) {
                    newsListFragment.onClickOfSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: newText" + newText);
                return false;
            }
        });

        // Register click listener on share
        mAactivityMainBinding.lytToolbar.imgShare.setOnClickListener(this::share);

        //Set tab layout
        TabLayout tabLayout = mAactivityMainBinding.tabLyt;
        // Add tab name for current country news
        mTabArray = getResources().getStringArray(R.array.tab_name_array);
       // mTabArray[0] = LocaleUtils.getCountryName(this);

        for (String tabName: mTabArray) {
            Log.i(TAG, "onCreate: tabName" + tabName);
            tabLayout.addTab(tabLayout.newTab());
        }

        //Set Bottom Appbar
       // setSupportActionBar(mAactivityMainBinding.appBottomBar);
        mAactivityMainBinding.txtLibrary.setOnClickListener(this::showNewsLibrary);
        mAactivityMainBinding.txtHelp.setOnClickListener(this::help);
        mAactivityMainBinding.txtAboutUs.setOnClickListener(this::aboutUs);

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

    public void share(View view){
        Log.i(TAG, "share: " + mFullCoverageUrl);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mFullCoverageUrl);
        sendIntent.setType("*/*");
        sendIntent.putExtra(Intent.EXTRA_TITLE, mFullCoverageStoryTitle);
        startActivity(Intent.createChooser(sendIntent, mFullCoverageStoryTitle));
    }

    public void showNewsLibrary(View view) {
        Log.i(TAG, "showNewsSource: " + view);
        if(mAactivityMainBinding.viewPager != null && mAactivityMainBinding.viewPager.getAdapter() != null){
            NewsListPagerAdapter newsListPagerAdapter = (NewsListPagerAdapter) mAactivityMainBinding.viewPager.getAdapter();
            FragmentManager fragmentManager = getSupportFragmentManager();
            NewsListFragment newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("f" + newsListPagerAdapter.getItemId(mSelectedPagePos));
            Log.i(TAG, "showNewsLibrary: Id of Fragment" + fragmentManager.findFragmentByTag("f" + newsListPagerAdapter.getItemId(mSelectedPagePos)));
            isLibraryLoaded = true;
            mAactivityMainBinding.lytToolbar.title.setText(getString(R.string.btn_sources_protal_list));
            newsListFragment.onClickOfLibrary(isLibraryLoaded,mSelectedPagePos);
        }
    }


    public void help(View view) {
        Log.i(TAG, "help: " + view);
        Log.i(TAG, "aboutUs: " + view);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // bottomSheetDialog.s(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetDialog.setContentView(R.layout.help_us_bottom_sheet);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
    }

    public void aboutUs(View view) {
        Log.i(TAG, "aboutUs: " + view);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
       // bottomSheetDialog.s(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetDialog.setContentView(R.layout.about_us_bottomsheet);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
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
    public void communicate(boolean isStatus, String url, String extraData) {
        Log.i(TAG, "communicate: isStatus" + isStatus);
        mFullCoverageUrl = url;
        mFullCoverageStoryTitle = extraData;
        if(isStatus) {
            mAactivityMainBinding.tabLyt.setVisibility(View.GONE);
            mAactivityMainBinding.lytToolbar.searchView.setVisibility(View.GONE);
            mAactivityMainBinding.appBottomBar.setVisibility(View.GONE);
            if(isLibraryLoaded){
                mAactivityMainBinding.lytToolbar.title.setText(getString(R.string.btn_sources_protal_list));
                mAactivityMainBinding.lytToolbar.imgShare.setVisibility(View.GONE);

            } else {
                mAactivityMainBinding.lytToolbar.title.setText(getString(R.string.appbar_full_title));
                mAactivityMainBinding.lytToolbar.imgShare.setVisibility(View.VISIBLE);
            }
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
                if(isLibraryLoaded){
                    mAactivityMainBinding.lytToolbar.title.setText(getString(R.string.btn_sources_protal_list));
                } else{
                    mAactivityMainBinding.lytToolbar.title.setText(getString(R.string.appbar_headline));
                }

                mAactivityMainBinding.lytToolbar.searchView.setVisibility(View.VISIBLE);
                mAactivityMainBinding.lytToolbar.imgShare.setVisibility(View.GONE);
                mAactivityMainBinding.appBottomBar.setVisibility(View.VISIBLE);
                mAactivityMainBinding.container.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                // Remove search fragment
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("SearchFragment");
                Log.i(TAG, "onOptionsItemSelected: Search fragment" + fragment);
                if(fragment != null) {
                    mAactivityMainBinding.lytToolbar.title.setVisibility(View.VISIBLE);
                    mAactivityMainBinding.tabLyt.setVisibility(View.VISIBLE);
                    mAactivityMainBinding.viewPager.setVisibility(View.VISIBLE);
                    mAactivityMainBinding.appBottomBar.setVisibility(View.VISIBLE);
                    mAactivityMainBinding.container.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
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