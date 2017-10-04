package com.ibm.bluelist.weatherapp.Presenter;

import android.os.Handler;
import android.os.Looper;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.Data.WeatherRepository;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

/**
 * This is the Presenter which is responsible for forwarding 'commands' between the View and the Model layers
 */
public class Presenter implements PresenterViewContract.Presenter {
    WeatherRepository repository;
    PresenterViewContract.View view;

    public Presenter(PresenterViewContract.View view) {
        this.view = view;
        repository = new WeatherRepository(this);
    }

    @Override
    public void loadWeather(String city) {
        if(AppUtils.isValidCityName(city)) {
            repository.fetchWeather(city);
        } else {
            view.showError("Please enter a city name!");
        }
    }

    @Override
    public void loadLastSearched() {
        repository.loadLastSearched();
    }

    @Override
    public void showWeather(final Weather weather) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                view.showWeather(weather);
            }
        });
    }

    @Override
    public void showError(final String message) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                view.showError(message);// code to interact with UI
            }
        });
    }

    public void runOnUIThread(Runnable task) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(task);
    }
}
