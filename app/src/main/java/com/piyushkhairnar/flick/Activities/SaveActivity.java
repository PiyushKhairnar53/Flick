package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.piyushkhairnar.flick.Adapters.FavouritesPagerAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.BottomNavigationViewHelper;

public class SaveActivity extends AppCompatActivity {

    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 2;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        mContext = SaveActivity.this;

        setupBottomNavigationView();


        mSmartTabLayout = (SmartTabLayout) findViewById(R.id.tab_view_pager_fav);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_fav);
        mViewPager.setAdapter(new FavouritesPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        mSmartTabLayout.setViewPager(mViewPager);

    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
