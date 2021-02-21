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
import com.piyushkhairnar.flick.Activities.ViewAllTvShowsActivity;
import com.piyushkhairnar.flick.Adapters.TVShowBriefsLargeAdapter;
import com.piyushkhairnar.flick.Adapters.TVShowBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.Utils.NetworkConnection;
import com.piyushkhairnar.flick.Utils.TvShowGenres;
import com.piyushkhairnar.flick.broadcastreceivers.ConnectivityBroadcastReceiver;
import com.piyushkhairnar.flick.network.ApiClient;
import com.piyushkhairnar.flick.network.ApiInterface;
import com.piyushkhairnar.flick.network.tvshows.AiringTodayTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.GenresList;
import com.piyushkhairnar.flick.network.tvshows.OnTheAirTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.PopularTVShowsResponse;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;
import com.piyushkhairnar.flick.network.tvshows.TopRatedTVShowsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvshowsFragment extends Fragment {

    private ProgressBar mProgressBar;
    private boolean mAiringTodaySectionLoaded;
    private boolean mOnTheAirSectionLoaded;
    private boolean mPopularSectionLoaded;
    private boolean mTopRatedSectionLoaded;

    private FrameLayout mAiringTodayLayout;
    private TextView mAiringTodayViewAllTextView;
    private RecyclerView mAiringTodayRecyclerView;
    private List<TVShowBrief> mAiringTodayTVShows;
    private TVShowBriefsSmallAdapter mAiringTodayAdapter;

    private FrameLayout mOnTheAirLayout;
    private TextView mOnTheAirViewAllTextView;
    private RecyclerView mOnTheAirRecyclerView;
    private List<TVShowBrief> mOnTheAirTVShows;
    private TVShowBriefsSmallAdapter mOnTheAirAdapter;

    private FrameLayout mPopularLayout;
    private TextView mPopularViewAllTextView;
    private RecyclerView mPopularRecyclerView;
    private List<TVShowBrief> mPopularTVShows;
    private TVShowBriefsSmallAdapter mPopularAdapter;

    private FrameLayout mTopRatedLayout;
    private TextView mTopRatedViewAllTextView;
    private RecyclerView mTopRatedRecyclerView;
    private List<TVShowBrief> mTopRatedTVShows;
    private TVShowBriefsSmallAdapter mTopRatedAdapter;

    private Snackbar mConnectivitySnackbar;
    private ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private boolean isBroadcastReceiverRegistered;
    private boolean isFragmentLoaded;
    private Call<GenresList> mGenresListCall;
    private Call<AiringTodayTVShowsResponse> mAiringTodayTVShowsCall;
    private Call<OnTheAirTVShowsResponse> mOnTheAirTVShowsCall;
    private Call<PopularTVShowsResponse> mPopularTVShowsCall;
    private Call<TopRatedTVShowsResponse> mTopRatedTVShowsCall;

    private AdView tAdView1;
    private AdView tAdView2;
    private AdView tAdView3;

    public TvshowsFragment() {
        // Required empty public constructor
    }

    public static TvshowsFragment newInstance() {

        return new TvshowsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvshows, container, false);
        // Inflate the layout for this fragment


        MobileAds.initialize(getContext(), getString(R.string.admob_app_id));


        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mAiringTodaySectionLoaded = false;
        mOnTheAirSectionLoaded = false;
        mPopularSectionLoaded = false;
        mTopRatedSectionLoaded = false;

        mAiringTodayLayout = (FrameLayout) view.findViewById(R.id.layout_airing_today);
        mOnTheAirLayout = (FrameLayout) view.findViewById(R.id.layout_on_the_air);
        mPopularLayout = (FrameLayout) view.findViewById(R.id.layout_popular);
        mTopRatedLayout = (FrameLayout) view.findViewById(R.id.layout_top_rated);

        mAiringTodayViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_airing_today);
        mOnTheAirViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_on_the_air);
        mPopularViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_popular);
        mTopRatedViewAllTextView = (TextView) view.findViewById(R.id.text_view_view_all_top_rated);

        mAiringTodayRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_airing_today);
        (new LinearSnapHelper()).attachToRecyclerView(mAiringTodayRecyclerView);
        mOnTheAirRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_on_the_air);
        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_popular);
        (new LinearSnapHelper()).attachToRecyclerView(mPopularRecyclerView);
        mTopRatedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_rated);

        mAiringTodayTVShows = new ArrayList<>();
        mOnTheAirTVShows = new ArrayList<>();
        mPopularTVShows = new ArrayList<>();
        mTopRatedTVShows = new ArrayList<>();

        mAiringTodayAdapter = new TVShowBriefsSmallAdapter(getContext(), mAiringTodayTVShows);
        mOnTheAirAdapter = new TVShowBriefsSmallAdapter(getContext(), mOnTheAirTVShows);
        mPopularAdapter = new TVShowBriefsSmallAdapter(getContext(), mPopularTVShows);
        mTopRatedAdapter = new TVShowBriefsSmallAdapter(getContext(), mTopRatedTVShows);

        mAiringTodayRecyclerView.setAdapter(mAiringTodayAdapter);
        mAiringTodayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mOnTheAirRecyclerView.setAdapter(mOnTheAirAdapter);
        mOnTheAirRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mPopularRecyclerView.setAdapter(mPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
        mTopRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mAiringTodayViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllTvShowsActivity.class);
                intent.putExtra(Constants.VIEW_ALL_TV_SHOWS_TYPE, Constants.AIRING_TODAY_TV_SHOWS_TYPE);
                startActivity(intent);
            }
        });
        mOnTheAirViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllTvShowsActivity.class);
                intent.putExtra(Constants.VIEW_ALL_TV_SHOWS_TYPE, Constants.ON_THE_AIR_TV_SHOWS_TYPE);
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
                Intent intent = new Intent(getContext(), ViewAllTvShowsActivity.class);
                intent.putExtra(Constants.VIEW_ALL_TV_SHOWS_TYPE, Constants.POPULAR_TV_SHOWS_TYPE);
                startActivity(intent);
            }
        });
        mTopRatedViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnection.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ViewAllTvShowsActivity.class);
                intent.putExtra(Constants.VIEW_ALL_TV_SHOWS_TYPE, Constants.TOP_RATED_TV_SHOWS_TYPE);
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

        mAiringTodayAdapter.notifyDataSetChanged();
        mOnTheAirAdapter.notifyDataSetChanged();
        mPopularAdapter.notifyDataSetChanged();
        mTopRatedAdapter.notifyDataSetChanged();
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
        if (mAiringTodayTVShowsCall != null) mAiringTodayTVShowsCall.cancel();
        if (mOnTheAirTVShowsCall != null) mOnTheAirTVShowsCall.cancel();
        if (mPopularTVShowsCall != null) mPopularTVShowsCall.cancel();
        if (mTopRatedTVShowsCall != null) mTopRatedTVShowsCall.cancel();
    }

    private void loadFragment() {

        if (TvShowGenres.isGenresListLoaded()) {
            loadAiringTodayTVShows();
            loadOnTheAirTVShows();
            loadPopularTVShows();
            loadTopRatedTVShows();
        } else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            mProgressBar.setVisibility(View.VISIBLE);
            mGenresListCall = apiService.getTVShowGenresList(getResources().getString(R.string.MOVIE_DB_API_KEY));
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

                    TvShowGenres.loadGenresList(response.body().getGenres());
                    loadAiringTodayTVShows();
                    loadOnTheAirTVShows();
                    loadPopularTVShows();
                    loadTopRatedTVShows();
                }

                @Override
                public void onFailure(Call<GenresList> call, Throwable t) {

                }
            });
        }

    }

    private void loadAiringTodayTVShows() {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mAiringTodayTVShowsCall = apiService.getAiringTodayTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mAiringTodayTVShowsCall.enqueue(new Callback<AiringTodayTVShowsResponse>() {
            @Override
            public void onResponse(Call<AiringTodayTVShowsResponse> call, Response<AiringTodayTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mAiringTodayTVShowsCall = call.clone();
                    mAiringTodayTVShowsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mAiringTodaySectionLoaded = true;
                checkAllDataLoaded();
                for (TVShowBrief TVShowBrief : response.body().getResults()) {
                    if (TVShowBrief != null && TVShowBrief.getBackdropPath() != null)
                        mAiringTodayTVShows.add(TVShowBrief);
                }
                mAiringTodayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AiringTodayTVShowsResponse> call, Throwable t) {

            }
        });
    }

    private void loadOnTheAirTVShows() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mOnTheAirTVShowsCall = apiService.getOnTheAirTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mOnTheAirTVShowsCall.enqueue(new Callback<OnTheAirTVShowsResponse>() {
            @Override
            public void onResponse(Call<OnTheAirTVShowsResponse> call, Response<OnTheAirTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mOnTheAirTVShowsCall = call.clone();
                    mOnTheAirTVShowsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mOnTheAirSectionLoaded = true;
                checkAllDataLoaded();
                for (TVShowBrief TVShowBrief : response.body().getResults()) {
                    if (TVShowBrief != null && TVShowBrief.getPosterPath() != null)
                        mOnTheAirTVShows.add(TVShowBrief);
                }
                mOnTheAirAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OnTheAirTVShowsResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularTVShows() {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mPopularTVShowsCall = apiService.getPopularTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mPopularTVShowsCall.enqueue(new Callback<PopularTVShowsResponse>() {
            @Override
            public void onResponse(Call<PopularTVShowsResponse> call, Response<PopularTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mPopularTVShowsCall = call.clone();
                    mPopularTVShowsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mPopularSectionLoaded = true;
                checkAllDataLoaded();
                for (TVShowBrief TVShowBrief : response.body().getResults()) {
                    if (TVShowBrief != null && TVShowBrief.getBackdropPath() != null)
                        mPopularTVShows.add(TVShowBrief);
                }
                mPopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularTVShowsResponse> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedTVShows() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        mProgressBar.setVisibility(View.VISIBLE);
        mTopRatedTVShowsCall = apiService.getTopRatedTVShows(getResources().getString(R.string.MOVIE_DB_API_KEY), 1);
        mTopRatedTVShowsCall.enqueue(new Callback<TopRatedTVShowsResponse>() {
            @Override
            public void onResponse(Call<TopRatedTVShowsResponse> call, Response<TopRatedTVShowsResponse> response) {
                if (!response.isSuccessful()) {
                    mTopRatedTVShowsCall = call.clone();
                    mTopRatedTVShowsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                mTopRatedSectionLoaded = true;
                checkAllDataLoaded();
                for (TVShowBrief TVShowBrief : response.body().getResults()) {
                    if (TVShowBrief != null && TVShowBrief.getPosterPath() != null)
                        mTopRatedTVShows.add(TVShowBrief);
                }
                mTopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedTVShowsResponse> call, Throwable t) {

            }
        });
    }

    private void checkAllDataLoaded() {
        if (mAiringTodaySectionLoaded && mOnTheAirSectionLoaded && mPopularSectionLoaded && mTopRatedSectionLoaded) {
            mProgressBar.setVisibility(View.GONE);
            mAiringTodayLayout.setVisibility(View.VISIBLE);
            mAiringTodayRecyclerView.setVisibility(View.VISIBLE);
            mOnTheAirLayout.setVisibility(View.VISIBLE);
            mOnTheAirRecyclerView.setVisibility(View.VISIBLE);
            mPopularLayout.setVisibility(View.VISIBLE);
            mPopularRecyclerView.setVisibility(View.VISIBLE);
            mTopRatedLayout.setVisibility(View.VISIBLE);
            mTopRatedRecyclerView.setVisibility(View.VISIBLE);
        }
    }


}
