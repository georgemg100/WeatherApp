package com.ibm.bluelist.weatherapp.Data;

import android.util.Log;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.City;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

/**
 * Entry point to the Model Layer. Communicates with the Presenter and datasources
 */
public class WeatherRepository {
    WeatherRemoteDatasource weatherRemoteDatasource = new WeatherRemoteDatasource();
    WeatherLocalDatasource weatherLocalDatasource = new WeatherLocalDatasource();
    private static WeatherRepository INSTANCE = null;
    Weather weather;
    /**
     * This callback parses the json response, downloads icons, and saves the data locally.
     * This then sends the model object to the presenter
     */
    private WeatherRepository() {

    }


    public static WeatherRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepository();
        }
        return INSTANCE;
    }

    public Weather getWeather() {
        return weather;
    }
    /**
     * Ideally the city id should be sent rather than the city name.
     * A JSON file of cities could be stored locally and used to get the city id
     */
    public void fetchWeather(String city, final WeatherDataSource.LoadWeatherCallback callback) {
        weatherRemoteDatasource.fetchWeather(city, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(this.getClass().getName(), "request failure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponseString = response.body().string();
                Log.i(this.getClass().getName(), "response: " + jsonResponseString);
                try {
                    Weather weather = AppUtils.fromJson(new JSONObject(jsonResponseString));
                    Log.i(this.getClass().getName(), "parsed weather object: " + weather);
                    weatherRemoteDatasource.downloadConditionIconsForForecasts(weather);
                    weatherLocalDatasource.insert(weather);
                    INSTANCE.weather = weather;
                    callback.onWeatherLoaded(weather);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable(e.getMessage());
                }
            }
        });
    }

    public void loadLastSearched(WeatherDataSource.LoadWeatherCallback callback) {
        if(weatherLocalDatasource.isEmpty()) {
        } else {
            String cityName = weatherLocalDatasource.getCityNameOfLastSavedWeather();
            fetchWeather(cityName, callback);
        }
    }

    public ArrayList<City> fetchCities(String text) throws IOException, JSONException {
        ArrayList<City> filteredCities = null;
        try {
            filteredCities = weatherLocalDatasource.fetchCitiesFromJSON(text);
        } catch(IOException e) {
            throw e;
        } catch(JSONException e) {
            throw e;
        }
        return filteredCities;
    }
}
