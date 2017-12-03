package com.example.android.moviesapp.Utilities;

import com.example.android.moviesapp.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handles JSON Related Tasks
 */

public class TmdbJSONUtils {

    public static Movie[] getMoviesFromRawData(String rawTmdbData)  throws JSONException {
        // JSON TAGS
        final String TAG_RESULTS = "results";
        final String TAG_ID = "id";
        final String TAG_ORIGINAL_TITLE = "title";
        final String TAG_OVERVIEW = "overview";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_RELEASE_DATE = "release_date";
        final String TAG_ORIGINAL_LANGUAGE = "original_language";
        final String TAG_VOTE_AVERAGE = "vote_average";

        JSONObject tmdbJson = new JSONObject(rawTmdbData);
        JSONArray movieResultsArray = tmdbJson.getJSONArray(TAG_RESULTS);

        Movie[] movies = new Movie[movieResultsArray.length()];

        for (int i = 0; i < movieResultsArray.length(); i++) {
            movies[i] = new Movie();
            JSONObject movieInfo = movieResultsArray.getJSONObject(i);

            movies[i].setMovieId(movieInfo.getString(TAG_ID));
            movies[i].setMovieTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
            movies[i].setMovieOverview(movieInfo.getString(TAG_OVERVIEW));
            movies[i].setMoviePosterPath(movieInfo.getString(TAG_POSTER_PATH));
            movies[i].setMovieReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
            movies[i].setMovieOriginalLanguage(movieInfo.getString(TAG_ORIGINAL_LANGUAGE));
            movies[i].setMovieVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));
        }
        return movies;
    }
}