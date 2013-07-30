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

import java.io.IOException;

import novoda.lib.sqliteprovider.migration.Migrations;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ninetwozero.internationumber.Internationumber;

public class DatabaseManager extends SQLiteOpenHelper {
    private final Context mContext;
    
    public DatabaseManager(Context context) {
        super(context, Internationumber.NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            Migrations.migrate(database, mContext.getAssets(), "migrations");
        } catch (IOException e) {
            Log.e(DatabaseManager.class.getSimpleName(), "An error occured while migrating and/or upgrading the database.");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int from, int to) {
        onCreate(database);
    }
}
