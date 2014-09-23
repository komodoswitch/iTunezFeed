package com.keran.marinov.itunezfeed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ItunezFeedDatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "ItunezFeedDatabaseHelper";
    private static final boolean LOGGING_ENABLED = true;
    private static final String DATABASE_NAME = "faves.db";
    private static final int DATABASE_VERSION = 1;

    public ItunezFeedDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        FavouritesTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

}
