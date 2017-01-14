package com.example.nusnafif.p02_popularmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.nusnafif.p02_popularmovie.utils.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by NUSNAFIF on 1/4/2017.
 */

public class Trailer implements Parcelable {
    @SerializedName("id")
    private String trailerId;
    @SerializedName("key")
    private String trailerKey;
    @SerializedName("name")
    private String trailerName;
    @SerializedName("site")
    private String trailerSite;
    @SerializedName("size")
    private String trailerSize;

    private Trailer(){

    }

    public String getName() {
        return trailerName;
    }

    public String getKey() {
        return trailerKey;
    }

    public String getTrailerUrl() {
        return Constant.MOVIEDB_TRAILERS_VIDEOYOUTUBE + trailerKey;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            Trailer trailer = new Trailer();
            trailer.trailerId = source.readString();
            trailer.trailerKey = source.readString();
            trailer.trailerName = source.readString();
            trailer.trailerSite = source.readString();
            trailer.trailerSize = source.readString();
            return trailer;
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(trailerId);
        parcel.writeString(trailerKey);
        parcel.writeString(trailerName);
        parcel.writeString(trailerSite);
        parcel.writeString(trailerSize);
    }
}
