
            android:screenOrientation="portrait"




package com.ex.popmovie;


    // show Trailer:
    private void showTrailer(String id) {
        String apiKey = getResources().getString(R.string.key_api);
        String queryType = BASE_URL + id + TRAILER;
        new QueryTrailer().execute(apiKey, queryType);


    }
    // Create a class that extends AsyncTask to perform network requests
    class QueryTrailer extends AsyncTask<String, Void, long[]> {
        // Override the doInBackground method to perform your network requests
        @Override
        protected int[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String apiKey = params[0];
            String queryType = params[1];
            URL requestUrl = NetworkUtils.buildUrl(apiKey, queryType);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                return JsonUtils.parseTrailer(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(long[] keyTrailer) {
            if (movieList != null) {
                recyclerView.setVisibility(View.VISIBLE);
                // Instead of iterating through every string, use recyclerViewAdapter.setList and pass in data
                recyclerViewAdapter.setList(movieList);
            } else {
                Toast.makeText(getActivity(), "ErrorQuery. Check out correct api_key", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	    public long[] parseTrailer(String dataJsonStr) throws JSONException {
        JSONObject dataJson = new JSONObject(dataJsonStr);
        /* Is there an error? */
        JSONArray dataArray = dataJson.getJSONArray("results");
        long keyTrailer[dataArray.length()];

        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject trailerJSON = dataArray.getJSONObject(i);
                keyTrailer[i] = Long.parse.String(trailerJSON.getString("key"));
            }
            return keyTrailer;
        }   catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }














    // show Reviews:
    private void showReviews(String id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + id + REVIEWS);
    }

    // Create a class that extends AsyncTask to perform network requests
    class showTrailer extends AsyncTask<String, Void, Movie[]> {
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


 