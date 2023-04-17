package com.example.revuproject;

import android.app.Application;

import com.example.revuproject.database.DatabaseManager;
import com.yandex.mapkit.MapKitFactory;

public class ApplicationExtended extends Application {

    private final static String MAPKIT_KEY = "b19bb6d9-b349-4a7c-9c86-c07ba8140f7d";

    @Override
    public void onCreate() {
        super.onCreate();

        //DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
        //DatabaseManager.getInstance(getApplicationContext());
        MapKitFactory.setApiKey(MAPKIT_KEY);
    }
}
