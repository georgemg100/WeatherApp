package com.ibm.bluelist.weatherapp.View;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by michaelgeorgescu on 10/3/17.
 */
/**
    Allows the Model Layer to access the context without having to pass it through the presenter
 */
public class MainApplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getName(), "onCreate main Application");
        context = getApplicationContext();
    }
}
