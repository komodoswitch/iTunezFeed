package com.keran.marinov.itunezfeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;


public class ItunezFeedContentProviderManager {

    private Context context;
    private final String LOG_TAG = "ItunezFeedContentProviderManager";
    private boolean LOG_ENABLED = true;


    public ItunezFeedContentProviderManager(Context context) {
        this.context = context;

    }

    private boolean hasSongTitle(String songTitle) {
        String[] whereArg = {songTitle};
        String whereColumn = FavouritesTable.FAVOURITES_SONG_TITLE + "=?";
        String[] returnColumn = {FavouritesTable.FAVOURITES_SONG_ID};
        Cursor cursor = context.getContentResolver().query(ItunezFeedContentProvider.CONTENT_URI_FAVOURITES_TABLE, returnColumn, whereColumn, whereArg, null);
        if (cursor != null && cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public boolean addNewFavouriteSongTitle(String songTitle, String imageUrl) {
        if (!hasSongTitle(songTitle)) {
             ContentValues contentValues = new ContentValues();
            contentValues.put(FavouritesTable.FAVOURITES_SONG_TITLE, songTitle);
            contentValues.put(FavouritesTable.FAVOURITES_SONG_IMAGE_URL, imageUrl);
            context.getContentResolver().insert(ItunezFeedContentProvider.CONTENT_URI_FAVOURITES_TABLE, contentValues);
            return true;
        }
        else
            return false;


    }

    public void deleteFavouriteSongTitle(String songTitle) {
        String[] whereArg = { songTitle + "" };
        String whereColumn = FavouritesTable.FAVOURITES_SONG_TITLE + "=?";
        context.getContentResolver().delete(ItunezFeedContentProvider.CONTENT_URI_FAVOURITES_TABLE, whereColumn, whereArg);
    }


    public CursorLoader getFavouritesTableCursorLoader()
    {
        return new CursorLoader(context, ItunezFeedContentProvider.CONTENT_URI_FAVOURITES_TABLE,FavouritesTable.FIELD_LIST, null, null,null);
    }


    private void printOutFavouritesTable()
    {


            Cursor cursor = context.getContentResolver().query(ItunezFeedContentProvider.CONTENT_URI_FAVOURITES_TABLE, FavouritesTable.FIELD_LIST, null, null, null);

            if (cursor.moveToFirst())
            {
                do
                {
                    Log.i("FAVOURITES TABLE SONG ID :", cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_ID)) + "=================");
                    Log.i("FAVOURITES TABLE SONG TITLE :",cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_TITLE)) + "=================");
                    Log.i("FAVOURITES TABLE SONG IMAGE URL :",cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_IMAGE_URL)) + "=================");
                }
                while (cursor.moveToNext());
            }
            cursor.close();

    }


}
