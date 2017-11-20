package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = (TextView) findViewById(R.id.title_textview);
        ImageView moviePoster = (ImageView) findViewById(R.id.poster_imageview);
        TextView movieReleaseDate = (TextView) findViewById(R.id.release_date_textview);
        TextView movieVotingAverage = (TextView) findViewById(R.id.voting_average_textview);
        TextView movieLanguage = (TextView) findViewById(R.id.original_laguage_textview);
        TextView movieOverview = (TextView) findViewById(R.id.overview_textview);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.movie_parcel));

        // Set all the views for a given movie
        movieTitle.setText(movie.getMovieTitle());
        movieReleaseDate.setText(movie.getMovieReleaseDate());
        movieVotingAverage.setText(movie.getMovieVoteAverage());
        movieLanguage.setText(movie.getMovieOriginalLanguage());
        movieOverview.setText(movie.getMovieOverview());

        Picasso.with(this)
                .load(movie.getMoviePosterPath())
                .resize(getResources().getInteger(R.integer.poster_w185_int_width),
                        getResources().getInteger(R.integer.poster_w185_int_height))
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(moviePoster);
    }
}