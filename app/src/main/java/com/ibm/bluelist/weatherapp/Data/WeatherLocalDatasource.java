package com.ibm.bluelist.weatherapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.City;
import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.View.MainApplication;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    ArrayList<City> allCities;


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

    private ArrayList<City> filterCitiesByText(String text) {
        if(text.contentEquals("")) return new ArrayList<>();
        ArrayList<City> results = new ArrayList<>();
        int low = 0;
        int high = allCities.size() - 1;
        int mid = 0;
        while(low <= high) {
            mid = (low + high) / 2;
            String name = allCities.get(mid).getName();
            if(name.compareToIgnoreCase(text) < 0) {
                low = mid + 1;
            } else if(name.compareToIgnoreCase(text) > 0) {
                high = mid - 1;
            } else {
                break;
            }
        }
        for(int i = mid; i < allCities.size(); i++) {
            if(allCities.get(i).getName().compareToIgnoreCase(text) != 0) i++;
            if(allCities.get(i).getName().toLowerCase().contains(text)) {
                results.add(allCities.get(i));
            } else {
                break;
            }
        }
        return results;
    }

    public ArrayList<City> fetchCitiesFromJSON(String text) throws IOException, JSONException {
        ArrayList<City> filteredCitiesArray = null;
        if(allCities != null) { //avoid doing IO each call
            filteredCitiesArray = filterCitiesByText(text);
        } else {
            String json = readFromAssets(MainApplication.context, "city.list.json");
            JSONArray jsonArrayAllCities = new JSONArray(json);
            ArrayList<City> cities = new ArrayList<>();
            for(int i = 0; i < jsonArrayAllCities.length(); i++) {
                JSONObject cityObj = jsonArrayAllCities.getJSONObject(i);
                cities.add(new City(cityObj.getString("id"), cityObj.getString("name"), cityObj.getString("country")));
            }
            Collections.sort(cities, new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            allCities = cities;
            filteredCitiesArray = filterCitiesByText(text);
        }
        return filteredCitiesArray;
    }

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }


}