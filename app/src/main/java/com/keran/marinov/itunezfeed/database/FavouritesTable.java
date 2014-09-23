package com.keran.marinov.itunezfeed.database;

import android.database.sqlite.SQLiteDatabase;

import com.keran.marinov.itunezfeed.common.L;

public class FavouritesTable
{
    private static final String LOG_TAG = "FavouritesTable";
    private static final boolean LOGGING_ENABLED = true;
    public static final String FAVOURITES_TABLE= "TRIP_DETAIL_TABLE";
	public static final String FAVOURITES_SONG_ID = "_id";
	public static final String FAVOURITES_SONG_TITLE = "FAVOURITES_SONG_TITLE";
	public static final String FAVOURITES_SONG_IMAGE_URL = "FAVOURITES_SONG_IMAGE_URL";
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ FAVOURITES_TABLE
			+ "("
			+ FAVOURITES_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FAVOURITES_SONG_TITLE + " TEXT,"
			+ FAVOURITES_SONG_IMAGE_URL + " TEXT"
			+ ");";

    public static final String[] FIELD_LIST =
            {
                    FavouritesTable.FAVOURITES_SONG_ID,
                    FavouritesTable.FAVOURITES_SONG_TITLE,
                    FavouritesTable.FAVOURITES_SONG_IMAGE_URL
            };

	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) 
	{
		L.log(LOG_TAG,LOGGING_ENABLED,"onUpgrade");
		database.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE);
		onCreate(database);
	}

}
