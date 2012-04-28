package com.clark.systeminfo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class FragmentSwitch extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_switch);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
