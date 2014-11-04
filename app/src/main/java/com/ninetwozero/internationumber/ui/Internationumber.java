package com.ninetwozero.internationumber.ui;

import android.app.Application;

import com.ninetwozero.internationumber.database.migrations.InitialMigration;

import se.emilsjolander.sprinkles.Sprinkles;

public class Internationumber extends Application {
    private static final String DB_NAME = "internationumber.db";

    @Override
    public void onCreate() {
        super.onCreate();

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext(), DB_NAME, 0);
        setupSerializers(sprinkles);
        setupMigrations(sprinkles);
    }

    private void setupSerializers(Sprinkles sprinkles) {
        //sprinkles.registerType(SoldierOverview.class, new SoldierOverviewSerializer());

    }

    private void setupMigrations(Sprinkles sprinkles) {
        sprinkles.addMigration(new InitialMigration(getApplicationContext()));
    }
}
