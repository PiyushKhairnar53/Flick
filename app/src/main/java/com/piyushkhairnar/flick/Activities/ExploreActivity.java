package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.BottomNavigationViewHelper;
import com.piyushkhairnar.flick.Utils.Constants;


public class ExploreActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext;
    private Button btnSearch;
    private CardView moviesTodayBtn;
    private CardView movies7DaysBtn;

    private CardView showsTodayBtn;
    private CardView shows7DaysBtn;

    private CardView cardViewMovies;
    private CardView cardViewShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        mContext = ExploreActivity.this;
        setupBottomNavigationView();

        //imageView = findViewById(R.id.movie_background);
        btnSearch = (Button)findViewById(R.id.btn_search_activity);
        moviesTodayBtn = (CardView)findViewById(R.id.cardview_trending_movies_today);
        movies7DaysBtn = (CardView)findViewById(R.id.cardview_trending_movies_7days);

        cardViewMovies = (CardView)findViewById(R.id.cardview_movies);
        cardViewShows = (CardView)findViewById(R.id.cardview_shows);

        showsTodayBtn = (CardView)findViewById(R.id.cardview_trending_shows_today);
        shows7DaysBtn = (CardView)findViewById(R.id.cardview_trending_shows_7days);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this,SearchActivity.class));
            }
        });

        cardViewMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,ViewAllMoviesActivity.class);
                startActivity(intent);
            }
        });

        cardViewShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,ViewAllTvShowsActivity.class);
                startActivity(intent);
            }
        });

        moviesTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,TrendingActivity.class);
                intent.putExtra(Constants.TRENDING_MOVIES, Constants.TRENDING_MOVIES_TODAY);
                startActivity(intent);

            }
        });

        movies7DaysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,TrendingActivity.class);
                intent.putExtra(Constants.TRENDING_MOVIES, Constants.TRENDING_MOVIES_7DAYS);
                startActivity(intent);
            }
        });

        showsTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,TrendingShowsActivity.class);
                intent.putExtra(Constants.TRENDING_SHOWS, Constants.TRENDING_SHOWS_TODAY);
                startActivity(intent);
            }
        });

        shows7DaysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExploreActivity.this,TrendingShowsActivity.class);
                intent.putExtra(Constants.TRENDING_SHOWS, Constants.TRENDING_SHOWS_7DAYS);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

}
