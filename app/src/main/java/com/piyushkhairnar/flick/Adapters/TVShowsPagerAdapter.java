package com.piyushkhairnar.flick.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.piyushkhairnar.flick.AllMoviesFragments.NowShowingBollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.NowShowingHollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.PopularMoviesFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.TopRatedBollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.TopRatedHollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.UpcomingMoviesFragment;
import com.piyushkhairnar.flick.AllShowsFragments.AiringTodayTvShowsFragment;
import com.piyushkhairnar.flick.AllShowsFragments.OnAirTvShowsFragment;
import com.piyushkhairnar.flick.AllShowsFragments.PopularTvShowsFragment;
import com.piyushkhairnar.flick.AllShowsFragments.TopRatedTVShowsFragment;
import com.piyushkhairnar.flick.R;

public class TVShowsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public TVShowsPagerAdapter(@NonNull FragmentManager fm,Context context) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  new TopRatedTVShowsFragment();
                break;
            case 1:
                fragment =  new PopularTvShowsFragment();
                break;
            case 2:
                fragment =  new AiringTodayTvShowsFragment();
                break;
            case 3:
                fragment =  new OnAirTvShowsFragment();
                break;


        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.top_rated_shows);
            case 1:
                return mContext.getResources().getString(R.string.popular_shows);
            case 2:
                return mContext.getResources().getString(R.string.airing_today_shows);
            case 3:
                return mContext.getResources().getString(R.string.on_the_air_shows);

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
