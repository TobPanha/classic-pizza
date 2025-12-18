package com.scentify.app;

import android.app.Application;

import com.scentify.app.data.repository.ScentRepository;

public class ScentifyApplication extends Application {
    private ScentRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new ScentRepository(this);
    }

    public ScentRepository getRepository() {
        return repository;
    }
}
