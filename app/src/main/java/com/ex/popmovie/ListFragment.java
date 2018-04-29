package com.ex.popmovie;

import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ex.popmovie.data.Movie;
import com.ex.popmovie.data.MovieContract;
import com.ex.popmovie.utilities.JsonUtils;
import com.ex.popmovie.utilities.NetworkUtils;
import com.ex.popmovie.utilities.RecyclerViewAdapter;

import static com.ex.popmovie.DetailFragment.EXTRA_OBJECT;
/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler {
    private static final String TAG = " ****** ListFragment ";
    private static final String POPULAR = "/popular?";
    private static final String TOP_RATED = "/top_rated?";
    private static final String FAVORITE = "fav";
    private static final String MOVIE_SORT = "movie_sort";
    private static final String MARK_POSITION = "mark_position";
    private static final int DEFAULT_SIZE = 180;
    private String movieSort = POPULAR;
    public static int markPosition = 0;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ListFragment() {
        Log.d(TAG, "Constructor ListFragment called");
        // Required empty public constructor
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState called " + Integer.toString(markPosition) + movieSort);
        savedInstanceState.putString(MOVIE_SORT, movieSort);
        savedInstanceState.putInt(MARK_POSITION, markPosition);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            markPosition = savedInstanceState.getInt(MARK_POSITION);
            movieSort = savedInstanceState.getString(MOVIE_SORT);
            Log.d(TAG, "onViewStateRestored called " + Integer.toString(markPosition) + movieSort);
            recyclerView.scrollToPosition(markPosition);
/*
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
*/
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setHasOptionsMenu(true);
        if(!isInternet()) {
            Toast.makeText(getActivity(), R.string.detail_error_internet, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rv_list);
        Context context = getActivity();
        if(context == null) closeOnError();
        int numberOfColumns = calculateColumns(context);
        // Set the gridLayoutManager on recyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        Log.d(TAG, "onCreateView( called " + Integer.toString(markPosition) + movieSort);
        // Call Movies to perform the request
        if (savedInstanceState == null) {
            loadData(POPULAR);
        } else {
            // restore InstanceState from Bundle:
            markPosition = savedInstanceState.getInt(MARK_POSITION);
            movieSort = savedInstanceState.getString(MOVIE_SORT);
            if(movieSort.equals(FAVORITE)) {
                loadFavMovies();
            } else {
                loadData(movieSort);
            }
        }
        return view;
    }

    private void loadData(String queryType) {
        String apiKey = getResources().getString(R.string.key_api);
        new QueryAsyncTask().execute(apiKey, queryType);
    }
    // Create a class that extends AsyncTask to perform network requests
    public class QueryAsyncTask extends AsyncTask<String, Void, Movie[]> {
        // Override the doInBackground method to perform your network requests
        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String apiKey = params[0];
            String queryType = params[1];
            URL requestUrl = NetworkUtils.buildUrl(apiKey, queryType);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                return JsonUtils.parseJson(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        // Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(Movie[] movies) {
            bindRecycleView(movies);
        }
    }

    public void bindRecycleView(Movie[] movieList) {
        if (movieList != null) {
            recyclerView.setVisibility(View.VISIBLE);
            // Instead of iterating through every string, use recyclerViewAdapter.setList and pass in data
            recyclerViewAdapter.setList(movieList);
            Log.d(TAG, "bindRecycleView " + Integer.toString(markPosition));
            recyclerView.scrollToPosition(markPosition); // Thanks to @Tim for the solution
        } else {
            Toast.makeText(getActivity(), R.string.detail_error_query, Toast.LENGTH_SHORT).show();
        }
    }

    // Perform db request
    private void loadFavMovies() {
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        cursor.moveToFirst();
        Movie[] movieList = new Movie[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            movieList[i] = getMovie(cursor, i);
            cursor.moveToNext();
            i++;
        }
        cursor.close();

        if (movieList != null) {
            recyclerView.setVisibility(View.VISIBLE);
            // Instead of iterating through every string, use recyclerViewAdapter.setList and pass in data
            recyclerViewAdapter.setList(movieList);
        } else {
            Toast.makeText(getActivity(), R.string.detail_error_query, Toast.LENGTH_SHORT).show();
        }
    }

    private Movie getMovie(@NonNull Cursor cursor, int position) {
        Movie movie = null;
        if (cursor.moveToPosition(position)) {
            movie = new Movie();
            movie.setIdMovie(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_ID_MOVIE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_TITLE)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_POSTER_PATH)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_OVERVIEW)));
            movie.setVote(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_VOTE)));
            movie.setPop(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_POP)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_RELEASE)));
        }
        return movie;
    }
    // Override method in order to handle RecyclerView item clicks.
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        // Pass the data to the DetailActivity
        markPosition = position;
        Movie markMovie = this.recyclerViewAdapter.movieList[position];
        intent.putExtra(EXTRA_OBJECT, markMovie);
        startActivity(intent);
    }
    /* https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
     * calculate number of columns in GridLayoutManager
     */
    private static int calculateColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / DEFAULT_SIZE);
    }

    private boolean isInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private void closeOnError() {
        getActivity().finish();
        Toast.makeText(getContext(), R.string.detail_error_context, Toast.LENGTH_SHORT).show();
    }
    // Creating Menu: Popular TopRating Favorite and HomeButton
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pop:
                loadData(POPULAR);
                movieSort = POPULAR;
                markPosition = 0;
                return true;
            case R.id.menu_rate:
                loadData(TOP_RATED);
                movieSort = TOP_RATED;
                markPosition = 0;
                return true;
            case R.id.menu_fav:
                loadFavMovies();
                movieSort = FAVORITE;
                markPosition = 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}