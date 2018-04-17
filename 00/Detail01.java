package com.ex.popmovie;

    private void showTrailer(String id) {
        Movie movie;
        getActivity().getContentResolver().query(MovieContract.MovieTable.)

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                + "c38r-SAnTWM")));
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


 