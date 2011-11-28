package com.clark.weather.controller;

import static com.clark.mvc.Facade.sendNotification;

import java.io.InputStream;
import java.util.List;

import com.clark.mvc.Command;
import com.clark.mvc.Notification;
import com.clark.weather.model.Province;
import com.clark.weather.model.Utils;

public class Commands {

    @Command("解析城市")
    public static void parseProvince(final Notification notification) {
        new Thread() {
            public void run() {
                try {
                    InputStream inputStream = (InputStream) notification.body;
                    List<Province> provinces = Utils.getProvinces(inputStream);
                    System.out.println(provinces);
                    sendNotification("解析城市成功", provinces);
                } catch (Exception e) {
                    sendNotification("解析城市失败", e);
                }
            }
        }.start();
    }
}
