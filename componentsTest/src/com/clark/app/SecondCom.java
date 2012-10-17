package com.clark.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import coms.AbstractComponent;

public class SecondCom extends AbstractComponent implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Button btn = new Button(getApplicationContext());
        btn.setText("第二页");
        btn.setOnClickListener(this);
        setContentView(btn);
    }

    public void onClick(View v) {
        finish();
    }
}
