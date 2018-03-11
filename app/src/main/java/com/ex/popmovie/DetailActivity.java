package com.ex.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.popmovie.data.Movie;
import com.ex.popmovie.utilities.RecyclerViewAdapter;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITON = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvPosterPath = findViewById(R.id.tv_detail_poster);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvOverview = findViewById(R.id.tv_detail_overview);
        TextView tvVote = findViewById(R.id.tv_detail_vote);
        TextView tvPop = findViewById(R.id.tv_detail_pop);
        TextView tvReleaseDate = findViewById(R.id.tv_detail_release);
        TextView tvIdMovie = findViewById(R.id.tv_detail_id);

        Intent intent = getIntent();
        // (2) Display the data that was passed from MainActivity
//        if (intent == null) { closeOnError(); } ;
        int p = intent.getIntExtra(EXTRA_POSITON,DEFAULT_POSITION);

        Movie movieDetail = movieList [p];
        tvPosterPath.setText(movieDetail.getPosterPath());
        tvTitle.setText(movieDetail.getTitle());
        tvOverview.setText(movieDetail.getOverview());
        tvVote.setText(movieDetail.getVote());
        tvPop.setText(movieDetail.getPop());
        tvReleaseDate.setText(movieDetail.setReleaseDate());
        tvIdMovie.setText(movieDetail.getIdMovie());
    }

/*
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
*/

}

