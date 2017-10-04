package com.ibm.bluelist.weatherapp.Data;

import android.util.Log;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Presenter.Presenter;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import org.json.JSONObject;

import java.io.IOException;

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
    Presenter presenter;

    /**
     * This callback is called when okhttp has finished the request.
     * This parses the json response, downloads icons, and saves the data for the city
     * to the db on a worker thread. This then sends the model object to the presenter which then
     * forwards the model object on the ui thread
     */
    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i(this.getClass().getName(), "request failure: " + e.getMessage());
            presenter.showError(e.getMessage());
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
                presenter.showWeather(weather);
            } catch (Exception e) {
                e.printStackTrace();
                presenter.showError(e.getMessage());
            }
        }
    };

    public WeatherRepository(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Ideally the city id should be sent rather than the city name.
     * A JSON file of cities could be stored locally and used to get the city id
     */
    public void fetchWeather(String city) {
        weatherRemoteDatasource.fetchWeather(city, callback);
    }

    public void loadLastSearched() {
        if(weatherLocalDatasource.isEmpty()) {
            presenter.showError("No saved weather. Enter a location to see weather!");
        } else {
            String cityName = weatherLocalDatasource.getCityNameOfLastSavedWeather();
            fetchWeather(cityName);
        }
    }
}
