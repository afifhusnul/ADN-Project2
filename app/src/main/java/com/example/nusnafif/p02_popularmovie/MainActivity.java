package com.example.nusnafif.p02_popularmovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.example.nusnafif.p02_popularmovie.fragment.MovieFragment;
import com.example.nusnafif.p02_popularmovie.utils.Constant;

import static com.example.nusnafif.p02_popularmovie.utils.Constant.API_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    MovieFragment fragment;
    private SharedPreferences prefs;
    private String orderBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*-- Check API KEY --*/
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }
        /* ----- */

        if (savedInstanceState == null) {
            fragment = new MovieFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        } else {
            fragment = (MovieFragment) getSupportFragmentManager().getFragment(savedInstanceState, "movieFragment");

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        getSupportFragmentManager().putFragment(savedInstanceState, "movieFragment", fragment);
    }

    /*-- Prepare Menu --*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(@Nullable Menu menu) {
        super.onPrepareOptionsMenu(menu);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        orderBy = prefs.getString(Constant.SORT_ORDER, Constant.SORTBY_DEFAULT_PARAM);

        if (orderBy != null) {
            switch (orderBy) {
                case Constant.TOP_RATED:
                    menu.findItem(R.id.menu_sort_top_rated).setChecked(true);
                    setTitle("Top Movies");
                    break;
                case Constant.FAVOURITE:
                    menu.findItem(R.id.menu_sort_favourite).setChecked(true);
                    setTitle("Favourites Movies");
                    break;
                case Constant.POPULAR:
                default:
                    menu.findItem(R.id.menu_sort_popular).setChecked(true);
                    setTitle("Popular Movies");
                    break;
            }
        }
        return true;
    }

}
