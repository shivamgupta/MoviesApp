package com.example.android.moviesapp.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Handles Network Related Tasks
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String TMDB_BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?";
    private static final String TMDB_BASE_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String api_key, String sort_by) {
        String TMDB_BASE_URL = TMDB_BASE_URL_POPULAR;

        if(sort_by.equals("popularity")){
            TMDB_BASE_URL = TMDB_BASE_URL_POPULAR;
        } else if (sort_by.equals("vote_average")){
            TMDB_BASE_URL = TMDB_BASE_URL_TOP_RATED;
        }

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}