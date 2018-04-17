package com.ex.popmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.popmovie.data.Movie;
import com.ex.popmovie.data.MovieContract;
import com.ex.popmovie.utilities.JsonUtils;
import com.ex.popmovie.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
//  implements LoaderManager.LoaderCallbacks<Cursor> ???
    private static final String BASE_URL = "https://www.themoviedb.org/movie/";
    private static final String YOUTUBE = "http://www.youtube.com/watch?v=";
    private static final String TRAILER = "/videos?";
    private static final String REVIEWS = "/reviews";

    private static final String MOVIE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String EXTRA_OBJECT = "mark_movie";
    // constant used to identify the Loader
    private static final int MOVIE_LOADER = 0;

    boolean isFav = false;
    String[] Keys = new String[20];
    Movie movieDetail;

    public DetailFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView ivPoster = view.findViewById(R.id.iv_poster);
        TextView tvTitle = view.findViewById(R.id.tv_detail_title);
        TextView tvOverview = view.findViewById(R.id.tv_detail_overview);
        TextView tvVote = view.findViewById(R.id.tv_detail_vote);
        TextView tvPop = view.findViewById(R.id.tv_detail_pop);
        TextView tvReleaseDate = view.findViewById(R.id.tv_detail_release);
        TextView tvIdMovie = view.findViewById(R.id.tv_detail_id);
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

        // Add ImageButton for Favorite movie:
        ImageButton imgButtonFav = view.findViewById(R.id.imgButton_fav);
        // check in Favorite movie
        if (checkIsFav(mIdMovie)) {
            imgButtonFav.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            imgButtonFav.setImageResource(R.drawable.ic_favorite_border_24dp);
        }
        imgButtonFav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateFavMovie(mIdMovie); // update Favorite movie to the database
                    }
                }
        );
        // Add ImageButton for preview Trailer:
        ImageButton imgButtonTrailer = view.findViewById(R.id.imgButton_trailer);
        imgButtonTrailer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTrailer(mIdMovie);
                    }
                }
        );
        // Add Button for Reviews:
        Button ButtonReviews = view.findViewById(R.id.b_reviews);
        ButtonReviews.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showReviews(mIdMovie);
                    }
                }
        );
        return view;
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
    // update Favorite movie database
    private boolean updateFavMovie(String id) {
        if(isFav) {
            isFav = false;
            deleteFavMovie(id);
        } else {
            isFav = true;
            saveFavMovie();
        }
        return isFav;
    }
    // saves Favorite movie information to the database
    private void saveFavMovie() {
        // create ContentValues object containing movie's key-value pairs
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieTable.COLUMN_TITLE, movieDetail.getTitle());
        contentValues.put(MovieContract.MovieTable.COLUMN_ID_MOVIE, movieDetail.getIdMovie());
        contentValues.put(MovieContract.MovieTable.COLUMN_POSTER_PATH, movieDetail.getPosterPath());
        contentValues.put(MovieContract.MovieTable.COLUMN_OVERVIEW, movieDetail.getOverview());
        contentValues.put(MovieContract.MovieTable.COLUMN_VOTE, movieDetail.getVote());
        contentValues.put(MovieContract.MovieTable.COLUMN_POP, movieDetail.getPop());
        contentValues.put(MovieContract.MovieTable.COLUMN_RELEASE, movieDetail.getReleaseDate());

        Uri newMovieUri = getActivity().getContentResolver().insert(
                MovieContract.MovieTable.CONTENT_URI, contentValues);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        ((ImageButton) fragment.getView().findViewById(R.id.imgButton_fav))
                .setImageResource(R.drawable.ic_favorite_red_24dp);
    }
    // saves Favorite movie information to the database
    private void deleteFavMovie(String id) {
        // delete movie with id from Favorite movie's database
        int rowsDelete = getActivity().getContentResolver().delete(MovieContract.MovieTable.CONTENT_URI,
                MovieContract.MovieTable.COLUMN_ID_MOVIE + " = ?" ,new String[] {id});

        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        ((ImageButton) fragment.getView().findViewById(R.id.imgButton_fav))
                .setImageResource(R.drawable.ic_favorite_border_24dp);
    }
    // show Trailer:
    private void showTrailer(String id) {
        String apiKey = getResources().getString(R.string.key_api);
        String queryType = "/" + id + TRAILER;
        new QueryTrailer().execute(apiKey, queryType);
//        QueryTrailer(apiKey, queryType, Keys);
        if(Keys[0] != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE + Keys[0])));
        }
    }
    // Create a class that extends AsyncTask to perform network requests
    class QueryTrailer extends AsyncTask<String, Void, String []> {
        // Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String apiKey = params[0];
            String queryType = params[1];
            URL requestUrl = NetworkUtils.buildUrl(apiKey, queryType);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                Keys = JsonUtils.parseTrailer(jsonResponse);
                return Keys;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
/*
   private void QueryTrailer(String key, String query, String[] Keys) {
        // Override the doInBackground method to perform your network requests
            URL requestUrl = NetworkUtils.buildUrl(key, query);
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                JsonUtils.parseTrailer(jsonResponse, Keys);
    }
*/
// show Reviews:
    private void showReviews(String id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + id + REVIEWS)));
    }
}
