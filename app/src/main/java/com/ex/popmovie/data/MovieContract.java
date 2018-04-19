package com.ex.popmovie.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

// Class MovieContract define table name and hir column names
public final class MovieContract {
    public static final String AUTHORITY = "com.ex.popmovie.data";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    // Create class MovieTable:
    public static final class MovieTable implements BaseColumns {
        // Create table name and each of the db columns
        public final static String TABLE_NAME = "favMovies";
        public final static String COLUMN_ID_MOVIE = "idMovie";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_POSTER_PATH = "posterPath";
        public final static String COLUMN_OVERVIEW = "overview";
        public final static String COLUMN_VOTE = "vote";
        public final static String COLUMN_POP = "pop";
        public final static String COLUMN_RELEASE = "releaseDate";

        // Create Uri for table:
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(TABLE_NAME).build();
        // Create Uri
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
