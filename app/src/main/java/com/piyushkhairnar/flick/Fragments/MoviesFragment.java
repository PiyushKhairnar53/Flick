package com.piyushkhairnar.flick.Fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.piyushkhairnar.flick.Activities.MainActivity;
import com.piyushkhairnar.flick.Activities.ViewAllMoviesActivity;
import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.Utils.MovieGenres;
import com.piyushkhairnar.flick.Utils.NetworkConnection;
import com.piyushkhairnar.flick.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.movies.GenresList;
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
public class MoviesFragment extends Fragment {

    private ProgressBar mProgressBar;
    private boolean mNowShowingSectionLoaded;
    private boolean mPopularSectionLoaded;
    private boolean mUpcomingSectionLoaded;
    private boolean mBollyTopRatedSectionLoaded;
    private boolean hollywoodTopRatedSectionLoaded;
    private boolean hollywoodNowShowingSectionLoaded;

    private FrameLayout mBollyNowShowingLayout;
    private TextView mBollyNowShowingViewAllTextView;
    private RecyclerView mBollyNowShowingRecyclerView;
    private List<MovieBrief> mNowShowingMovies;
    private MovieBriefsSmallAdapter mBollyNowShowingAdapter;

    private FrameLayout mPopularLayout;
    private TextView mPopularViewAllTextView;
    private RecyclerView mPopularRecyclerView;
    private List<MovieBrief> mPopularMovies;
    private MovieBriefsSmallAdapter mPopularAdapter;

    private FrameLayout mUpcomingLayout;
    private TextView mUpcomingViewAllTextView;
    private RecyclerView mUpcomingRecyclerView;
    private List<MovieBrief> mUpcomingMovies;
    private MovieBriefsSmallAdapter mUpcomingAdapter;

    private FrameLayout mBollyTopRatedLayout;
    private TextView mBollyTopRatedViewAllTextView;
    private RecyclerView mBollyTopRatedRecyclerView;
    private List<MovieBrief> mBollyTopRatedMovies;
    private MovieBriefsSmallAdapter mBollyTopRatedAdapter;

    private FrameLayout hollywoodTopRatedLayout;
    private TextView hollywoodTopRatedViewAllTextView;
    private RecyclerView hollywoodTopRatedRecyclerView;
    private List<MovieBrief> hollywoodTopRatedMovies;
    private MovieBriefsSmallAdapter hollywoodTopRatedAdapter;

    private FrameLayout hollywoodNowShowingLayout;
    private TextView hollywoodTNowShowingViewAllTextView;
    private RecyclerView hollywoodNowShowingRecyclerView;
    private List<MovieBrief> hollywoodNowShowingMovies;
    private MovieBriefsSmallAdapter hollywoodNowShowingAdapter;


    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isFragmentLoaded;
    private Call<GenresList> mGenresListCall;

    private Call<PopularMoviesResponse> mPopularMoviesCall;
    private Call<UpcomingMoviesResponse> mUpcomingMoviesCall;

    private Call<BollywoodTopRatedMoviesResponse> mBollyTopRatedMoviesCall;
    private Call<NowShowingMoviesResponse> mBollyNowShowingMoviesCall;

    private Call<BollywoodTopRatedMoviesResponse> hollywoodTopRatedMoviesCall;
    private Call<NowShowingMoviesResponse> hollywoodNowShowingMoviesCall;


    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {

        return new MoviesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mNowShowingSectionLoaded = false;
        mPopularSectionLoaded = false;
        mUpcomingSectionLoaded = false;
        mBollyTopRatedSectionLoaded = false;
        hollywoodTopRatedSectionLoaded = false;
        hollywoodNowShowingSectionLoaded = false;

        mNowShowingMovies = new ArrayList<>();
        mPopularMovies = new ArrayList<>();
        mUpcomingMovies = new ArrayList<>();
        mBollyTopRatedMovies = new ArrayList<>();
        hollywoodTopRatedMovies = new ArrayList<>();
        hollywoodNowShowingMovies = new ArrayList<>();


        mBollyNowShowingLayout = (FrameLayout) view.findViewById(R.id.layout_now_showing_bolly);
        mPopularLayout = (FrameLayout) view.findViewById(R.id.layout_popular);
        mUpcomingLayout = (FrameLayout) view.findViewById(R.id.layout_upcoming);
        mBollyTopRatedLayout = (FrameLayout) view.findViewById(R.id.layout_top_rated_bolly);
        hollywoodTopRatedLayout = (FrameLayout)view.findViewById(R.id.layout_top_rated_hollywood);
        hollywoodNowShowingLayout = (FrameLayout)view.findViewById(R.id.layout_now_showing_hollywood);


        mBollyNowShowingAdapter = new MovieBriefsSmallAdapter(getContext(), mNowShowingMovies);
        mPopularAdapter = new MovieBriefsSmallAdapter(getContext(), mPopularMovies);
        mUpcomingAdapter = new MovieBriefsSmallAdapter(getContext(), mUpcomingMovies);
        mBollyTopRatedAdapter = new MovieBriefsSmallAdapter(getContext(), mBollyTopRatedMovies);
        hollywoodTopRatedAdapter = new MovieBriefsSmallAdapter(getContext(), hollywoodTopRatedMovies);
        hollywoodNowShowingAdapter = new MovieBriefsSmallAdapter(getContext(), hollywoodNowShowingMovies);


        mBollyNowShowingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_now_showing_bolly);
        mPopularViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_popular);
        mUpcomingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_upcoming);
        mBollyTopRatedViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_top_rated_bolly);
        hollywoodTopRatedViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_top_rated_hollywood);
        hollywoodTNowShowingViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_now_showing_hollywood);


        mBollyNowShowingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_now_showing_bolly);
        (new LinearSnapHelper()).attachToRecyclerView(mBollyNowShowingRecyclerView);
        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_popular);
        mUpcomingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_upcoming);
        (new LinearSnapHelper()).attachToRecyclerView(mUpcomingRecyclerView);
        mBollyTopRatedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_rated_bolly);
        hollywoodTopRatedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_rated_hollywood);
        hollywoodNowShowingRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_now_showing_hollywood);


        mBollyNowShowingRecyclerView.setAdapter(mBollyNowShowingAdapter);
        mBollyNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
        mUpcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mBollyTopRatedRecyclerView.setAdapter(mBollyTopRatedAdapter);
        mBollyTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        hollywoodTopRatedRecyclerView.setAdapter(hollywoodTopRatedAdapter);
        hollywoodTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        hollywoodNowShowingRecyclerView.setAdapter(hollywoodNowShowingAdapter);
        hollywoodNowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        mBollyNowShowingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.NOW_SHOWING_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mPopularViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.POPULAR_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mUpcomingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.UPCOMING_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        mBollyTopRatedViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.TOP_RATED_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        hollywoodTopRatedViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.TOP_RATED_MOVIES_TYPE);
                startActivity(intent);
            }
        });
        hollywoodTNowShowingViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.TOP_RATED_MOVIES_TYPE);
                startActivity(intent);
            }
        });

        if (NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadFragment();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mBollyNowShowingAdapter.notifyDataSetChanged();
        mPopularAdapter.notifyDataSetChanged();
        mUpcomingAdapter.notifyDataSetChanged();
        mBollyTopRatedAdapter.notifyDataSetChanged();
        hollywoodTopRatedAdapter.notifyDataSetChanged();
        hollywoodNowShowingAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isFragmentLoaded && !NetworkConnection.isConnected(getContext())) {
            mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver(new ConnectivityBroadcastReceiver.ConnectivityReceiverListener() {
                @Override
                public void onNetworkConnectionConnected() {
                    isFragmentLoaded = true;
                    loadFragment();
                    isBroadcastReceiverRegistered = false;
                    getActivity().unregisterReceiver(mConnectivityBroadcastReceiver);
                }
            });

            Toast.makeText(getContext(),"No Internet Connection!",Toast.LENGTH_LONG).show();

            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadcastReceiverRegistered = true;
            getActivity().registerReceiver(mConnectivityBroadcastReceiver, intentFilter);
        } else if (!isFragmentLoaded && NetworkConnection.isConnected(getContext())) {
            isFragmentLoaded = true;
            loadFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = false;
            getActivity().unregisterReceiver(mConnectivityBroadcastReceiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mGenresListCall != null) mGenresListCall.cancel();
        if (mBollyNowShowingMoviesCall != null) mBollyNowShowingMoviesCall.cancel();
        if (mPopularMoviesCall != null) mPopularMoviesCall.cancel();
        if (mUpcomingMoviesCall != null) mUpcomingMoviesCall.cancel();
        if (mBollyTopRatedMoviesCall != null) mBollyTopRatedMoviesCall.cancel();

    }

    private void loadFragment() {

        if (MovieGenres.isGenresListLoaded()) {
            loadBollyNowShowingMovies();
            loadPopularMovies();
            loadUpcomingMovies();
            loadBollyTopRatedMovies();
            loadHollywoodTopRatedMovies();
            loadHollywoodNowShowingMovies();
        } else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            mProgressBar.setVisibility(View.VISIBLE);
            mGenresListCall = apiService.getMovieGenresList(getResources().getString(R.string.MOVIE_DB_API_KEY));
            mGenresListCall.enqueue(new Callback<GenresList>() {
                @Override
                public void onResponse(Call<GenresList> call, Response<GenresList> response) {
                    if (!response.isSuccessful()) {
                        mGenresListCall = call.clone();
                        mGenresListCall.enqueue(this);
                        return;
                    }

                    if (response.body() == null) return;
                    if (response.body().getGenres() == null) return;

                    MovieGenres.loadGenresList(response.body().getGenres());
                    loadBollyNowShowingMovies();
                    loadPopularMovies();
                    loadUpcomingMovies();
                    loadBollyTopRatedMovies();
                    loadHollywoodTopRatedMovies();
                    loadHollywoodNowShowingMovies();
                }

                @Override
                public void onFailure(Call<GenresList> call, Throwable t) {

                }
            });
        }

    }

    private void loadBollyNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mBollyNowShowingMoviesCall = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "IN");
        mBollyNowShowingMoviesCall.enqueue(new Callback<NowShowingMoviesResponse>() {
            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mBollyNowShowingMoviesCall = call.clone();
                    mBollyNowShowingMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mNowShowingSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getBackdropPath() != null)
                        mNowShowingMovies.add(movieBrief);
                }
                mBollyNowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mPopularMoviesCall = apiService.getPopularMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "IN");
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

                mPopularSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPosterPath() != null)
                        mPopularMovies.add(movieBrief);
                }
                mPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadUpcomingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mUpcomingMoviesCall = apiService.getUpcomingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
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

                mUpcomingSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getBackdropPath() != null)
                        mUpcomingMovies.add(movieBrief);
                }
                mUpcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadBollyTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mBollyTopRatedMoviesCall = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 2, "IN");
        mBollyTopRatedMoviesCall.enqueue(new Callback<BollywoodTopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<BollywoodTopRatedMoviesResponse> call, Response<BollywoodTopRatedMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mBollyTopRatedMoviesCall = call.clone();
                    mBollyTopRatedMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mBollyTopRatedSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPosterPath() != null)
                        mBollyTopRatedMovies.add(movieBrief);
                }
                mBollyTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BollywoodTopRatedMoviesResponse> call, Throwable t) {

            }
        });
    }


    private void loadHollywoodTopRatedMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        hollywoodTopRatedMoviesCall = apiService.getTopRatedMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1,"US");
        hollywoodTopRatedMoviesCall.enqueue(new Callback<BollywoodTopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<BollywoodTopRatedMoviesResponse> call, Response<BollywoodTopRatedMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    hollywoodTopRatedMoviesCall = call.clone();
                    hollywoodTopRatedMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                hollywoodTopRatedSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getPosterPath() != null)
                        hollywoodTopRatedMovies.add(movieBrief);
                }
                hollywoodTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BollywoodTopRatedMoviesResponse> call, Throwable t) {

            }
        });
    }

    private void loadHollywoodNowShowingMovies() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        hollywoodNowShowingMoviesCall = apiService.getNowShowingMovies(getResources().getString(R.string.MOVIE_DB_API_KEY), 1, "US");
        hollywoodNowShowingMoviesCall.enqueue(new Callback<NowShowingMoviesResponse>() {
            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    hollywoodNowShowingMoviesCall = call.clone();
                    hollywoodNowShowingMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                hollywoodNowShowingSectionLoaded = true;
                checkAllDataLoaded();
                for (MovieBrief movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getBackdropPath() != null)
                        hollywoodNowShowingMovies.add(movieBrief);
                }
                hollywoodNowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

            }
        });
    }



    private void checkAllDataLoaded() {
        if (mNowShowingSectionLoaded && mPopularSectionLoaded && mUpcomingSectionLoaded && mBollyTopRatedSectionLoaded && hollywoodTopRatedSectionLoaded && hollywoodNowShowingSectionLoaded ) {
            mProgressBar.setVisibility(View.GONE);
            mBollyNowShowingLayout.setVisibility(View.VISIBLE);
            mBollyNowShowingRecyclerView.setVisibility(View.VISIBLE);
            mPopularLayout.setVisibility(View.VISIBLE);
            mPopularRecyclerView.setVisibility(View.VISIBLE);
            mUpcomingLayout.setVisibility(View.VISIBLE);
            mUpcomingRecyclerView.setVisibility(View.VISIBLE);
            mBollyTopRatedLayout.setVisibility(View.VISIBLE);
            mBollyTopRatedRecyclerView.setVisibility(View.VISIBLE);
            hollywoodTopRatedLayout.setVisibility(View.VISIBLE);
            hollywoodTopRatedRecyclerView.setVisibility(View.VISIBLE);
            hollywoodNowShowingLayout.setVisibility(View.VISIBLE);
            hollywoodNowShowingRecyclerView.setVisibility(View.VISIBLE);

        }
    }
}
