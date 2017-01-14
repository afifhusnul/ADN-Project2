package com.example.nusnafif.p02_popularmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class ReviewsResponse {

    @SerializedName("results")
    private List<Review> reviews = new ArrayList<>();
    public List<Review> getReviews() {
        return reviews;
    }

}