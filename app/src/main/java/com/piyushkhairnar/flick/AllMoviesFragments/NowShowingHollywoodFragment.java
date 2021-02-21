package com.piyushkhairnar.flick.AllMoviesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.movies.MovieBrief;
import com.piyushkhairnar.flick.network.movies.NowShowingMoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowShowingHollywoodFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<MovieBrief> mMovies;
    private MovieBriefsSmallAdapter mMoviesAdapter;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<NowShowingMoviesResponse> mNowShowingMoviesCall;

    public NowShowingHollywoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        mNowShowingMoviesCall = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), presentPage, "US");
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

    }


}
