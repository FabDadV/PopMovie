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
    private static final String POPULAR = "/popular?";
    private static final String TOP_RATED = "/top_rated?";
    private static final String MOVIE_SORT = "movie_sort";
    private static final int DEFAULT_SIZE = 180;
    private String movieSort = POPULAR;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOVIE_SORT, movieSort);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(!isInternet()) {
            Toast.makeText(getActivity(), "No Internet. Check out Internet connection", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rv_list);
        Context context = getContext();
        if(context == null) closeOnError();
        int numberOfColumns = calculateColumns(getContext());
        // Set the gridLayoutManager on recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        // Call Movies to perform the request
        if (savedInstanceState == null) {
            loadData(POPULAR);
        } else {
            movieSort = savedInstanceState.getString(MOVIE_SORT);
            if(movieSort == "fav") {
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
    class QueryAsyncTask extends AsyncTask<String, Void, Movie[]> {
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
        protected void onPostExecute(Movie[] movieList) {
            if (movieList != null) {
                recyclerView.setVisibility(View.VISIBLE);
                // Instead of iterating through every string, use recyclerViewAdapter.setList and pass in data
                recyclerViewAdapter.setList(movieList);
            } else {
                Toast.makeText(getActivity(), "ErrorQuery. Check out correct api_key", Toast.LENGTH_SHORT).show();
            }
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
            Toast.makeText(getActivity(), "ErrorQuery. Check out correct api_key", Toast.LENGTH_SHORT).show();
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
                return true;
            case R.id.menu_rate:
                loadData(TOP_RATED);
                movieSort = TOP_RATED;
                return true;
            case R.id.menu_fav:
                loadFavMovies();
                movieSort = "fav";
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}