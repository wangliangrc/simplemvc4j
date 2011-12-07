package com.clark.android.test;

import java.io.Serializable;

import android.widget.TextView;

import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.BaseActivity;
import com.clark.android.annotation.SaveInstance;
import com.clark.android.annotation.ViewProperty;

public class Activity01 extends BaseActivity {
    @Override
    protected ListenerAdapter getListenerAdapter() {
        return null;
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText(student == null ? "null" : student.toString());

        if (student == null) {
            student = new Student();
        }

        student.setId((int) (Math.random() * 1000));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @ViewProperty(R.id.text)
    public TextView textView;

    @Override
    protected void onInitialize() {
    }

    @SaveInstance
    public Student student;
}

class Student implements Serializable {

    private static final long serialVersionUID = 5642640315222201454L;

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + "]";
    }

}