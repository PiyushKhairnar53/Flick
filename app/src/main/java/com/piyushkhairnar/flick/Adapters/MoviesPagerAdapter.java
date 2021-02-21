package com.piyushkhairnar.flick.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.piyushkhairnar.flick.AllMoviesFragments.NowShowingBollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.NowShowingHollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.PopularMoviesFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.TopRatedBollywoodFragment;

import com.piyushkhairnar.flick.AllMoviesFragments.TopRatedHollywoodFragment;
import com.piyushkhairnar.flick.AllMoviesFragments.UpcomingMoviesFragment;
import com.piyushkhairnar.flick.R;

public class MoviesPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;


    public MoviesPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  new PopularMoviesFragment();
                break;
            case 1:
                fragment =  new NowShowingBollywoodFragment();
                break;
            case 2:
                fragment =  new TopRatedBollywoodFragment();
                break;
            case 3:
                fragment =  new NowShowingHollywoodFragment();
                break;
            case 4:
                fragment =  new TopRatedHollywoodFragment();
                break;
            case 5:
                fragment =  new UpcomingMoviesFragment();
                break;


        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.popular_movies);
            case 1:
                return mContext.getResources().getString(R.string.now_showing_hindi);
            case 2:
                return mContext.getResources().getString(R.string.top_rated_hindi);
            case 3:
                return mContext.getResources().getString(R.string.now_showing_english);
            case 4:
                return mContext.getResources().getString(R.string.top_rated_english);
            case 5:
                return mContext.getResources().getString(R.string.upcoming_movies);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
