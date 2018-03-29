package com.ex.popmovie;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.popmovie.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends FragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DetailFragment();
    }
}