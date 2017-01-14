package com.example.nusnafif.p02_popularmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nusnafif.p02_popularmovie.R;
import com.example.nusnafif.p02_popularmovie.model.Trailer;
import com.example.nusnafif.p02_popularmovie.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final static String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private final ArrayList<Trailer> movieTrailers;
    private Context context;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.movieTrailers = trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Trailer trailer = movieTrailers.get(position);
        final Context context = holder.mView.getContext();

        float paddingLeft = 0;
        if (position == 0) {
            paddingLeft = context.getResources().getDimension(R.dimen.detail_horizontal_padding);
        }

        float paddingRight = 0;
        if (position + 1 != getItemCount()) {
            paddingRight = context.getResources().getDimension(R.dimen.detail_horizontal_padding) / 2;
        }

        holder.mView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);
        holder.trailer = trailer;

        Log.i(LOG_TAG, " Info Trailer  : " + Constant.MOVIEDB_TRAILERS_VIDEOYOUTUBE_IMG + trailer.getKey() + "/0.jpg");
        Glide.with(context).load(Constant.MOVIEDB_TRAILERS_VIDEOYOUTUBE_IMG+ trailer.getKey() + "/0.jpg").into(holder.trailerThumbnail);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.trailer_thumbnail)
        ImageView trailerThumbnail;
        public Trailer trailer;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void add(List<Trailer> trailers) {
        movieTrailers.clear();
        movieTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> getTrailers() {
        return movieTrailers;
    }
}
