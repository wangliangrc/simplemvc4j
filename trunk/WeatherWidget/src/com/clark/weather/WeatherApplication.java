package com.clark.weather;

import static com.clark.mvc.Facade.controller;
import static com.clark.mvc.Facade.setUIWorker;
import android.app.Application;
import android.os.Handler;

import com.clark.mvc.UIWorker;
import com.clark.weather.controller.Commands;

public class WeatherApplication extends Application implements UIWorker {
    private Handler handler;

    @Override
    public void onCreate() {
        handler = new Handler();
        // 设置UI执行线程
        setUIWorker(this);
        // 注册CMD
        controller().register(Commands.class);
    }

    @Override
    public void onTerminate() {
        controller().remove(Commands.class);
        super.onTerminate();
    }

    @Override
    public void postTask(Runnable task) {
        handler.post(task);
    }
}
