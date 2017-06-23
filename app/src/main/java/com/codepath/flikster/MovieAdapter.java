package com.codepath.flikster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flikster.models.Config;
import com.codepath.flikster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by anyazhang on 6/22/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies
    ArrayList<Movie> movies;
    // config needed for img urls
    Config config;

    // context for rendering
    Context context;


    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get context and create inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds inflated view with specific data element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get movie data at specified position
        Movie movie = movies.get(position);
        // populate view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // determine current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build url for poster image
        String imageUrl = null;
        // if in portrait mode, load the poster image
        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get correct placeholder and imageview for current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        // load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
            .into(imageView);
    }

    // returns total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create viewholder as static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;

        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position - won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for new activity
                Intent intent = new Intent (context, MovieDetailsActivity.class);

                String imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                intent.putExtra("imageUrl", imageUrl);

                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
