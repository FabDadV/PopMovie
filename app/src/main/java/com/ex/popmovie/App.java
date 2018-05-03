package com.ex.popmovie;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class App extends Application {
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Stetho.initializeWithDefaults(this);
    }

}
