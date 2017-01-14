package com.example.nusnafif.p02_popularmovie.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nusnafif.p02_popularmovie.DetailActivity;
import com.example.nusnafif.p02_popularmovie.R;
import com.example.nusnafif.p02_popularmovie.adapter.ReviewAdapter;
import com.example.nusnafif.p02_popularmovie.adapter.TrailerAdapter;
import com.example.nusnafif.p02_popularmovie.model.Movie;
import com.example.nusnafif.p02_popularmovie.model.Review;
import com.example.nusnafif.p02_popularmovie.model.Trailer;
import com.example.nusnafif.p02_popularmovie.rest.FetchReviewData;
import com.example.nusnafif.p02_popularmovie.rest.FetchTrailerData;
import com.example.nusnafif.p02_popularmovie.rest.MovieListener;
import com.example.nusnafif.p02_popularmovie.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class DetailFragment extends Fragment {

    public static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String MOVIE_DETAILS = "MOVIE_DETAILS";
    public static final String MOVIE_TRAILERS = "MOVIE_TRAILERS";
    public static final String MOVIE_REVIEWS = "MOVIE_REVIEWS";

    private Movie movieDetail;

    List<Review> reviews = new ArrayList<Review>();
    List<Trailer> trailers = new ArrayList<Trailer>();

    @BindView(R.id.movie_title)
    TextView movieTitleView;
    @BindView(R.id.movie_overview)
    TextView mMovieOverviewView;
    @BindView(R.id.movie_release_date)
    TextView mMovieReleaseDateView;
    @BindView(R.id.movie_user_rating)
    TextView mMovieRatingView;

    @BindView(R.id.review_list)
    RecyclerView mRecyclerViewForReviews;

    @BindView(R.id.trailer_list)
    RecyclerView mRecyclerViewForTrailers;

    private ReviewAdapter reviewRecyclerAdapter;
    private TrailerAdapter trailerRecyclerAdapter;
    private ShareActionProvider mShareActionProvider;

    public DetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(MOVIE_DETAILS)) {
            movieDetail = getArguments().getParcelable(MOVIE_DETAILS);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof DetailActivity) {
            appBarLayout.setTitle(movieDetail.getTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Glide.with(activity).load(movieDetail.getBackdropUrl(getContext())).fitCenter().into(movieBackdrop);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        movieTitleView.setText(movieDetail.getTitle());
        mMovieOverviewView.setText(movieDetail.getOverview());
        mMovieRatingView.setText("  " + movieDetail.getUserRating());
        mMovieReleaseDateView.setText("  " + movieDetail.getReleaseDate(getActivity()));

        /*-- Trailers --*/
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        trailerRecyclerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        mRecyclerViewForTrailers.setAdapter(trailerRecyclerAdapter);
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        /*-- Reviews --*/
        reviewRecyclerAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        mRecyclerViewForReviews.setAdapter(reviewRecyclerAdapter);


        /*-- Fetch reviews --*/
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_REVIEWS)) {
            reviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS);
            reviewRecyclerAdapter.add(reviews);
        } else {
            if (new NetworkUtil(getContext()).isConnected()) {
                fetchReviews();
            } else {
                Toast.makeText(getContext(), "No Internet connection. Make your internet connection establish", Toast.LENGTH_SHORT).show();
            }

        }

        /*-- Fetch trailers --*/
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_TRAILERS)) {
            trailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILERS);
            trailerRecyclerAdapter.add(trailers);
        } else {
            if (new NetworkUtil(getContext()).isConnected()) {
                fetchTrailers();
            } else {
                Toast.makeText(getContext(), "No Internet connection. Make your internet connection establish", Toast.LENGTH_SHORT).show();
            }
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        ArrayList<Review> reviews = reviewRecyclerAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            outState.putParcelableArrayList(MOVIE_REVIEWS, reviews);
        }

        ArrayList<Trailer> trailers = trailerRecyclerAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(MOVIE_TRAILERS, trailers);
        }
    }

    private void fetchReviews() {
        FetchReviewData fetchMovieReviewTask = new FetchReviewData(getContext(),
                new MovieListener<List<Review>>() {
                    @Override
                    public void onTaskComplete(List<Review> result) {
                        reviews.clear();
                        reviews.addAll(result);
                        reviewRecyclerAdapter.add(reviews);
                    }
                });
        fetchMovieReviewTask.execute(movieDetail.getId());
    }


    private void fetchTrailers() {
        FetchTrailerData fetchTrailerDataTask = new FetchTrailerData(getContext(),
                new MovieListener<List<Trailer>>() {
                    @Override
                    public void onTaskComplete(List<Trailer> result) {
                        trailers.clear();
                        trailers.addAll(result);
                        trailerRecyclerAdapter.add(trailers);

                        if (trailerRecyclerAdapter.getItemCount() > 0) {
                            Trailer trailer = trailerRecyclerAdapter.getTrailers().get(0);
                            updateShareActionProvider(trailer);
                        }
                    }
                });
        fetchTrailerDataTask.execute(movieDetail.getId());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_detail, menu);
        MenuItem shareTrailerMenuItem = menu.findItem(R.id.share_trailer);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareTrailerMenuItem);
    }

    private void updateShareActionProvider(Trailer trailer) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, movieDetail.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailer.getName() + ": "
                + trailer.getTrailerUrl());
        mShareActionProvider.setShareIntent(sharingIntent);
    }

}

