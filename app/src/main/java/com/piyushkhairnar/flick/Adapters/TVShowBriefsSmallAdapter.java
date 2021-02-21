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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piyushkhairnar.flick.Activities.TvShowDetailActivity;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.Utils.Favourite;
import com.piyushkhairnar.flick.network.tvshows.TVShowBrief;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TVShowBriefsSmallAdapter extends RecyclerView.Adapter<TVShowBriefsSmallAdapter.TVShowViewHolder> {

    private Context mContext;
    private List<TVShowBrief> mTVShows;

    public TVShowBriefsSmallAdapter(Context context, List<TVShowBrief> tvShows) {
        mContext = context;
        mTVShows = tvShows;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TVShowViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_show_small, parent, false));
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {

        Glide.with(mContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342 + mTVShows.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.tvShowPosterImageView);

        if (mTVShows.get(position).getName() != null)
            holder.tvShowTitleTextView.setText(mTVShows.get(position).getName());
        else
            holder.tvShowTitleTextView.setText("");

        if (Favourite.isTVShowFav(mContext, mTVShows.get(position).getId())) {
            holder.tvShowFavImageButton.setImageResource(R.drawable.ic_favorite_fill);
            holder.tvShowFavImageButton.setEnabled(false);
        } else {
            holder.tvShowFavImageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.tvShowFavImageButton.setEnabled(true);
        }

        if (mTVShows.get(position).getReleaseDate() != null && !mTVShows.get(position).getReleaseDate().trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(mTVShows.get(position).getReleaseDate());
                holder.movieRelYear.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.movieRelYear.setText("");
        }

        if (mTVShows.get(position).getVoteAverage() != null && mTVShows.get(position).getVoteAverage() > 0) {
            holder.movieRatings.setVisibility(View.VISIBLE);
            holder.movieRatings.setText(String.format("%.1f", mTVShows.get(position).getVoteAverage()) + Constants.RATING_SYMBOL);
        } else {
            holder.movieRatings.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mTVShows.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        public CardView tvShowCard;
        public ImageView tvShowPosterImageView;
        public TextView tvShowTitleTextView;
        public ImageButton tvShowFavImageButton;
        public TextView movieRelYear;
        public TextView movieRatings;


        public TVShowViewHolder(View itemView) {
            super(itemView);
            tvShowCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            tvShowPosterImageView = (ImageView) itemView.findViewById(R.id.image_view_show_card);
            tvShowTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            tvShowFavImageButton = (ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);
            movieRelYear = (TextView)itemView.findViewById(R.id.text_view_year_show_card);
            movieRatings = (TextView)itemView.findViewById(R.id.text_view_rating_show_card);

            tvShowPosterImageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.31);
            tvShowPosterImageView.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.66);

            tvShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TvShowDetailActivity.class);
                    intent.putExtra(Constants.TV_SHOW_ID, mTVShows.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });

            tvShowFavImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addTVShowToFav(mContext, mTVShows.get(getAdapterPosition()).getId(), mTVShows.get(getAdapterPosition()).getPosterPath(), mTVShows.get(getAdapterPosition()).getName());
                    tvShowFavImageButton.setImageResource(R.drawable.ic_favorite_fill);
                    tvShowFavImageButton.setEnabled(false);
                }
            });
        }
    }

}
