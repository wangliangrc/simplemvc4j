package com.clark.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

public class ActivityMain extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Handler handler = new Handler();
        new Thread() {
            public void run() {
                try {
                    testDb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "测试完毕",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }

    private static void testDb() throws Exception {
        Class.forName("SQLite.JDBCDriver");
        Properties info = new Properties();
        Connection c = DriverManager.getConnection("jdbc:sqlite:/"
                + Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test.db", info);
        Statement statement = c.createStatement();
        statement
                .execute("create table conputers (id integer primary key, name text)");
        statement.close();
        c.close();
    }
}