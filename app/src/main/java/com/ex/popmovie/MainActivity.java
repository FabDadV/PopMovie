package com.ex.popmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String POPULAR = "/popular?";
    private static final String TOP_RATED = "/top_rated?";
    private String queryType = TOP_RATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add FragmentManager for Fragment with RecycleView
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        // Call loadData to perform the network request to get data
        loadData(queryType);
    }

    private void loadData(String queryType) {
        String apiKey = getResources().getString(R.string.key_api);
        new ListFragment.QueryAsyncTask().execute(apiKey, queryType);
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