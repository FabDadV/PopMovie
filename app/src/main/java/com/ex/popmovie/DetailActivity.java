package com.ex.popmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvDetail = findViewById(R.id.tv_detail);
        Intent intent = getIntent();
        // (2) Display the data that was passed from MainActivity
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                String stringDetail = intent.getStringExtra(Intent.EXTRA_TEXT);
                tvDetail.setText(stringDetail);
            }
        }
    }
}
