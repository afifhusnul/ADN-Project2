package com.example.nusnafif.p02_popularmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class MoviesResponse {
    @SerializedName("results")
    private List<Movie> movies = new ArrayList<>();
    public List<Movie> getMovies() {
        return movies;
    }
}
