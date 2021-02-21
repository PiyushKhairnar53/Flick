package com.piyushkhairnar.flick.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piyushkhairnar.flick.Activities.ViewAllMoviesActivity;
import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.movies.MovieBrief;
import com.piyushkhairnar.flick.network.movies.NowShowingMoviesResponse;
import com.piyushkhairnar.flick.network.movies.PopularMoviesResponse;
import com.piyushkhairnar.flick.network.movies.BollywoodTopRatedMoviesResponse;
import com.piyushkhairnar.flick.network.movies.UpcomingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllMoviesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<MovieBrief> mMovies;
    private MovieBriefsSmallAdapter mMoviesAdapter;

    private int mMovieType;
    private String country;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<NowShowingMoviesResponse> mNowShowingMoviesCall;
    private Call<PopularMoviesResponse> mPopularMoviesCall;
    private Call<UpcomingMoviesResponse> mUpcomingMoviesCall;
    private Call<BollywoodTopRatedMoviesResponse> mTopRatedMoviesCall;

    public ViewAllMoviesFragment(int movieType,String code) {

        mMovieType = movieType;
        country = code;

    }

    public ViewAllMoviesFragment() {

    }

    public static ViewAllMoviesFragment newInstance() {

        return new ViewAllMoviesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_all_movies, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_view_all_movies);
        mMovies = new ArrayList<>();
        mMoviesAdapter = new MovieBriefsSmallAdapter(getContext(), mMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
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
                    loadMovies(mMovieType,country);
                    loading = true;
                }

            }
        });

        loadMovies(mMovieType,country);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMoviesAdapter.notifyDataSetChanged();

    }

    private void loadMovies(int movieType,String code) {
        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        mSmoothProgressBar.progressiveStart();

        switch (movieType) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                mNowShowingMoviesCall = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, code);
                mNowShowingMoviesCall.enqueue(new Callback<NowShowingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mNowShowingMoviesCall = call.clone();
                            mNowShowingMoviesCall.enqueue(this);
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
                    public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                mPopularMoviesCall = apiService.getPopularMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "IN");
                mPopularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {
                    @Override
                    public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mPopularMoviesCall = call.clone();
                            mPopularMoviesCall.enqueue(this);
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
                    public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                mUpcomingMoviesCall = apiService.getUpcomingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "IN");
                mUpcomingMoviesCall.enqueue(new Callback<UpcomingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<UpcomingMoviesResponse> call, Response<UpcomingMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mUpcomingMoviesCall = call.clone();
                            mUpcomingMoviesCall.enqueue(this);
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
                    public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                mTopRatedMoviesCall = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "IN");
                mTopRatedMoviesCall.enqueue(new Callback<BollywoodTopRatedMoviesResponse>() {
                    @Override
                    public void onResponse(Call<BollywoodTopRatedMoviesResponse> call, Response<BollywoodTopRatedMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mTopRatedMoviesCall = call.clone();
                            mTopRatedMoviesCall.enqueue(this);
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
                    public void onFailure(Call<BollywoodTopRatedMoviesResponse> call, Throwable t) {

                    }
                });
                break;
        }
    }

}
