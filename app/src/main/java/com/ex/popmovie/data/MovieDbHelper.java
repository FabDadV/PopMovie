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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ex.popmovie.data.MovieContract.*;

public class MovieDbHelper extends SQLiteOpenHelper {

    // Create datebase file name:
    private static final String DB_NAME = "FavMovie.db";
    // If you change the database schema, you must increment the database version
    private static final int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieTable.TABLE_NAME + " (" +
                MovieTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieTable.COLUMN_ID_MOVIE + " TEXT NOT NULL, " +
                MovieTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieTable.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieTable.COLUMN_OVERVIEW + " TEXT, " +
                MovieTable.COLUMN_VOTE + " TEXT, " +
                MovieTable.COLUMN_POP + " TEXT, " +
                MovieTable.COLUMN_RELEASE + " TEXT" +
                ");";
        database.execSQL(SQL_CREATE_MOVIE_TABLE);
    }
    // Override the onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDbVersion, int newDbVersion) {
        // For now simply drop the table and create a new one. This means if you change the
        // DB_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        // Inside, execute a drop table query, and then call onCreate to re-create it
        db.execSQL("DROP TABLE IF EXISTS " + MovieTable.TABLE_NAME);
        // Recreate database:
        onCreate(db);
    }
}
