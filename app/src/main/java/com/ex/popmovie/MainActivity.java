package com.ex.popmovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.URL;

import com.ex.popmovie.data.Movie;
import com.ex.popmovie.utilities.RecyclerViewAdapter;
import com.ex.popmovie.utilities.NetworkUtils;
import com.ex.popmovie.utilities.JsonUtils;

import static com.ex.popmovie.DetailActivity.EXTRA_OBJECT;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler {
    private static final int DEFAULT_SIZE = 180;
    private static final String POPULAR = "/popular?";
    private static final String TOP_RATED = "/top_rated?";
    private String queryType = TOP_RATED;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_data);
        int numberOfColumns = calculateColumns(this);
        // Set the gridLayoutManager on recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        // Call loadData to perform the network request to get data
        loadData(queryType);
    }

    private void loadData(String queryType) {
        String apiKey = getResources().getString(R.string.key_api);
        new QueryAsyncTask().execute(apiKey, queryType);
    }

    // Create a class that extends AsyncTask to perform network requests
    private class QueryAsyncTask extends AsyncTask<String, Void, Movie[]> {
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
                Toast.makeText(MainActivity.this, "ErrorQuery. Check out correct api_key", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Override method in order to handle RecyclerView item clicks.
    public void onClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        // Pass the data to the DetailActivity
        Movie markMovie = new Movie();
        markMovie = this.recyclerViewAdapter.movieList[position];
        intent.putExtra(EXTRA_OBJECT, markMovie);
        startActivity(intent);
    }
/* https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
 * calculate number of columns in GridLayoutManager
 */
    public static int calculateColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int numberColumns = (int) (dpWidth / DEFAULT_SIZE);
            return numberColumns;
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.menu_pop) {
            loadData(POPULAR);
            return true;
        }
        if (itemThatWasClickedId == R.id.menu_rate) {
            loadData(TOP_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}