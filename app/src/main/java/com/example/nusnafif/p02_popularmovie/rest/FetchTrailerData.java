package com.example.nusnafif.p02_popularmovie.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nusnafif.p02_popularmovie.model.Trailer;
import com.example.nusnafif.p02_popularmovie.model.TrailersResponse;
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

public class FetchTrailerData extends AsyncTask<Long,Void,List<Trailer>> {

    private final String LOG_TAG = FetchTrailerData.class.getSimpleName();
    private Context context;
    private MovieListener<List<Trailer>> listener;

    public FetchTrailerData(Context context, MovieListener<List<Trailer>> listener) {
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected List<Trailer> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MovieInterface service = retrofit.create(MovieInterface.class);
        Call<TrailersResponse> call = service.getTrailersById(params[0],
                Constant.API_KEY);
        try {
            Response<TrailersResponse> response = call.execute();
            TrailersResponse trailers = response.body();
            return trailers.getTrailers();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Fetching trailer data error", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        listener.onTaskComplete(trailers);
    }
}
