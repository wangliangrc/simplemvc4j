package com.clark.app;

import android.os.Bundle;

import coms.AbstractComponent;

public class MainCom extends AbstractComponent {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainCom.onCreate()");
    }

    @Override
    public void onDestroy() {
        System.out.println("MainCom.onDestroy()");
        super.onDestroy();
    }
}
