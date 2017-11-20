package com.example.android.moviesapp;

import android.os.AsyncTask;

import com.example.android.moviesapp.Utilities.NetworkUtils;
import com.example.android.moviesapp.Utilities.TmdbJSONUtils;

import java.net.StandardSocketOptions;
import java.net.URL;

/**
 *  Fetches TMDB data asynchronously
 */

public class MovieDataAsyncFetcher extends AsyncTask<String, Void, Movie[]> {
    private final String tmdbApiKey;
    private final OnFetchTaskCompleted uiListener;

    public MovieDataAsyncFetcher(String apiKey, OnFetchTaskCompleted listener){
        tmdbApiKey = apiKey;
        uiListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String rawTmdbData = null;
        String sort_by = params[0];
        URL tmdbRequestUrl = NetworkUtils.buildUrl(tmdbApiKey, sort_by);
        try {
            rawTmdbData = NetworkUtils.getResponseFromHttpUrl(tmdbRequestUrl);
        } catch (Exception e){
            e.printStackTrace();
        }

        Movie[] movies = null;
        try{
            movies = TmdbJSONUtils.getMoviesFromRawData(rawTmdbData);
        } catch (Exception e){
            e.printStackTrace();
        }

        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        // Notify UI
        uiListener.onMovieDataAsyncFetcherCompleted(movies);
    }
}