package com.example.nusnafif.p02_popularmovie.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nusnafif.p02_popularmovie.R;
import com.example.nusnafif.p02_popularmovie.adapter.MovieAdapter;
import com.example.nusnafif.p02_popularmovie.data.MovieContract;
import com.example.nusnafif.p02_popularmovie.model.Movie;
import com.example.nusnafif.p02_popularmovie.rest.FetchMovieData;
import com.example.nusnafif.p02_popularmovie.rest.MovieListener;
import com.example.nusnafif.p02_popularmovie.utils.Constant;
import com.example.nusnafif.p02_popularmovie.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    private final String PREF_MOVIES_ORDER = "PREF_MOVIES_ORDER";
    private MovieAdapter movieRecyclerAdapter;

    private static final int FAVORITES_MOVIES_LOADER = 0;

    private SharedPreferences prefs;
    private String OrderBy;

    List<Movie> movies = new ArrayList<Movie>();

    public MovieFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        OrderBy = prefs.getString(Constant.SORT_ORDER, Constant.SORTBY_DEFAULT_PARAM);

        Log.i(LOG_TAG, "Info OnCreate orderBy :  "+OrderBy);

        if (savedInstanceState != null) {
            ArrayList<Movie> storedMovies = new ArrayList<Movie>();
            storedMovies = savedInstanceState.<Movie>getParcelableArrayList(PREF_MOVIES_ORDER);
            movies.clear();
            movies.addAll(storedMovies);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.movies_main_fragment, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        /*-- Grid column = 2 Col --*/
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),Constant.MOVIE_GRID_COLUMN));
        movieRecyclerAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());
        recyclerView.setAdapter(movieRecyclerAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> storedMovies = new ArrayList<Movie>();
        storedMovies.addAll(movies);
        outState.putParcelableArrayList(PREF_MOVIES_ORDER, storedMovies);

        Log.i(LOG_TAG, "Info onSaveInstanceState orderBy :  "+OrderBy);

        if (!OrderBy.equals("favorites")) {
            getActivity().getSupportLoaderManager().destroyLoader(FAVORITES_MOVIES_LOADER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(LOG_TAG, "Info onStart orderBy :  "+OrderBy);

        if (movies.size() > 0 && OrderBy.isEmpty()) {
            updatePosterAdapter();
        } else {
            if(new NetworkUtil(getContext()).isConnected()) {
                getMoviesData();
            } else {
                if(OrderBy.equals("favorites"))
                    getActivity().getSupportLoaderManager().initLoader(FAVORITES_MOVIES_LOADER, null, this);
                    Toast.makeText(getContext(), "No Internet connection. Make your internet connection establish", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                OrderBy = String.valueOf(Constant.POPULAR);
                getActivity().setTitle("Popuplar Movies");
                /*-- Load Movie Data -- POPULAR --*/
                getMoviesData();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;


            case R.id.menu_sort_top_rated:
                OrderBy = String.valueOf(Constant.TOP_RATED);
                getActivity().setTitle("Top Movies");
                /*-- Load Movie Data -- TOP RATED --*/
                getMoviesData();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;

            case R.id.menu_sort_favourite:
                OrderBy = String.valueOf(Constant.FAVOURITE);
                getActivity().setTitle("Favourites Movies");
                /*-- Load Movie Data -- FAVOURITE --*/
                getMoviesData();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieRecyclerAdapter.add(cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {  }

    private void getMoviesData() {
        Log.i(LOG_TAG, "Info getMoviesData orderBy :  "+OrderBy);
        if (!OrderBy.equals("favorites")) {
            FetchMovieData fetchMovieData = new FetchMovieData(getContext(),
                    new MovieListener<List<Movie>>() {
                        @Override
                        public void onTaskComplete(List<Movie> result) {
                            movies.clear();
                            movies.addAll(result);
                            updatePosterAdapter();
                        }
                    });
            fetchMovieData.execute(OrderBy);
        }else{
            getActivity().getSupportLoaderManager().initLoader(FAVORITES_MOVIES_LOADER, null, this);
        }
    }

    /*-- Update poster images --*/
    private void updatePosterAdapter() {
        movieRecyclerAdapter.add(movies);
    }

}
