package edu.upc.pes;

import android.app.Application;

public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Singleton.getInstance();
    }
}
