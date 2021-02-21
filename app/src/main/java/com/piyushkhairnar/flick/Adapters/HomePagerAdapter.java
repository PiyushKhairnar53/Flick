package com.piyushkhairnar.flick.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.piyushkhairnar.flick.Fragments.FavouriteMoviesFragment;
import com.piyushkhairnar.flick.Fragments.FavouriteTvShowsFragment;
import com.piyushkhairnar.flick.Fragments.MoviesFragment;
import com.piyushkhairnar.flick.Fragments.TvshowsFragment;
import com.piyushkhairnar.flick.R;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MoviesFragment();
            case 1:
                return new TvshowsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.movies);
            case 1:
                return mContext.getResources().getString(R.string.tv_shows);
        }
        return null;
    }
}