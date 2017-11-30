package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridView moviePosterGridView;
    private Menu preferenceMenu;
    private static final String API_KEY = BuildConfig.API_KEY;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviePosterGridView = (GridView) findViewById(R.id.gridview);
        moviePosterGridView.setOnItemClickListener(moviePosterClickListener);

        toast = Toast.makeText(this, "Sorted by Popularily", Toast.LENGTH_SHORT);

        if (savedInstanceState == null) {
            getMoviesFromTmdb();
        } else {
            Parcelable[] parcelable = savedInstanceState.getParcelableArray(getString(R.string.movie_parcel));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }
                moviePosterGridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, preferenceMenu);

        preferenceMenu = menu;

        preferenceMenu.add(Menu.NONE,
                R.string.pref_sort_popular_tag,
                Menu.NONE,
                null)
                .setVisible(false)
                .setIcon(R.drawable.popular)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        preferenceMenu.add(Menu.NONE,
                R.string.pref_sort_vote_avg_tag,
                Menu.NONE,
                null)
                .setVisible(false)
                .setIcon(R.drawable.vote)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        updateMenu();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovieObjects = moviePosterGridView.getCount();
        if (numMovieObjects > 0) {
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) moviePosterGridView.getItemAtPosition(i);
            }
            outState.putParcelableArray(getString(R.string.movie_parcel), movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.pref_sort_popular_tag:
                updateSharedPrefs(getString(R.string.tmdb_sort_by_popularity));
                updateMenu();
                getMoviesFromTmdb();
                return true;

            case R.string.pref_sort_vote_avg_tag:
                updateSharedPrefs(getString(R.string.tmdb_sort_by_vote_average));
                updateMenu();
                getMoviesFromTmdb();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(position);
            System.out.println(movie.getMovieTitle());
            Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
            intent.putExtra(getResources().getString(R.string.movie_parcel), movie);
            startActivity(intent);
        }
    };

    private void getMoviesFromTmdb() {
        // First check if network is available
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (networkAvailable) {
            String apiKey = API_KEY;

            OnFetchTaskCompleted taskCompleted = new OnFetchTaskCompleted() {
                @Override
                public void onMovieDataAsyncFetcherCompleted(Movie[] movies) {
                    moviePosterGridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));
                }
            };

            MovieDataAsyncFetcher movieTask = new MovieDataAsyncFetcher(apiKey, taskCompleted);
            movieTask.execute(getSortMethod());
        } else {
            Toast.makeText(this, getString(R.string.need_internet_error), Toast.LENGTH_LONG).show();
        }
    }

    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.tmdb_sort_by_tag), getString(R.string.tmdb_sort_by_popularity));
    }

    private void updateMenu() {
        String sortMethod = getSortMethod();

        if (sortMethod.equals(getString(R.string.tmdb_sort_by_popularity))) {
            preferenceMenu.findItem(R.string.pref_sort_popular_tag).setVisible(false);
            preferenceMenu.findItem(R.string.pref_sort_vote_avg_tag).setVisible(true);
            toast.cancel();
            toast = Toast.makeText(this, "Sorted by Popularily", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            preferenceMenu.findItem(R.string.pref_sort_popular_tag).setVisible(true);
            preferenceMenu.findItem(R.string.pref_sort_vote_avg_tag).setVisible(false);
            toast.cancel();
            toast = Toast.makeText(this, "Sorted by Vote Average", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void updateSharedPrefs(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.tmdb_sort_by_tag), sortMethod);
        editor.apply();
    }
}
