package com.clark.weather.model;

import static com.clark.func.Functions.UTF_8;
import static com.clark.func.Functions.closeQuietly;
import static com.clark.func.Functions.isNotBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Province> getProvinces(InputStream inputStream)
            throws IOException {
        try {
            ArrayList<Province> provinces = new ArrayList<Province>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, UTF_8));
            String line = null;
            Province province = null;
            String[] parts = null;
            while ((line = reader.readLine()) != null) {
                if (isNotBlank(line)) {
                    line = line.trim();
                    if (!line.contains("=")) {
                        province = new Province(line.substring(1,
                                line.length() - 1));
                        provinces.add(province);
                    } else {
                        parts = line.split("=");
                        province.add(new City(parts[0], parts[1], province));
                    }
                }
            }
            return provinces;
        } finally {
            closeQuietly(inputStream);
        }
    }

    private static final String url1 = "http://www.weather.com.cn/data/sk/%s.html";
    private static final String url2 = "http://www.weather.com.cn/data/cityinfo/%s.html";
    private static final String url3 = "http://m.weather.com.cn/data/%s.html";

    public static String skUrl(String cityCode) {
        return String.format(url1, cityCode);
    }

    public static String cityinfoUrl(String cityCode) {
        return String.format(url2, cityCode);
    }

    public static String mUrl(String cityCode) {
        return String.format(url3, cityCode);
    }
}
