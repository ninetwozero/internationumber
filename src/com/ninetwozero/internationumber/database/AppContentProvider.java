/*
	This file is part of Internationumber

    Internationumber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Internationumber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.ninetwozero.internationumber.database;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class AppContentProvider extends ContentProvider {
    private DatabaseManager mDatabaseManager;
    private SQLiteDatabase mDatabase;

    public synchronized SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabaseManager = new DatabaseManager(getContext());
            mDatabase = mDatabaseManager.getWritableDatabase();
        }
        return mDatabase;
    }

    @Override
    public boolean onCreate() {
        getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    	final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getType(uri));
        final SQLiteDatabase db = getDatabase();
        final Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (UriFactory.URI_MATCHER.match(uri)) {
	    	case UriFactory.URI_CODES.COUNTRIES:
	    		return UriFactory.URI_PATH.COUNTRIES;
	    	case UriFactory.URI_CODES.BACKUP:
	    		return UriFactory.URI_PATH.BACKUP;
            default:
                return "";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final long id = mDatabase.replaceOrThrow(getType(uri), null, contentValues);
    	getContext().getContentResolver().notifyChange(uri, null);
    	return Uri.parse(getType(uri) + "/" + id);
    }

    @Override
    public int delete(Uri uri, String where, String[] selection) {
    	final int status = mDatabase.delete(getType(uri), where, selection);
    	getContext().getContentResolver().notifyChange(uri, null);
    	return status;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
    	final int status = mDatabase.update(getType(uri), contentValues, s, strings);
    	getContext().getContentResolver().notifyChange(uri, null);
        return status;
    }
}
