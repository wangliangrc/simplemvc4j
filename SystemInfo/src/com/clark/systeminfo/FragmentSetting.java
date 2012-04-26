package com.clark.systeminfo;

import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;

public class FragmentSetting extends PreferenceFragment implements
        OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_setting);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setBackgroundColor(Color.BLACK);

        resolver = getActivity().getContentResolver();

        wifiSwitch = (SwitchPreference) findPreference("wifi_switch");
        wifiDirectSwitch = (SwitchPreference) findPreference("wifi_direct_switch");
        bluetoothSwitch = (SwitchPreference) findPreference("bluetooth_switch");
        flightModeSwitch = (SwitchPreference) findPreference("flight_mode");
        windowRotationSwitch = (SwitchPreference) findPreference("rotation");
        googleLocationSwitch = (SwitchPreference) findPreference("google_location");
        gpsLocationSwitch = (SwitchPreference) findPreference("gps_location");
        wifiLocationSwitch = (SwitchPreference) findPreference("wifi_location");
        unknowenApkSwitch = (SwitchPreference) findPreference("unknowen_source");
        usdDebugSwitch = (SwitchPreference) findPreference("usb_debug");

        wifiSwitch.setOnPreferenceClickListener(this);
        wifiDirectSwitch.setOnPreferenceClickListener(this);
        bluetoothSwitch.setOnPreferenceClickListener(this);
        flightModeSwitch.setOnPreferenceClickListener(this);
        windowRotationSwitch.setOnPreferenceClickListener(this);
        googleLocationSwitch.setOnPreferenceClickListener(this);
        gpsLocationSwitch.setOnPreferenceClickListener(this);
        wifiLocationSwitch.setOnPreferenceClickListener(this);
        unknowenApkSwitch.setOnPreferenceClickListener(this);
        usdDebugSwitch.setOnPreferenceClickListener(this);

        updateStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiSwitch = null;
        wifiDirectSwitch = null;
        bluetoothSwitch = null;
        flightModeSwitch = null;
        windowRotationSwitch = null;
        googleLocationSwitch = null;
        gpsLocationSwitch = null;
        wifiLocationSwitch = null;
        unknowenApkSwitch = null;
        usdDebugSwitch = null;
        resolver = null;
    }

    private void updateStatus() {
        int enable = Settings.Secure.getInt(resolver,
                Settings.Secure.ADB_ENABLED, 0);
        usdDebugSwitch.setChecked(enable != 0);

        enable = Settings.Secure.getInt(resolver, Settings.Secure.BLUETOOTH_ON,
                0);
        bluetoothSwitch.setChecked(enable != 0);

        enable = Settings.Secure.getInt(resolver,
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        unknowenApkSwitch.setChecked(enable != 0);

        enable = Settings.Secure.getInt(resolver, Settings.Secure.WIFI_ON, 0);
        wifiSwitch.setChecked(enable != 0);

        enable = Settings.System.getInt(resolver,
                Settings.System.ACCELEROMETER_ROTATION, 0);
        windowRotationSwitch.setChecked(enable != 0);

        enable = Settings.System.getInt(resolver,
                Settings.System.AIRPLANE_MODE_ON, 0);
        flightModeSwitch.setChecked(enable != 0);
    }

    private SwitchPreference wifiSwitch;
    private SwitchPreference wifiDirectSwitch;
    private SwitchPreference bluetoothSwitch;
    private SwitchPreference flightModeSwitch;
    private SwitchPreference windowRotationSwitch;
    private SwitchPreference googleLocationSwitch;
    private SwitchPreference gpsLocationSwitch;
    private SwitchPreference wifiLocationSwitch;
    private SwitchPreference unknowenApkSwitch;

    private SwitchPreference usdDebugSwitch;

    private ContentResolver resolver;
}
