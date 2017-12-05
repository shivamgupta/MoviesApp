package com.example.android.moviesapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.Utilities.FavoriteMovieContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

public class MovieDetailsActivity extends AppCompatActivity {

    private Menu favoriteMenu;
    private Movie movie;
    Toast toast;
    boolean thisMovieIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verifyStoragePermissions(this, getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = (TextView) findViewById(R.id.title_textview);
        ImageView moviePoster = (ImageView) findViewById(R.id.poster_imageview);
        TextView movieReleaseDate = (TextView) findViewById(R.id.release_date_textview);
        TextView movieVotingAverage = (TextView) findViewById(R.id.voting_average_textview);
        TextView movieLanguage = (TextView) findViewById(R.id.original_laguage_textview);
        TextView movieOverview = (TextView) findViewById(R.id.overview_textview);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(getString(R.string.movie_parcel));

        thisMovieIsFavorite = isCurrentMovieFavorite();

        toast = Toast.makeText(this, "Add to your favorites?", Toast.LENGTH_SHORT);
        // Set all the views for a given movie
        movieTitle.setText(movie.getMovieTitle());
        movieReleaseDate.setText(movie.getMovieReleaseDate());
        movieVotingAverage.setText(movie.getMovieVoteAverage());
        movieLanguage.setText(movie.getMovieOriginalLanguage());
        movieOverview.setText(movie.getMovieOverview());

        if (thisMovieIsFavorite) {
            String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + File.separator +
                    "favoriteMovies" +
                    File.separator +
                    movie.getMovieId() +
                    ".jpg";

            Uri imageUri = Uri.fromFile(new File(file_path));

            System.out.println("Reading from - " + file_path);

            Picasso.with(getApplicationContext())
                .load(imageUri)
                .resize(getResources().getInteger(R.integer.poster_w185_int_width),
                        getResources().getInteger(R.integer.poster_w185_int_height))
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(moviePoster);
        } else {
            Picasso.with(getApplicationContext())
                .load(movie.getMoviePosterPath())
                .resize(getResources().getInteger(R.integer.poster_w185_int_width),
                        getResources().getInteger(R.integer.poster_w185_int_height))
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(moviePoster);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, favoriteMenu);

        favoriteMenu = menu;

        if (thisMovieIsFavorite)
        {
            favoriteMenu.add(Menu.NONE,
                    R.string.unfavorite_tag,
                    Menu.NONE,
                    null)
                    .setVisible(false)
                    .setIcon(R.drawable.hollow_star)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            favoriteMenu.add(Menu.NONE,
                    R.string.favorite_tag,
                    Menu.NONE,
                    null)
                    .setVisible(true)
                    .setIcon(R.drawable.solid_star)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            favoriteMenu.add(Menu.NONE,
                    R.string.unfavorite_tag,
                    Menu.NONE,
                    null)
                    .setVisible(true)
                    .setIcon(R.drawable.hollow_star)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            favoriteMenu.add(Menu.NONE,
                    R.string.favorite_tag,
                    Menu.NONE,
                    null)
                    .setVisible(false)
                    .setIcon(R.drawable.solid_star)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.unfavorite_tag:
                addMovieToDb();
                item.setVisible(false);
                favoriteMenu.findItem(R.string.favorite_tag).setVisible(true);
                return true;

            case R.string.favorite_tag:
                removeMovieFromDb();
                item.setVisible(false);
                favoriteMenu.findItem(R.string.unfavorite_tag).setVisible(true);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentMovieFavorite() {
        int currentMovieId = Integer.parseInt(movie.getMovieId());
        Cursor favoriteMovieCursor = getContentResolver().query(FavoriteMovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        int favoriteMovieCount = favoriteMovieCursor.getCount();
        favoriteMovieCursor.moveToFirst();
        for(int i = 0; i < favoriteMovieCount; i++){
            int idIndex = favoriteMovieCursor.getColumnIndex(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int savedMovieId = favoriteMovieCursor.getInt(idIndex);
            if (currentMovieId == savedMovieId)
                return true;
            boolean ifNextRowExists = favoriteMovieCursor.moveToNext();
            if(!ifNextRowExists)
                return false;
        }
        return false;
    }

    private void addMovieToDb(){
        saveImageOnPhonesExternalStorage();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getMovieOverview());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getMoviePosterPath());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getMovieReleaseDate());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_LANGAUGE, movie.getMovieOriginalLanguage());
        contentValues.put(FavoriteMovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getMovieVoteAverage());

        Uri uri = getContentResolver().insert(FavoriteMovieContract.MovieEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            toast.cancel();
            toast = Toast.makeText(this, "Added to your favorites.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void removeMovieFromDb(){
        removeImageFromPhonesExternalStorage(movie.getMovieId());
        String id = movie.getMovieId();
        Uri uri = FavoriteMovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        int movieDeleted = getContentResolver().delete(uri, null, null);

        if (movieDeleted > 0){
            toast.cancel();
            toast = Toast.makeText(this, "Removed from your favorites.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * TODO: Step 1 - CHECK THIS
     */
    private void saveImageOnPhonesExternalStorage(){
        Picasso.with(getApplicationContext())
            .load(movie.getMoviePosterPath())
            .into(new Target() {
                  @Override
                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                      try {
                          String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "favoriteMovies";
                          File myDir = new File(path);

                          if (!myDir.exists())
                              if (!myDir.mkdirs())
                                  Log.e("Log :: ", "Problem creating Image folder");

                          String name = movie.getMovieId() + ".jpg";
                          myDir = new File(myDir, name);
                          System.out.println("Saving to - " + myDir.toString());
                          FileOutputStream out = new FileOutputStream(myDir);
                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                          out.flush();
                          out.close();
                      } catch(Exception e){
                          e.printStackTrace();
                      }
                  }

                  @Override
                  public void onBitmapFailed(Drawable errorDrawable) {
                  }

                  @Override
                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                  }
            });
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity, Context context) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void removeImageFromPhonesExternalStorage(String movieId){
        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator +
                "favoriteMovies" +
                File.separator +
                movieId +
                ".jpg";

        File file = new File(file_path);

        if (file.exists() && file.canRead())
            file.delete();
    }

    public void goToVideo(View view) {
        Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
    }
}