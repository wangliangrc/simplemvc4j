package com.clark.weather;

import static com.clark.func.Functions.closeQuietly;
import static com.clark.mvc.Facade.sendNotification;
import static com.clark.mvc.Facade.view;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.clark.io.ByteArrayOutputStream;
import com.clark.mvc.Mediator;
import com.clark.mvc.Notification;
import com.clark.weather.model.City;
import com.clark.weather.model.Province;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view().register(this);

        textView = (TextView) findViewById(R.id.test);
        try {
            sendNotification("解析城市", getAssets().open("cities.dat"));
        } catch (IOException e) {
        }
    }

    @Override
    protected void onDestroy() {
        view().remove(this);
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    @Mediator({ "解析城市成功", "解析城市失败" })
    public void fetchParseCityResult(Notification notification) {
        if (notification.name.equals("解析城市成功")) {
            List<Province> provinces = (List<Province>) notification.body;
            StringBuilder builder = new StringBuilder();
            for (Province province : provinces) {
                builder.append(province.getName()).append("\n");
                for (City city : province) {
                    builder.append(
                            String.format("%s(%s)", city.getName(),
                                    city.getCode())).append("\n");
                }
            }
            textView.setText(builder.toString());
        } else if (notification.name.equals("解析城市失败")) {
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            try {
                Throwable throwable = (Throwable) notification.body;
                throwable.printStackTrace(new PrintStream(error));
                textView.setText(error.toString());
            } finally {
                closeQuietly(error);
            }
        }
    }
}