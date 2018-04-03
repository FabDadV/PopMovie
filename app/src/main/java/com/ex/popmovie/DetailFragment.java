package com.ex.popmovie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.popmovie.data.Movie;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String MOVIE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String EXTRA_OBJECT = "mark_movie";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView ivPoster = v.findViewById(R.id.iv_poster);
        TextView tvTitle = v.findViewById(R.id.tv_detail_title);
        TextView tvOverview = v.findViewById(R.id.tv_detail_overview);
        TextView tvVote = v.findViewById(R.id.tv_detail_vote);
        TextView tvPop = v.findViewById(R.id.tv_detail_pop);
        TextView tvReleaseDate = v.findViewById(R.id.tv_detail_release);
        TextView tvIdMovie = v.findViewById(R.id.tv_detail_id);

//        Intent intent = getActivity().getIntent();
        // (2) Display the data that was passed from MainActivity
//        if (intent == null) { closeOnError(); return; }
//        intent.getIntExtra(EXTRA_POSITION, position);
        Movie movieDetail = getActivity().getIntent().getParcelableExtra(EXTRA_OBJECT);

        String mPosterPath = movieDetail.getPosterPath();
        String mTitle = movieDetail.getTitle();
        String mOverview = movieDetail.getOverview();
        String mVote = movieDetail.getVote();
        String mPop = movieDetail.getPop();
        String mReleaseDate = movieDetail.getReleaseDate();
        String mIdMovie = movieDetail.getIdMovie();

        String posterUrl = MOVIE_URL + mPosterPath;
        Picasso.with(getContext())
                .load(posterUrl)
                .into(ivPoster);

        tvTitle.setText(mTitle);
        tvOverview.setText(mOverview);
        tvVote.setText(mVote);
        tvPop.setText(mPop);
        tvReleaseDate.setText(mReleaseDate);
        tvIdMovie.setText(mIdMovie);

        return v;
    }
}
