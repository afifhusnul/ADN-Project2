package com.example.nusnafif.p02_popularmovie.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nusnafif.p02_popularmovie.model.Review;
import com.example.nusnafif.p02_popularmovie.model.ReviewsResponse;
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

public class FetchReviewData extends AsyncTask<Long,Void,List<Review>> {

    private final String LOG_TAG = FetchReviewData.class.getSimpleName();
    private Context context;
    private MovieListener<List<Review>> listener;

    public FetchReviewData(Context context, MovieListener<List<Review>> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Review> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MovieInterface service = retrofit.create(MovieInterface.class);
        Call<ReviewsResponse> call = service.getReviewsById(params[0],
                Constant.API_KEY);
        try {
            Response<ReviewsResponse> response = call.execute();
            ReviewsResponse reviews = response.body();
            return reviews.getReviews();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Fetching Review data error", e);
        }
        return null;
    }


    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);
        listener.onTaskComplete(reviews);

    }

}
