package com.ex.popmovie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.popmovie.data.Movie;
import com.ex.popmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment         {
//  implements LoaderManager.LoaderCallbacks<Cursor> ???
    private static final String MOVIE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String EXTRA_OBJECT = "mark_movie";
    // constant used to identify the Loader
    private static final int MOVIE_LOADER = 0;

    boolean isFav = false;
    Movie movieDetail;

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
        movieDetail = getActivity().getIntent().getParcelableExtra(EXTRA_OBJECT);

        String mPosterPath = movieDetail.getPosterPath();
        String mTitle = movieDetail.getTitle();
        String mOverview = movieDetail.getOverview();
        String mVote = movieDetail.getVote();
        String mPop = movieDetail.getPop();
        String mReleaseDate = movieDetail.getReleaseDate();
        final String mIdMovie = movieDetail.getIdMovie();

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

        // Add Button for Favorite movie:
        ImageButton imgButtonFav = v.findViewById(R.id.imgButton_fav);
        // check in Favorite movie
        if (checkIsFav(mIdMovie)) {
            imgButtonFav.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            imgButtonFav.setImageResource(R.drawable.ic_favorite_border_24dp);
        };

        imgButtonFav.setOnClickListener(
                new View.OnClickListener() {
                    // displays the AddEditFragment when FAB is touched
                    @Override
                    public void onClick(View view) {
                        updateFavMovie(mIdMovie); // save Favorite movie to the database
                    }
                }
        );

        return v;
    }

    private boolean checkIsFav(String id) {
// check in movie with id is favorite
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null,
                MovieContract.MovieTable.COLUMN_ID_MOVIE + "=" + id,
                null,
                null);
        /* return true if the cursor is not empty */
        isFav =  (cursor.getCount() > 0);
        cursor.close();
        return isFav;
    }

    // update
    private boolean updateFavMovie(String id) {
        if(isFav) {
            isFav = false;
            deleteFavMovie(id);
        } else {
            isFav = true;
            saveFavMovie();
        };
        return isFav;
    }
    // saves Favorite movie information to the database
    private void saveFavMovie() {
        // create ContentValues object containing movie's key-value pairs
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieTable.COLUMN_TITLE, movieDetail.getTitle().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_ID_MOVIE, movieDetail.getIdMovie().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_POSTER_PATH, movieDetail.getPosterPath().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_OVERVIEW, movieDetail.getOverview().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_VOTE, movieDetail.getVote().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_POP, movieDetail.getPop().toString());
        contentValues.put(MovieContract.MovieTable.COLUMN_RELEASE, movieDetail.getReleaseDate().toString());

        Uri newMovieUri = getActivity().getContentResolver().insert(
                MovieContract.MovieTable.CONTENT_URI, contentValues);
    }

    // saves Favorite movie information to the database
    private void deleteFavMovie(String id) {
        // delete movie with id from Favorite movie's database
      int rowsDelete = getActivity().getContentResolver().delete(MovieContract.MovieTable.CONTENT_URI,
                MovieContract.MovieTable.COLUMN_ID_MOVIE + " = ?" ,new String[] {id});
    }
}
