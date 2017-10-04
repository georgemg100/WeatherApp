package com.ibm.bluelist.weatherapp.Data.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

public class Forecast {
    private Long requestTime;
    private Integer low;
    private Integer high;
    private Integer currentTemp;
    private String conditions;
    private String iconUrl;
    private Bitmap iconBitmap;

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    public String getIconUrl() {
        return iconUrl;
    }
    public Integer getLow() {
        return low;
    }

    public Integer getHigh() {
        return high;
    }

    public Integer getCurrentTemp() {
        return currentTemp;
    }

    public String getConditions() {
        return conditions;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public void setCurrentTemp(Integer currentTemp) {
        this.currentTemp = currentTemp;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
