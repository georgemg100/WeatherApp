package com.ibm.bluelist.weatherapp;

import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by michaelgeorgescu on 10/3/17.
 */

public class AppUtils {
    public static final String KEY_LIST = "list";
    public static final String KEY_CITY_OBJECT = "city";
    public static final String KEY_NAME_STRING = "name";
    public static final String KEY_WEATHER_ARRAY = "weather";
    public static final String KEY_CONDITIONS_STRING = "main";
    public static final String KEY_ICON_URL_STRING = "icon";
    public static final String KEY_MAIN_OBJECT = "main";
    public static final String KEY_HI_DOUBLE = "temp_max";
    public static final String KEY_LOW_DOUBLE = "temp_min";
    public static final String KEY_CURRENT_DOUBLE = "temp";
    public static final String KEY_CITY_ID_INT = "id";

    public static Weather fromJson(JSONObject json) throws JSONException {
        Weather weather = new Weather();
        JSONObject jsonCityObject = json.getJSONObject(KEY_CITY_OBJECT);
        weather.setCity(jsonCityObject.getString(KEY_NAME_STRING));
        weather.setCityId(jsonCityObject.getString(KEY_CITY_ID_INT));
        weather.setRequestTime(System.currentTimeMillis());
        List<Forecast> forecasts = new ArrayList<Forecast>();
        try {
            JSONArray jsonArray = json.getJSONArray(KEY_LIST);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObjectMain = jsonObject.getJSONObject(KEY_MAIN_OBJECT);
                JSONArray jsonArrayWeather = jsonObject.getJSONArray(KEY_WEATHER_ARRAY);
                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                Integer hi = toFarenheight(jsonObjectMain.getDouble(KEY_HI_DOUBLE));
                Integer low = toFarenheight(jsonObjectMain.getDouble(KEY_LOW_DOUBLE));
                Integer currentTemp = toFarenheight(jsonObjectMain.getDouble(KEY_CURRENT_DOUBLE));
                String conditions = jsonObjectWeather.getString(KEY_CONDITIONS_STRING);
                String iconUrl = jsonObjectWeather.getString(KEY_ICON_URL_STRING);

                Forecast forecast = new Forecast();
                forecast.setConditions(conditions);
                forecast.setIconUrl(iconUrl);
                forecast.setHigh(hi);
                forecast.setLow(low);
                forecast.setCurrentTemp(currentTemp);
                forecast.setRequestTime(System.currentTimeMillis());
                forecasts.add(forecast);
            }
        } catch (JSONException e) {
            throw e;
        }
        weather.setForecasts(forecasts);
        return weather;
    }

    public static boolean isValidCityName(String city) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
        return pattern.matcher(city.trim()).matches();
    }

    public static Integer toFarenheight(double kelvin) {
        return (int)(kelvin * 1.8 - 459.67);
    }
}