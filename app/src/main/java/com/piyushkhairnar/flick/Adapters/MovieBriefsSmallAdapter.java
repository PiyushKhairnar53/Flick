package com.piyushkhairnar.flick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.piyushkhairnar.flick.network.movies.MovieBrief;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieBriefsSmallAdapter extends RecyclerView.Adapter<MovieBriefsSmallAdapter.MovieViewHolder> {

    private Context mContext;
    private List<MovieBrief> mMovies;

    public MovieBriefsSmallAdapter(Context context, List<MovieBrief> movies) {
        mContext = context;
        mMovies = movies;
    }

    @NonNull
    @Override
    public MovieBriefsSmallAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_small, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBriefsSmallAdapter.MovieViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342 + mMovies.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImageView);

        if (mMovies.get(position).getTitle() != null)
            holder.movieTitleTextView.setText(mMovies.get(position).getTitle());
        else
            holder.movieTitleTextView.setText("");

        if (mMovies.get(position).getReleaseDate() != null && !mMovies.get(position).getReleaseDate().trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(mMovies.get(position).getReleaseDate());
                holder.movieRelYear.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.movieRelYear.setText("");
        }

        if (mMovies.get(position).getVoteAverage() != null && mMovies.get(position).getVoteAverage() > 0) {
            holder.movieRatings.setVisibility(View.VISIBLE);
            holder.movieRatings.setText(String.format("%.1f", mMovies.get(position).getVoteAverage()) + Constants.RATING_SYMBOL);
        } else {
            holder.movieRatings.setVisibility(View.GONE);
        }

        /*if (Favourite.isMovieFav(mContext, mMovies.get(position).getId())) {
            holder.movieFavImageButton.setImageResource(R.drawable.ic_favorite_fill);
            holder.movieFavImageButton.setEnabled(false);
        } else {
            holder.movieFavImageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.movieFavImageButton.setEnabled(true);
        }*/

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;
        public ImageButton movieFavImageButton;
        public TextView movieRelYear;
        public TextView movieRatings;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_card);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            movieFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);
            movieRelYear = (TextView)itemView.findViewById(R.id.text_view_year_show_card);
            movieRatings = (TextView)itemView.findViewById(R.id.text_view_rating_show_card);

            moviePosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            moviePosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

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
