package com.example.android.moviesapp;

import android.os.AsyncTask;

import com.example.android.moviesapp.Utilities.NetworkUtils;
import com.example.android.moviesapp.Utilities.TmdbJSONUtils;

import java.net.StandardSocketOptions;
import java.net.URL;

/**
 *  Fetches TMDB data asynchronously
 */

public class DataAsyncFetcher extends AsyncTask<String, Void, String> {
    private final String tmdbApiKey;

    public DataAsyncFetcher(String apiKey){
        tmdbApiKey = apiKey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String rawTmdbData = null;
        String movieId = params[0];
        URL tmdbRequestUrl = NetworkUtils.buildUrlForVideos(tmdbApiKey, movieId);
        try {
            rawTmdbData = NetworkUtils.getResponseFromHttpUrl(tmdbRequestUrl);
        } catch (Exception e){
            e.printStackTrace();
        }

        String trailerKey = "Unable to find trailer.";
        try{
            trailerKey = TmdbJSONUtils.getKeyFromRawVideoData(rawTmdbData);
        } catch (Exception e){
            e.printStackTrace();
        }

        return trailerKey;
    }

    @Override
    protected void onPostExecute(String key) {
        super.onPostExecute(key);
        MovieDetailsActivity.trailerVideoKey = key;
    }
}