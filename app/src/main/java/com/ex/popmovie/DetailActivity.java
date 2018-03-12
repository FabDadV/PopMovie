package com.ex.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.popmovie.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String MOVIE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String EXTRA_OBJECT = "mark_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivPoster = findViewById(R.id.iv_poster);
        TextView tvPosterPath = findViewById(R.id.tv_detail_poster);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvOverview = findViewById(R.id.tv_detail_overview);
        TextView tvVote = findViewById(R.id.tv_detail_vote);
        TextView tvPop = findViewById(R.id.tv_detail_pop);
        TextView tvReleaseDate = findViewById(R.id.tv_detail_release);
        TextView tvIdMovie = findViewById(R.id.tv_detail_id);

        Intent intent = getIntent();
        // (2) Display the data that was passed from MainActivity
        if (intent == null) { closeOnError(); return; }
//        intent.getIntExtra(EXTRA_POSITION, position);
        Movie movieDetail = getIntent().getParcelableExtra(EXTRA_OBJECT);

        String mPosterPath = movieDetail.getPosterPath();
        String mTitle = movieDetail.getTitle();
        String mOverview = movieDetail.getOverview();
        String mVote = movieDetail.getVote();
        String mPop = movieDetail.getPop();
        String mReleaseDate = movieDetail.getReleaseDate();
        String mIdMovie = movieDetail.getIdMovie();

        String posterUrl = MOVIE_URL + mPosterPath;
        Picasso.with(this)
                .load(posterUrl)
                .into(ivPoster);

        tvPosterPath.setText(mPosterPath);
        tvTitle.setText(mTitle);
        tvOverview.setText(mOverview);
        tvVote.setText(mVote);
        tvPop.setText(mPop);
        tvReleaseDate.setText(mReleaseDate);
        tvIdMovie.setText(mIdMovie);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}