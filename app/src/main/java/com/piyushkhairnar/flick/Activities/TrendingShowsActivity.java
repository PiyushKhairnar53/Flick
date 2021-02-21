package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyushkhairnar.flick.Adapters.TVShowBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.movies.TrendingTodayMoviesResponse;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;
import com.piyushkhairnar.flick.network.tvshows.TrendingTodayShowsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingShowsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<TVShowBrief> mTVShows;
    private TVShowBriefsSmallAdapter mTVShowsAdapter;

    private int mTVShowType;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<TrendingTodayShowsResponse> mTrendingTodayShowsCall;
    private Call<TrendingTodayShowsResponse> mTrending7DaysShowsCall;

    private ImageView btnBack;
    private TextView txtToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_shows);

        Intent receivedIntent = getIntent();
        mTVShowType = receivedIntent.getIntExtra(Constants.TRENDING_SHOWS, -1);

        btnBack = (ImageView)findViewById(R.id.back_trending_shows);
        txtToolbarTitle = (TextView)findViewById(R.id.txt_toolbar_title_trending_shows);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trending_shows);
        mTVShows = new ArrayList<>();
        mTVShowsAdapter = new TVShowBriefsSmallAdapter(TrendingShowsActivity.this, mTVShows);
        mRecyclerView.setAdapter(mTVShowsAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(TrendingShowsActivity.this, 3);
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
                    loadTVShows(mTVShowType);
                    loading = true;
                }

            }
        });

        loadTVShows(mTVShowType);

    }

    private void loadTVShows(int tvShowType) {
        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        mSmoothProgressBar.progressiveStart();

        switch (tvShowType) {
            case Constants.TRENDING_SHOWS_TODAY:
                txtToolbarTitle.setText("Shows - Trending Today");
                mTrendingTodayShowsCall = apiService.getTrendingShowsToday(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mTrendingTodayShowsCall.enqueue(new Callback<TrendingTodayShowsResponse>() {
                    @Override
                    public void onResponse(Call<TrendingTodayShowsResponse> call, Response<TrendingTodayShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mTrendingTodayShowsCall = call.clone();
                            mTrendingTodayShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }


                    @Override
                    public void onFailure(Call<TrendingTodayShowsResponse> call, Throwable t) {

                    }
                });
                break;

            case Constants.TRENDING_SHOWS_7DAYS:
                txtToolbarTitle.setText("Shows - Trending Week");
                mTrending7DaysShowsCall = apiService.getTrendingShows7Days(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage);
                mTrending7DaysShowsCall.enqueue(new Callback<TrendingTodayShowsResponse>() {
                    @Override
                    public void onResponse(Call<TrendingTodayShowsResponse> call, Response<TrendingTodayShowsResponse> response) {
                        if (!response.isSuccessful()) {
                            mTrending7DaysShowsCall = call.clone();
                            mTrending7DaysShowsCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (TVShowBrief tvShowBrief : response.body().getResults()) {
                            if (tvShowBrief != null && tvShowBrief.getName() != null && tvShowBrief.getPosterPath() != null)
                                mTVShows.add(tvShowBrief);
                        }
                        mTVShowsAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }


                    @Override
                    public void onFailure(Call<TrendingTodayShowsResponse> call, Throwable t) {

                    }
                });
                break;

        }

    }
}