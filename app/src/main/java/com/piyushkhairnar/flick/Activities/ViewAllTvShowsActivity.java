package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.piyushkhairnar.flick.Adapters.MoviesPagerAdapter;
import com.piyushkhairnar.flick.Adapters.TVShowBriefsSmallAdapter;
import com.piyushkhairnar.flick.Adapters.TVShowsPagerAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.tvshows.AiringTodayTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.OnTheAirTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.PopularTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;
import com.piyushkhairnar.flick.network.tvshows.TopRatedTVShowsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllTvShowsActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MoviesPagerAdapter pagerAdapter;

    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tv_shows);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTvShows);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        imgBack = (ImageView)findViewById(R.id.back_view_all_shows);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.view_all_tv_shows_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_view_all_tv_shows);
        mViewPager.setAdapter(new TVShowsPagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
