package com.ex.popmovie.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/* Utility functions to handle JSON data. */
public final class JsonUtils {
    public static String[] parseJson(String dataJsonStr) throws JSONException {
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
        String[] stringList;

        try {
            stringList = new String[dataArray.length()];
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject movieJSON = dataArray.getJSONObject(i);
                String strMovie = movieJSON.getString("id");
                String strTitle = movieJSON.getString("title");
                String strPosterPath = movieJSON.getString("poster_path");
                String strOverview = movieJSON.getString("overview");
                String strVote = movieJSON.getString("vote_count");
                String strPop = movieJSON.getString("popularity");
                String strReleaseDate = movieJSON.getString("release_date");

                stringList[i] = strPosterPath + "\n" +
                        "Title: " + strTitle + "\n" +
                        "Overview: " + strOverview + "\n" +
                        "Popularity: " + strPop + "; Rating: " + strVote + "\n" +
                        "ReleaseDate: " + strReleaseDate + "; ID: " + strMovie + "\n\n";
            }
            return stringList;
        }   catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}