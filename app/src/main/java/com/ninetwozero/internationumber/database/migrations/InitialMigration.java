package com.ninetwozero.internationumber.database.migrations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ninetwozero.internationumber.database.dao.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import se.emilsjolander.sprinkles.Migration;

public class InitialMigration extends Migration {
    private Context context;
    public InitialMigration(Context context) {
        this.context = context;
    }

    @Override
    protected void doMigration(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + Country.TABLE_NAME + " (" +
                "locale TEXT PRIMARY KEY," +
                "name TEXT," +
                "code INTEGER," +
                "prefixes TEXT" +
            ")"
        );

        insertCountriesToDatabase(sqLiteDatabase);
    }

    private void insertCountriesToDatabase(SQLiteDatabase database) {
        try {
            InputStream inputStream = context.getAssets().open("initial_countries_data.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (reader.ready()) {
                database.execSQL(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
