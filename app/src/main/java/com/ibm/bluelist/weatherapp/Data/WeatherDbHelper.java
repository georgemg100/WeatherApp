package com.ibm.bluelist.weatherapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ibm.bluelist.weatherapp.Data.WeatherDbContract;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "Weather.db";

    private static final String SQL_CREATE_WEATHER_TABLE =
            "CREATE TABLE " + WeatherDbContract.WeatherEntry.TABLE_NAME + " (" +
                    WeatherDbContract.WeatherEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY + " TEXT," +
                    WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY_ID +" INTEGER," +
                    WeatherDbContract.WeatherEntry.COLUMN_NAME_REQUEST_TIME + " BIGINT)";

    private static final String SQL_CREATE_FORECAST_TABLE =
            "CREATE TABLE " + WeatherDbContract.ForecastEntry.TABLE_NAME + " (" +
                    WeatherDbContract.ForecastEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherDbContract.ForecastEntry.COLUMN_NAME_CITY_ID +" INTEGER," +
                    WeatherDbContract.ForecastEntry.COLUMN_NAME_CURRENT_TEMP +" REAL," +
                    WeatherDbContract.ForecastEntry.COLUMN_NAME_HIGH +" REAL," +
                    WeatherDbContract.ForecastEntry.COLUMN_NAME_LOW +" REAL," +
                    WeatherDbContract.ForecastEntry.COLUMN_NAME_CONDITIONS + " TEXT)";

    private static final String SQL_DELETE_WEATHER_ENTRY =
            "DROP TABLE IF EXISTS " + WeatherDbContract.WeatherEntry.TABLE_NAME;

    private static final String SQL_DELETE_FORECAST_ENTRY =
            "DROP TABLE IF EXISTS " + WeatherDbContract.ForecastEntry.TABLE_NAME;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().getName(), "creating table Weather: " + SQL_CREATE_WEATHER_TABLE);
        Log.i(this.getClass().getName(), "creating table Forecast: " + SQL_CREATE_FORECAST_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
        db.execSQL(SQL_CREATE_FORECAST_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_WEATHER_ENTRY);
        db.execSQL(SQL_DELETE_FORECAST_ENTRY);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
