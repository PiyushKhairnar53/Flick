package com.piyushkhairnar.flick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piyushkhairnar.flick.Activities.MovieDetailActivity;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.Utils.Favourite;
import com.piyushkhairnar.flick.Utils.MovieGenres;
import com.piyushkhairnar.flick.network.movies.MovieBrief;

import java.util.List;

public class MovieBriefsLargeAdapter extends RecyclerView.Adapter<MovieBriefsLargeAdapter.MovieViewHolder> {

    private Context mContext;
    private List<MovieBrief> mMovies;

    public MovieBriefsLargeAdapter(Context context, List<MovieBrief> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_large, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_780 + mMovies.get(position).getBackdropPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);

        if (mMovies.get(position).getTitle() != null)
            holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
        else
            holder.movieTitleTextView.setText("");

        if (mMovies.get(position).getVoteAverage() != null && mMovies.get(position).getVoteAverage() > 0) {
            holder.movieRatingTextView.setVisibility(View.VISIBLE);
            holder.movieRatingTextView.setText(String.format("%.1f", mMovies.get(position).getVoteAverage()) + Constants.RATING_SYMBOL);
        } else {
            holder.movieRatingTextView.setVisibility(View.GONE);
        }

        setGenres(holder, mMovies.get(position));

        if (Favourite.isMovieFav(mContext, mMovies.get(position).getId())) {
            holder.movieFavImageButton.setImageResource(R.drawable.ic_favorite_fill);
            holder.movieFavImageButton.setEnabled(false);
        } else {
            holder.movieFavImageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.movieFavImageButton.setEnabled(true);
        }

    }

    private void setGenres(MovieViewHolder holder, MovieBrief movie) {
        String genreString = "";
        for (int i = 0; i < movie.getGenreIds().size(); i++) {
            if (movie.getGenreIds().get(i) == null) continue;
            if (MovieGenres.getGenreName(movie.getGenreIds().get(i)) == null) continue;
            genreString += MovieGenres.getGenreName(movie.getGenreIds().get(i)) + ", ";
        }
        if (!genreString.isEmpty())
            holder.movieGenreTextView.setText(genreString.substring(0, genreString.length() - 2));
        else
            holder.movieGenreTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public RelativeLayout imageLayout;
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public TextView movieRatingTextView;
        public TextView movieGenreTextView;
        public ImageButton movieFavImageButton;


        public MovieViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.image_layout_show_card);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_card);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            movieRatingTextView = (TextView) itemView.findViewById(R.id.text_view_rating_show_card);
            movieGenreTextView = (TextView) itemView.findViewById(R.id.text_view_genre_show_card);
            movieFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);

            imageLayout.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9);
            imageLayout.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.9) / 1.77);

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(Constants.MOVIE_ID, mMovies.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });

            movieFavImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addMovieToFav(mContext, mMovies.get(getAdapterPosition()).getId(), mMovies.get(getAdapterPosition()).getPosterPath(), mMovies.get(getAdapterPosition()).getTitle());
                    movieFavImageButton.setImageResource(R.drawable.ic_favorite_fill);
                    movieFavImageButton.setEnabled(false);
                }
            });
        }

    }

}
