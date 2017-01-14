package com.example.nusnafif.p02_popularmovie.rest;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public interface MovieListener<T> {
    public void onTaskComplete(T result);
}
