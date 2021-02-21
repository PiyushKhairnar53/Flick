package com.piyushkhairnar.flick.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.piyushkhairnar.flick.Adapters.MovieBriefsSmallAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Favourite;
import com.piyushkhairnar.flick.network.movies.MovieBrief;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteMoviesFragment extends Fragment {

    private RecyclerView mFavMoviesRecyclerView;
    private List<MovieBrief> mFavMovies;
    private MovieBriefsSmallAdapter mFavMoviesAdapter;

    private LinearLayout mEmptyLayout;

    public FavouriteMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_movies, container, false);

        mFavMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_fav_movies);
        mFavMovies = new ArrayList<>();
        mFavMoviesAdapter = new MovieBriefsSmallAdapter(getContext(), mFavMovies);
        mFavMoviesRecyclerView.setAdapter(mFavMoviesAdapter);
        mFavMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layout_recycler_view_fav_movies_empty);

        loadFavMovies();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFavMoviesAdapter.notifyDataSetChanged();
    }


    private void loadFavMovies() {
        List<MovieBrief> favMovieBriefs = Favourite.getFavMovieBriefs(getContext());
        if (favMovieBriefs.isEmpty()) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            return;
        }

        for (MovieBrief movieBrief : favMovieBriefs) {
            mFavMovies.add(movieBrief);
        }
        mFavMoviesAdapter.notifyDataSetChanged();
    }

}
