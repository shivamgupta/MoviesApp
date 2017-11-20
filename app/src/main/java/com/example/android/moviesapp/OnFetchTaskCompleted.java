package com.example.android.moviesapp;

/**
 * Interface for OnFetchTaskCompleted
 */

interface OnFetchTaskCompleted {
    void onMovieDataAsyncFetcherCompleted(Movie[] movies);
}