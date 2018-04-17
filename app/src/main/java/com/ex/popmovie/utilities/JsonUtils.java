package com.ex.popmovie.utilities;

import com.ex.popmovie.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/* Utility functions to handle JSON data. */
public final class JsonUtils {
    public static Movie[] parseJson(String dataJsonStr) throws JSONException {
        final String CUR_HTTP_CODE = "cod";
        JSONObject dataJson = new JSONObject(dataJsonStr);
        /* Is there an error? */
        if (dataJson.has(CUR_HTTP_CODE)) {
            int errorCode = dataJson.getInt(CUR_HTTP_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray dataArray = dataJson.getJSONArray("results");
        Movie movies[] = new Movie[dataArray.length()];

        try {
            for (int i = 0; i < dataArray.length(); i++) {
                Movie m = new Movie();
                JSONObject movieJSON = dataArray.getJSONObject(i);
                m.setIdMovie(movieJSON.getString("id"));
                m.setTitle(movieJSON.getString("title"));
                m.setOverview(movieJSON.getString("overview"));
                m.setPosterPath(movieJSON.getString("poster_path"));
                m.setVote(movieJSON.getString("vote_average"));
                m.setPop(movieJSON.getString("popularity"));
                m.setReleaseDate(movieJSON.getString("release_date"));
                movies[i] = m;
            }
            return movies;
        }   catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String[] parseTrailer(String dataJsonStr) throws JSONException {
        JSONObject dataJson = new JSONObject(dataJsonStr);
        /* Is there an error? */
        JSONArray dataArray = dataJson.getJSONArray("results");
        String[] keyTrailer = new String[dataArray.length()];
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject trailerJSON = dataArray.getJSONObject(i);
                keyTrailer[i] = trailerJSON.getString("key");
            }
            return keyTrailer;
        }   catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}