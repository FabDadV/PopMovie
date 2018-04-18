/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 ** Deitel & Associates, Inc. 2016 - Android 6 for Programmers (Samples) **
 **************************************************************************/
package com.ex.popmovie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import android.support.annotation.NonNull;

import com.ex.popmovie.R;
import com.ex.popmovie.data.MovieContract.MovieTable;

public class DbContentProvider extends ContentProvider {
    private MovieDbHelper dbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int ONE_MOVIE = 1;
    private static final int MOVIES = 2;

    static {
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieTable.TABLE_NAME + "/#", ONE_MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieTable.TABLE_NAME, MOVIES);
    }

    public DbContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // Implement this to initialize your content provider on startup.
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Implement this to handle query requests from clients,
        // create SQLiteQueryBuilder for querying movies table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieTable.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE: // movie with specified id will be selected
                queryBuilder.appendWhere(
                        MovieTable._ID + "=" + uri.getLastPathSegment());
                break;
            case MOVIES: // all movies will be selected
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }
        // execute the query to select one or all movies
        Cursor cursor = queryBuilder.query(dbHelper.getWritableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        // configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.
        Uri newMovieUri;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                // insert the new movie--success yields new movie's row id
                long rowId = dbHelper.getWritableDatabase().insert(
                        MovieTable.TABLE_NAME, null, values);
                // if the movie was inserted, create an appropriate Uri;
                // otherwise, throw an exception
                if (rowId > 0) { // SQLite row IDs start at 1
                    newMovieUri = MovieTable.buildMovieUri(rowId);
                    // notify observers that the database changed
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }
        return newMovieUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Implement this to handle requests to update one or more rows.
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                // get from the uri the id of movie to update
                String id = uri.getLastPathSegment();
                // update the row
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        MovieTable.TABLE_NAME, values, MovieTable._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }
        // if changes were made, notify observers that the database changed
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // delete the movie
        return dbHelper.getWritableDatabase().delete(
                MovieTable.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // Implement this to handle requests for the MIME type of the data at the given URI.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
