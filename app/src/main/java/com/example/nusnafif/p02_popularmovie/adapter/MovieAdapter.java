package com.example.nusnafif.p02_popularmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nusnafif.p02_popularmovie.DetailActivity;
import com.example.nusnafif.p02_popularmovie.R;
import com.example.nusnafif.p02_popularmovie.data.MovieContract;
import com.example.nusnafif.p02_popularmovie.fragment.DetailFragment;
import com.example.nusnafif.p02_popularmovie.model.Movie;
import com.example.nusnafif.p02_popularmovie.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();

    private final List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        view.getLayoutParams().height = (int) (viewGroup.getWidth() / Constant.MOVIE_GRID_COLUMN *
                Movie.POSTER_ASPECT_RATIO);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Movie movieDetails = movieList.get(position);
                Intent I = new Intent(context, DetailActivity.class)
                        .putExtra(DetailFragment.MOVIE_DETAILS, movieDetails);
                context.startActivity(I);

            }
        });
        Glide.with(context).load(movieList.get(position).getPosterUrl(context)).fitCenter().into(viewHolder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private ImageView moviePoster;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            moviePoster = (ImageView) view.findViewById(R.id.thumbnail);
        }

    }

    public void add(List<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    public void add(Cursor cursor) {
        movieList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieContract.MovieEntry.COL_MOVIE_ID);
                String title = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_TITLE);
                String posterPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_POSTER_PATH);
                String overview = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_OVERVIEW);
                String rating = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
                String releaseDate = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
                String backdropPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_BACKDROP_PATH);
                Movie movie = new Movie(id, title, posterPath, overview, rating, releaseDate, backdropPath);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }
}
