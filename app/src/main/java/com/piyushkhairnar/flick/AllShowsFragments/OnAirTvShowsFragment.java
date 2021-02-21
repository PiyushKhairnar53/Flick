package com.piyushkhairnar.flick.AllShowsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.piyushkhairnar.flick.Adapters.TVShowBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.tvshows.AiringTodayTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.OnTheAirTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnAirTvShowsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<TVShowBrief> mTVShows;
    private TVShowBriefsSmallAdapter mTVShowsAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<OnTheAirTVShowsResponse> mOnAirTVShowsCall;

    public OnAirTvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_all_movies, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_view_all_movies);
        mTVShows = new ArrayList<>();
        mTVShowsAdapter = new TVShowBriefsSmallAdapter(getContext(), mTVShows);
        mRecyclerView.setAdapter(mTVShowsAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
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
                    loadMovies();
                    loading = true;
                }

            }
        });

        loadMovies();


        return view;
    }

    private void loadMovies() {

        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        mSmoothProgressBar.progressiveStart();

        mOnAirTVShowsCall = apiService.getOnTheAirTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mOnAirTVShowsCall.enqueue(new Callback<OnTheAirTVShowsResponse>() {
            @Override
            public void onResponse(Call<OnTheAirTVShowsResponse> call, Response<OnTheAirTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mOnAirTVShowsCall = call.clone();
                    mOnAirTVShowsCall.enqueue(this);
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
            public void onFailure(Call<OnTheAirTVShowsResponse> call, Throwable t) {

            }
        });

    }

}

