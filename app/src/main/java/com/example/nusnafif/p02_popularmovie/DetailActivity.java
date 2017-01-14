package com.example.nusnafif.p02_popularmovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.nusnafif.p02_popularmovie.data.MovieContract;
import com.example.nusnafif.p02_popularmovie.fragment.DetailFragment;
import com.example.nusnafif.p02_popularmovie.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.favorite)
    FloatingActionButton favoriteButton;

    private Movie movieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.MOVIE_DETAILS, getIntent().getParcelableExtra(DetailFragment.MOVIE_DETAILS));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details, fragment).commit();
        }

        movieDetail = getIntent().getParcelableExtra(DetailFragment.MOVIE_DETAILS);

        setUpFavoriteButton();
    }

    private void setUpFavoriteButton() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_del_fav));
                } else {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_add_fav));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        favoriteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFavorite()) {
                            removeFromFavorites();
                            Snackbar snackAdd = Snackbar.make(v, "Movie removed from Favorites", Snackbar.LENGTH_LONG);
                            snackAdd.show();
                        } else {
                            markAsFavorite();
                            Snackbar snackDel = Snackbar.make(v, "Movie added as Favorites", Snackbar.LENGTH_LONG);
                            snackDel.show();
                        }
                    }
                });

    }

    private boolean isFavorite() {

        Cursor movieCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieDetail.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }

    }

    public void markAsFavorite() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                            movieDetail.getId());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                            movieDetail.getTitle());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                            movieDetail.getPoster());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                            movieDetail.getOverview());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                            movieDetail.getUserRating());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                            movieDetail.getReleaseDate(getBaseContext()));
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                            movieDetail.getBackdrop());
                    getContentResolver().insert(
                            MovieContract.MovieEntry.CONTENT_URI,
                            movieValues
                    );
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setUpFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void removeFromFavorites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieDetail.getId(), null);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setUpFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
