package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.piyushkhairnar.flick.Adapters.MoviesPagerAdapter;
import com.piyushkhairnar.flick.Adapters.TVShowsPagerAdapter;
import com.piyushkhairnar.flick.R;

public class ViewAllMoviesActivity extends AppCompatActivity {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MoviesPagerAdapter pagerAdapter;

    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMovies);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        imgBack = (ImageView)findViewById(R.id.back_view_all_movies);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mTabLayout = (TabLayout) findViewById(R.id.view_all_movies_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_view_all_movies);
        mViewPager.setAdapter(new MoviesPagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
