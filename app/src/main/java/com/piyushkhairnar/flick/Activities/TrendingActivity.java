package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.Adapters.SearchResultsAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.movies.Trending7DaysMoviesResponse;
import com.piyushkhairnar.flick.network.movies.TrendingTodayMoviesResponse;
import com.piyushkhairnar.flick.network.movies.MovieBrief;
import com.piyushkhairnar.flick.network.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<MovieBrief> mMovies;
    private MovieBriefsSmallAdapter mMoviesAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private int mTrendType;

    private Call<TrendingTodayMoviesResponse> mTrendingTodayMovies;
    private Call<Trending7DaysMoviesResponse> mTrending7DaysMovies;

    private ImageView btnBack;
    private TextView txtToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Intent receivedIntent = getIntent();
        mTrendType = receivedIntent.getIntExtra(Constants.TRENDING_MOVIES, -1);

        if (mTrendType == -1) finish();

        btnBack = (ImageView)findViewById(R.id.back_trending_movies);
        txtToolbarTitle = (TextView)findViewById(R.id.txt_toolbar_title_trending);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_genre_activity);
        mMovies = new ArrayList<>();
        mMoviesAdapter = new MovieBriefsSmallAdapter(TrendingActivity.this, mMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(TrendingActivity.this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadMovies(mTrendType);
                    loading = true;
                }

            }
        });

        loadMovies(mTrendType);
    }

    private void loadMovies(int trendType) {

        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        mSmoothProgressBar.progressiveStart();

        switch (trendType) {

            case Constants.TRENDING_MOVIES_TODAY:
                txtToolbarTitle.setText("Movies - Trending Today");
                mTrendingTodayMovies = apiService.getTrendingMoviesDay(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mTrendingTodayMovies.enqueue(new Callback<TrendingTodayMoviesResponse>() {
                    @Override
                    public void onResponse(Call<TrendingTodayMoviesResponse> call, Response<TrendingTodayMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mTrendingTodayMovies = call.clone();
                            mTrendingTodayMovies.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<TrendingTodayMoviesResponse> call, Throwable t) {

                    }
                });
                break;

            case Constants.TRENDING_MOVIES_7DAYS:
                txtToolbarTitle.setText("Movies - Trending Week");
                mTrending7DaysMovies = apiService.getTrendingMovies7Days(getResources().getString(R.string.MOVIE_DB_API_KEY),presentPage);
                mTrending7DaysMovies.enqueue(new Callback<Trending7DaysMoviesResponse>() {
                    @Override
                    public void onResponse(Call<Trending7DaysMoviesResponse> call, Response<Trending7DaysMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mTrending7DaysMovies = call.clone();
                            mTrending7DaysMovies.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<Trending7DaysMoviesResponse> call, Throwable t) {

                    }
                });
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMoviesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMoviesAdapter.notifyDataSetChanged();
    }

}