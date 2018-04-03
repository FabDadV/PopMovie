package com.ex.popmovie;

import android.support.v4.app.Fragment;

public class DetailActivity extends FragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DetailFragment();
    }
}