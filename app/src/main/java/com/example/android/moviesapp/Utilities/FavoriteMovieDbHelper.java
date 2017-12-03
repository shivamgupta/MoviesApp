package com.example.android.moviesapp.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 2;

    // Constructor
    FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the movies database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteMovieContract.MovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID    + " INTEGER, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_LANGAUGE + " TEXT, " +
                FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}