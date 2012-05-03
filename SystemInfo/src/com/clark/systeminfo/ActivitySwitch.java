package com.clark.systeminfo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActivitySwitch extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_switch);
    }

}
