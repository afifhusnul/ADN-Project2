package com.example.nusnafif.p02_popularmovie.rest;

import com.example.nusnafif.p02_popularmovie.model.MoviesResponse;
import com.example.nusnafif.p02_popularmovie.model.ReviewsResponse;
import com.example.nusnafif.p02_popularmovie.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public interface MovieInterface {
    @GET("movie/{sort_by}")
    Call<MoviesResponse> discoverMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailersById(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviewsById(@Path("id") long movieId, @Query("api_key") String apiKey);
}
