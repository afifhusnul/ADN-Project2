package com.example.nusnafif.p02_popularmovie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by NUSNAFIF on 12/28/2016.
 */

public class NetworkUtil {
    private final Context mContext;

    /* Constructor */
    public NetworkUtil(Context context) {
        mContext = context;
    }

    /*
    * Check the network connection
    */
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
