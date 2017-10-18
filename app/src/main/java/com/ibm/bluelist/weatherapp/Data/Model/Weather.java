package com.ibm.bluelist.weatherapp.Data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelgeorgescu on 10/3/17.
 */

public class Weather {
    private List<Forecast> forecasts = new ArrayList<>();
    private String city;
    private String cityId;
    private Long requestTime;

    public String getCityId() {
        return cityId;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public String getCity() {
        return city;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        String forecastStr = "";
        for(Forecast forecast : forecasts) {
            forecastStr += "conditions: " + forecast.getConditions() + "\n"
                    + "current Temp: " + forecast.getCurrentTemp() + "\n"
                    + "high: " + forecast.getHigh() + "\n"
                    + "low: " + forecast.getLow();
        }
        return "city: " + city + "\n" +
                "forecasts:\n " + forecastStr;
    }
}
