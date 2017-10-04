package com.ibm.bluelist.weatherapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.View.MainApplication;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelgeorgescu on 10/3/17.
 */
/**
 * Saves weather for a specific city and it's 5 day forecast to the SQLite database but
 * but currently only pulls the city name from the db to get latest search.
 */
public class WeatherLocalDatasource {
    WeatherDbHelper weatherDbHelper;

    public WeatherLocalDatasource() {
        Log.i(this.getClass().getCanonicalName(), "Context: " + MainApplication.context);
        weatherDbHelper = new WeatherDbHelper(MainApplication.context);
    }

    public void insert(Weather weather) {
        SQLiteDatabase db = weatherDbHelper.getWritableDatabase();

        //clear out table and store latest entry
        String rawQuery = "DELETE FROM " + WeatherDbContract.WeatherEntry.TABLE_NAME;
        String rawQuery2 = "DELETE FROM " + WeatherDbContract.ForecastEntry.TABLE_NAME;
        db.rawQuery(rawQuery, null);
        db.rawQuery(rawQuery2, null);

        //insert Weather for current city in db with its 5 day - 3 hour forecasts
        ContentValues values = new ContentValues();
        values.put(WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY, weather.getCity());
        values.put(WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY_ID, weather.getCityId());
        values.put(WeatherDbContract.WeatherEntry.COLUMN_NAME_REQUEST_TIME, weather.getRequestTime());

        db.insert(WeatherDbContract.WeatherEntry.TABLE_NAME, null, values);

        ContentValues valuesForecasts = new ContentValues();
        for(Forecast forecast : weather.getForecasts()) {
            valuesForecasts.put(WeatherDbContract.ForecastEntry.COLUMN_NAME_CITY_ID, weather.getCityId());
            valuesForecasts.put(WeatherDbContract.ForecastEntry.COLUMN_NAME_CURRENT_TEMP, forecast.getCurrentTemp());
            valuesForecasts.put(WeatherDbContract.ForecastEntry.COLUMN_NAME_LOW, forecast.getLow());
            valuesForecasts.put(WeatherDbContract.ForecastEntry.COLUMN_NAME_HIGH, forecast.getHigh());
            valuesForecasts.put(WeatherDbContract.ForecastEntry.COLUMN_NAME_CONDITIONS, forecast.getConditions());
            db.insert(WeatherDbContract.ForecastEntry.TABLE_NAME, null, valuesForecasts);
        }
        db.close();
    }

    public Weather getLastSavedWeather() { //not used
        Weather weather = new Weather();
        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast = new Forecast();
        SQLiteDatabase db = weatherDbHelper.getReadableDatabase();
        String rawQuery = "SELECT * FROM " +
                WeatherDbContract.WeatherEntry.TABLE_NAME + " w " +
                "INNER JOIN " + WeatherDbContract.ForecastEntry.TABLE_NAME + " f " +
                "ON w." + WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY_ID + " = f." + WeatherDbContract.ForecastEntry.COLUMN_NAME_CITY_ID +
                " ORDER BY " + WeatherDbContract.WeatherEntry.COLUMN_NAME_REQUEST_TIME + " DESC;";

        Log.i(this.getClass().getName(), "getLatest() raw query: " + rawQuery);
        Cursor cursor = db.rawQuery(rawQuery, null);
        cursor.moveToFirst();
        weather.setCity(cursor.getString(1));
        weather.setCityId(cursor.getString(2));
        weather.setRequestTime(cursor.getLong(3));
        forecast.setCurrentTemp(AppUtils.toFarenheight(cursor.getDouble(6)));
        forecast.setHigh(AppUtils.toFarenheight(cursor.getDouble(7)));
        forecast.setLow(AppUtils.toFarenheight(cursor.getDouble(8)));
        forecast.setConditions(cursor.getString(9));
        forecastList.add(forecast);
        weather.setForecasts(forecastList);
        for(int i = 0; i < cursor.getColumnNames().length; i++) {
            Log.i(this.getClass().getName(), "db column names: " + cursor.getColumnNames()[i]);
        }
        return weather;
    }

    public String getCityNameOfLastSavedWeather() {
        Weather weather = new Weather();
        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast = new Forecast();
        SQLiteDatabase db = weatherDbHelper.getReadableDatabase();
        String rawQuery = "SELECT * FROM " +
                WeatherDbContract.WeatherEntry.TABLE_NAME + " w " +
                "INNER JOIN " + WeatherDbContract.ForecastEntry.TABLE_NAME + " f " +
                "ON w." + WeatherDbContract.WeatherEntry.COLUMN_NAME_CITY_ID + " = f." + WeatherDbContract.ForecastEntry.COLUMN_NAME_CITY_ID +
                " ORDER BY " + WeatherDbContract.WeatherEntry.COLUMN_NAME_REQUEST_TIME + " DESC;";

        Log.i(this.getClass().getName(), "getLatest() raw query: " + rawQuery);
        Cursor cursor = db.rawQuery(rawQuery, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public boolean isEmpty() {
        SQLiteDatabase db = weatherDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WeatherDbContract.WeatherEntry.TABLE_NAME, null);
        return !cursor.moveToFirst();


    }

}