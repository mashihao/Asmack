package com.demo;

import android.app.Application;

/**
 * Created by sv-004 on 2016/7/1.
 */
public class MyApplication extends Application {


    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
