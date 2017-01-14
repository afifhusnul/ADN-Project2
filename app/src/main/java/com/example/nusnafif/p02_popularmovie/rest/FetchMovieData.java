package com.example.nusnafif.p02_popularmovie.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nusnafif.p02_popularmovie.model.Movie;
import com.example.nusnafif.p02_popularmovie.model.MoviesResponse;
import com.example.nusnafif.p02_popularmovie.utils.Constant;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();


    private Context context;
    private MovieListener<List<Movie>> listener;

    public FetchMovieData(Context context, MovieListener<List<Movie>> listener) {
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieInterface service = retrofit.create(MovieInterface.class);
        Call<MoviesResponse> call = service.discoverMovies(params[0],
                Constant.API_KEY);

        try {
            Response<MoviesResponse> response = call.execute();
            MoviesResponse movies = response.body();
            return movies.getMovies();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Fetching Movie data error", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }

}
