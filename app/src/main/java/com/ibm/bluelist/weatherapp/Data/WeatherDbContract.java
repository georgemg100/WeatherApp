package com.ibm.bluelist.weatherapp.Data;

import android.provider.BaseColumns;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

public class WeatherDbContract {

    private WeatherDbContract() { //prevent instantiation
    }

    public static class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_NAME_REQUEST_TIME = "request_time";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_CITY_ID = "city_id";
    }

    public static class ForecastEntry implements BaseColumns {
        public static final String TABLE_NAME = "forecast";
        public static final String COLUMN_NAME_LOW = "low";
        public static final String COLUMN_NAME_HIGH = "high";
        public static final String COLUMN_NAME_CURRENT_TEMP = "current";
        public static final String COLUMN_NAME_CONDITIONS = "conditions";
        public static final String COLUMN_NAME_CITY_ID = "city_id";

    }
}
