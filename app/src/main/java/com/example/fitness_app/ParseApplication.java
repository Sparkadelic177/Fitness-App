package com.example.fitness_app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ParseObject.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("SparkysFitness") // should correspond to APP_ID env variable
                .clientKey("StayFit")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://sparkysfitness.herokuapp.com/parse").build());
    }
}