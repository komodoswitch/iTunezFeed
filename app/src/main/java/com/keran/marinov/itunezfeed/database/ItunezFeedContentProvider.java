package com.keran.marinov.itunezfeed.database;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;


public class ItunezFeedContentProvider extends ContentProvider {

    private static final String LOG_TAG = "ItunezFeedContentProvider";
    private static final boolean LOGGING_ENABLED = true;
	private ItunezFeedDatabaseHelper database;
    private static final int FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID = 10;
	private static final int FAVOURITES_TABLE_SINGLE_ROW_QUERY_SWITCH_ID = 20;
    private static final String AUTHORITY = "com.keran.marinov.itunezfeed.database";
    private static final String BASE_PATH_FAVOURITES_TABLE = "FAVOURITES_TABLE";
    private static final String CONTENT_PROTOCOL = "content://";
    private static final String RIGHT_SLASH = "/";
	public static final Uri CONTENT_URI_FAVOURITES_TABLE = Uri.parse(CONTENT_PROTOCOL + AUTHORITY + RIGHT_SLASH + BASE_PATH_FAVOURITES_TABLE);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
	{
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAVOURITES_TABLE, FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAVOURITES_TABLE + "/#", FAVOURITES_TABLE_SINGLE_ROW_QUERY_SWITCH_ID);
	}

	@Override
	public boolean onCreate() 
	{
		database = new ItunezFeedDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) 
	{
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
		switch (uriType) 
		{
		case FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID:
            checkFavouritesTableColumns(projection);
			queryBuilder.setTables(FavouritesTable.FAVOURITES_TABLE);
			break;
		case FAVOURITES_TABLE_SINGLE_ROW_QUERY_SWITCH_ID:
            checkFavouritesTableColumns(projection);
			queryBuilder.setTables(FavouritesTable.FAVOURITES_TABLE);
			queryBuilder.appendWhere(FavouritesTable.FAVOURITES_SONG_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
        SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		Uri returnUri=Uri.parse(BASE_PATH_FAVOURITES_TABLE + "/" + id);
		switch (uriType) 
		{
		case FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID:
			id = sqlDB.insert(FavouritesTable.FAVOURITES_TABLE, null, values);
			returnUri=Uri.parse(BASE_PATH_FAVOURITES_TABLE + "/" + id);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		String id = uri.getLastPathSegment();
		switch (uriType) 
		{
		case FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID:
			rowsDeleted = sqlDB.delete(FavouritesTable.FAVOURITES_TABLE, selection,selectionArgs);
			break;
		case FAVOURITES_TABLE_SINGLE_ROW_QUERY_SWITCH_ID:
            if (TextUtils.isEmpty(selection))
			    rowsDeleted = sqlDB.delete(FavouritesTable.FAVOURITES_TABLE,FavouritesTable.FAVOURITES_SONG_ID + "=" + id, null);
			else
			    rowsDeleted = sqlDB.delete(FavouritesTable.FAVOURITES_TABLE,FavouritesTable.FAVOURITES_SONG_ID + "=" + id + " and " + selection,selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) 
	{

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		String id = uri.getLastPathSegment();
		switch (uriType) 
		{
		case FAVOURITES_TABLE_ALL_ROWS_QUERY_SWITCH_ID:
			rowsUpdated = sqlDB.update(FavouritesTable.FAVOURITES_TABLE, values, selection,selectionArgs);
			break;
		case FAVOURITES_TABLE_SINGLE_ROW_QUERY_SWITCH_ID:
            if (TextUtils.isEmpty(selection))
			    rowsUpdated = sqlDB.update(FavouritesTable.FAVOURITES_TABLE, values,FavouritesTable.FAVOURITES_SONG_ID + "=" + id, null);
			else
			    rowsUpdated = sqlDB.update(FavouritesTable.FAVOURITES_TABLE, values,FavouritesTable.FAVOURITES_SONG_ID + "=" + id + " and " + selection,selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkFavouritesTableColumns(String[] projection)
	{
        if (projection != null)
		{
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(FavouritesTable.FIELD_LIST));
			if (!availableColumns.containsAll(requestedColumns))
			{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
