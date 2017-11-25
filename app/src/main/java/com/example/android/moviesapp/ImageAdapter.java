package com.example.android.moviesapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * ArrayAdapter for GridView
 */

public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private final Context mContext;
    private final Movie[] mMovies;

    public ImageAdapter(Context context, Movie[] movies) {
        mContext = context;
        mMovies = movies;

    }

    @Override
    public Movie getItem(int position) {
        if (mMovies == null || mMovies.length == 0) {
            return null;
        }

        return mMovies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        if (mMovies == null || mMovies.length == 0) {
            return -1;
        }

        return mMovies.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);
        ImageView imageView;

        // Adapters recycle views to AdapterViews.
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        if(movie.getMoviePosterPath() != null)
        {
            Picasso.with(mContext)
                    .load(movie.getMoviePosterPath())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.not_found)
                    .into(imageView);
        } else {
            Log.e(LOG_TAG, "Movie Poster Path is null");
        }

        return imageView;
    }
}