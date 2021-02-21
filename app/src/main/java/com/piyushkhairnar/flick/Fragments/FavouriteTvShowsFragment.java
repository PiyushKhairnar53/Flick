package com.piyushkhairnar.flick.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.piyushkhairnar.flick.Adapters.TVShowBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Favourite;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteTvShowsFragment extends Fragment {

    private RecyclerView mFavTVShowsRecyclerView;
    private List<TVShowBrief> mFavTVShows;
    private TVShowBriefsSmallAdapter mFavTVShowsAdapter;

    private LinearLayout mEmptyLayout;

    public FavouriteTvShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_tv_shows, container, false);

        mFavTVShowsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_tv_shows);
        mFavTVShows = new ArrayList<>();
        mFavTVShowsAdapter = new TVShowBriefsSmallAdapter(getContext(), mFavTVShows);
        mFavTVShowsRecyclerView.setAdapter(mFavTVShowsAdapter);
        mFavTVShowsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_recycler_view_fav_tv_shows_empty);

        loadFavTVShows();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFavTVShowsAdapter.notifyDataSetChanged();
    }

    private void loadFavTVShows() {
        List<TVShowBrief> favTVShowBriefs = Favourite.getFavTVShowBriefs(getContext());
        if (favTVShowBriefs.isEmpty()) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }

        for (TVShowBrief tvShowBrief : favTVShowBriefs) {
            mFavTVShows.add(tvShowBrief);
        }
        mFavTVShowsAdapter.notifyDataSetChanged();
    }

}
