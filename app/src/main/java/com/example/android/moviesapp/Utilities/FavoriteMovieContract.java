package com.example.android.moviesapp.Utilities;

import android.net.Uri;
import android.provider.BaseColumns;


public class FavoriteMovieContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.moviesapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory (table)
    public static final String PATH_MOVIES = "movies";

    /* MovieEntry is an inner class that defines the contents of the movies table */
    public static final class MovieEntry {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Movie table and column names
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_LANGAUGE = "language";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
    }
}
