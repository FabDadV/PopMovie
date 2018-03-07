package com.ex.popmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.net.URL;

import com.ex.popmovie.utilities.RecyclerViewAdapter;
import com.ex.popmovie.utilities.NetworkUtils;
import com.ex.popmovie.utilities.JsonUtils;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler {
    // TODO (1) add parameter queryType
    //    private String queryType = "/popular?";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Set the layoutManager on recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        // Call loadData to perform the network request to get data
        loadData();
    }

    //    private void loadData() { new QueryAsyncTask().execute(apiKey, queryType); }
    private void loadData() {
        String apiKey = getResources().getString(R.string.key_api);
        new QueryAsyncTask().execute(apiKey);
    }

    // Create a class that extends AsyncTask to perform network requests
    private class QueryAsyncTask extends AsyncTask<String, Void, String[]> {
        // Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String apiKey = params[0];
//            String queryType = params[1];
//            URL requestUrl = NetworkUtils.buildUrl(apiKey, queryType);
            URL requestUrl = NetworkUtils.buildUrl(apiKey);
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
        protected void onPostExecute(String[] stringList) {
            if (stringList != null) {
                recyclerView.setVisibility(View.VISIBLE);
                // Instead of iterating through every string, use recyclerViewAdapter.setList and pass in data
                recyclerViewAdapter.setList(stringList);
            } else {
                Toast.makeText(MainActivity.this, "ErrorQuery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Override method in order to handle RecyclerView item clicks.
    public void onClick(String stringData) {
        Intent intent = new Intent(this, DetailActivity.class);
        // Pass the data to the DetailActivity
        intent.putExtra(Intent.EXTRA_TEXT, stringData);
        startActivity(intent);
    }
}