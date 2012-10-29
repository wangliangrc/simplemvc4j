package com.clark.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import coms.Shell;

public class MainShell extends Shell implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Button btn = new Button(getApplicationContext());
        btn.setText("第一页");
        btn.setOnClickListener(this);
        setContentView(btn);
    }

    public void onClick(View v) {
        startComponent(new Intent(getApplication(), SecondShell.class));
    }

}
