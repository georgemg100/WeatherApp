package com.ibm.bluelist.weatherapp.Search;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.City;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.Data.WeatherDataSource;
import com.ibm.bluelist.weatherapp.Data.WeatherRepository;
import com.ibm.bluelist.weatherapp.Search.SearchCityContract;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michaelgeorgescu on 10/15/17.
 */

public class SearchCityPresenter implements SearchCityContract.Presenter {

    SearchCityContract.View view;
    WeatherRepository repository;
    ArrayList<City> cities = new ArrayList<>();

    public SearchCityPresenter(SearchCityContract.View view) {
        this.view = view;
        repository = WeatherRepository.getInstance();
    }

    @Override
    public void loadCitiesFromAssets() {
        view.showSpinner();
        view.hideActivity();
        Runnable runnable = new Runnable() {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    view.hideSpinner();
                    view.showActivity();
                }
            };

            @Override
            public void run() {
                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                //fetch cities on background thread
                try {
                    repository.fetchCities(""); //load into main memory
                } catch (IOException e) {
                    view.showError(e.getMessage());
                } catch (JSONException e) {
                    view.showError(e.getMessage());
                }
                Message completeMessage = handler.obtainMessage();
                completeMessage.sendToTarget();
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void loadCities(String text) {
        try {
             cities = repository.fetchCities(text);
        } catch (IOException e) {
            view.showError(e.getMessage());
        } catch (JSONException e) {
            view.showError(e.getMessage());
        }
        view.showCities(cities);
    }

    @Override
    public City getCityAtPosition(int index) {
        return cities.get(index);
    }

    @Override
    public void loadWeather(String city) {
        if(AppUtils.isValidCityName(city)) {
            view.showSpinner();
            repository.fetchWeather(city, new WeatherDataSource.LoadWeatherCallback() {

                @Override
                public void onWeatherLoaded(final Weather weather) {
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            view.dismissSpinner();
                            view.dismissActivity(weather);
                        }
                    });

                }

                @Override
                public void onDataNotAvailable(final String message) {
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            view.showError(message);
                        }
                    });
                }
            } );
        } else {
            view.showError("Please enter a city name!");
        }
    }

    public void runOnUIThread(Runnable task) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(task);
    }

}
