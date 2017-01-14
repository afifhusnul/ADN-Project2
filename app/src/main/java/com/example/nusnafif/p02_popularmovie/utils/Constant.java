package com.example.nusnafif.p02_popularmovie.utils;

/**
 * Created by NUSNAFIF on 12/28/2016.
 */

public class Constant {
    /* use private Constructor to prevents instantiation of class */
    private Constant() {
    }

    /*
    * Base Url & Api Key
    */

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String API_KEY = "";  //Put your API key here*/
    public static final String MOVIEDB_PIC_SIZE_SMALL = "w500";
    public static final String MOVIEDB_PIC_SIZE_BIG = "w780";

    /*-- Review & Trailers --*/
    public static final String MOVIEDB_TRAILERS_VIDEOYOUTUBE = "http://www.youtube.com/watch?v=";
    public static final String MOVIEDB_TRAILERS_VIDEOYOUTUBE_IMG = "http://img.youtube.com/vi/";
    public static final String MOVIEDB_NO_RELEASE_DATE = "Unknown Release Date";


    /**
     * Menu
     */
    public static final String SORT_ORDER = "sortType";
    public static final String SORTBY_DEFAULT_PARAM = "popular";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVOURITE = "favorites";

    /*-- Grid Columns --*/
    public static final int MOVIE_GRID_COLUMN = 2;

}
