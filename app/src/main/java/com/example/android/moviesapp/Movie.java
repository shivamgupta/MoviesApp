package com.example.android.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sgupta on 11/16/17.
 * Reference - https://github.com/udacity/android-custom-arrayadapter/blob/parcelable/app/src/main/java/demo/example/com/customarrayadapter/AndroidFlavor.java
 */

public class Movie implements Parcelable {
    /**
     * EXAMPLE MOVIE OBJECT FROM THE MOVIE DB API
         {

            "vote_count": 1,
            "id": 486673,
            "video": false,
             "vote_average": 10,
             "title": "The Watchman Chronicles",
             "popularity": 1,
             "poster_path": "\/ceS3wnAXvlUzZclUpPcEumKRA24.jpg",
             "original_language": "en",
             "original_title": "The Watchman Chronicles",
             "genre_ids": [99],
             "backdrop_path": null,
             "adult": false,
             "overview": "Personal accounts of encounters with UFO's.",
             "release_date": "2017-03-03" (Format: yyyy-mm-dd)
         }
     */
    private String movieTitle;
    private String movieOverview;
    private String moviePosterPath;
    private String movieReleaseDate;
    private String movieOriginalLanguage;
    private Double movieVoteAverage;

    /**
     * Constructors
     */
    public Movie() {
    }

    private Movie(Parcel p) {
        movieTitle = p.readString();
        movieOverview = p.readString();
        moviePosterPath = p.readString();
        movieReleaseDate = p.readString();
        movieOriginalLanguage = p.readString();
        //movieVoteAverage = (Double) p.readValue(Double.class.getClassLoader());
    }

    /**
     * Setters
     */
    public void setMovieTitle(String title) {
        movieTitle = title;
    }

    public void setMovieOverview(String overview) {
        movieOverview = overview;
    }

    public void setMoviePosterPath(String posterPath){
        moviePosterPath = posterPath;
    }

    public void setMovieReleaseDate(String date){
        movieReleaseDate = date;
    }

    public void setMovieOriginalLanguage(String lang){
        movieOriginalLanguage = lang;
    }

    public void setMovieVoteAverage(Double avg){
        movieVoteAverage = avg;
    }

    /**
     * Getters
     */
    public String getMovieTitle() {
        if (movieTitle != null){
            return movieTitle;
        } else{
            return "Info not available";
        }

    }

    public String getMovieOverview() {
        if (movieOverview != null || movieOverview != "") {
            return movieOverview;
        } else{
            return "Info not available";
        }
    }

    public String getMoviePosterPath(){
        return "http://image.tmdb.org/t/p/w185" + moviePosterPath;
    }

    public String getMovieReleaseDate(){
        if(movieReleaseDate != null) {
            return movieReleaseDate;
        } else {
            return "Info not available";
        }
    }

    public String getMovieOriginalLanguage(){
        if (movieOriginalLanguage != null) {
            return movieOriginalLanguage.toUpperCase();
        } else{
            return "Info not available";
        }
    }

    public String getMovieVoteAverage(){
        if (movieVoteAverage != null) {
            return movieVoteAverage.toString();
        } else{
            return "0";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(movieOverview);
        parcel.writeString(moviePosterPath);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieOriginalLanguage);
        parcel.writeDouble(movieVoteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel p) {
            return new Movie(p);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
