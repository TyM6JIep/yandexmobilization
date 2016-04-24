package com.arsanima.yandexmobilization;

import android.app.Application;

import com.arsanima.yandexmobilization.helpers.NetworkHelper;
import com.arsanima.yandexmobilization.helpers.StorageHelper;

/**
 * Created by YuriF on 16.04.16.
 */
public class YMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSingletons();
    }

    public void initSingletons() {
        // инициализируем наши хелперы
        StorageHelper.getInstance().initialize(getApplicationContext());
        NetworkHelper.getInstance().initialize(getApplicationContext());
    }
}
