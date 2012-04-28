package com.clark.systeminfo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
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
        String key = preference.getKey();
        String title = "请选择";
        if (key.equals("accessibility")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_ACCESSIBILITY_SETTINGS), title));
        } else if (key.equals("add_account")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_ADD_ACCOUNT), title));
        } else if (key.equals("airplane_mode")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_AIRPLANE_MODE_SETTINGS), title));
        } else if (key.equals("apn")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_APN_SETTINGS), title));
        } else if (key.equals("app_detail")) {
            Intent target = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            target.setData(Uri.parse("package:com.android.providers.settings"));
            startActivity(Intent.createChooser(target, title));
        } else if (key.equals("develop")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS), title));
        } else if (key.equals("app_setting")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_APPLICATION_SETTINGS), title));
        } else if (key.equals("bluetooth")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_BLUETOOTH_SETTINGS), title));
        } else if (key.equals("2g/3g")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_DATA_ROAMING_SETTINGS), title));
        } else if (key.equals("date")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_DATE_SETTINGS), title));
        } else if (key.equals("device_info")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_DEVICE_INFO_SETTINGS), title));
        } else if (key.equals("display")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_DISPLAY_SETTINGS), title));
        } else if (key.equals("inputmethod")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_INPUT_METHOD_SETTINGS), title));
        } else if (key.equals("input_method_subtype")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS), title));
        } else if (key.equals("internal_storage")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_INTERNAL_STORAGE_SETTINGS), title));
        } else if (key.equals("locale")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_LOCALE_SETTINGS), title));
        } else if (key.equals("location_source")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS), title));
        } else if (key.equals("manage_all_applications")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS), title));
        } else if (key.equals("memory_card")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_MEMORY_CARD_SETTINGS), title));
        } else if (key.equals("network_operator")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_NETWORK_OPERATOR_SETTINGS), title));
        } else if (key.equals("nfcsharing")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_NFCSHARING_SETTINGS), title));
        } else if (key.equals("privacy")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_PRIVACY_SETTINGS), title));
        } else if (key.equals("quick_launch")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_QUICK_LAUNCH_SETTINGS), title));
        } else if (key.equals("search")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_SEARCH_SETTINGS), title));
        } else if (key.equals("security")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_SECURITY_SETTINGS), title));
        } else if (key.equals("setting")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_SETTINGS), title));
        } else if (key.equals("sound")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_SOUND_SETTINGS), title));
        } else if (key.equals("sync")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_SYNC_SETTINGS), title));
        } else if (key.equals("user_dictionary")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_USER_DICTIONARY_SETTINGS), title));
        } else if (key.equals("wifi_ip")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_WIFI_IP_SETTINGS), title));
        } else if (key.equals("wifi")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_WIFI_SETTINGS), title));
        } else if (key.equals("wireless")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_WIRELESS_SETTINGS), title));
        } else if (key.equals("manage_applications")) {
            startActivity(Intent.createChooser(new Intent(
                    Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS), title));
        } else if (key.equals("switches")) {
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.frame, Fragment.instantiate(getActivity()
                    .getApplicationContext(), FragmentSwitch.class
                    .getCanonicalName()));
            transaction.addToBackStack(null);
            transaction.commit();
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setBackgroundColor(Color.BLACK);

        findPreference("accessibility").setOnPreferenceClickListener(this);
        findPreference("add_account").setOnPreferenceClickListener(this);
        findPreference("airplane_mode").setOnPreferenceClickListener(this);
        findPreference("apn").setOnPreferenceClickListener(this);
        findPreference("app_detail").setOnPreferenceClickListener(this);
        findPreference("develop").setOnPreferenceClickListener(this);
        findPreference("app_setting").setOnPreferenceClickListener(this);
        findPreference("bluetooth").setOnPreferenceClickListener(this);
        findPreference("2g/3g").setOnPreferenceClickListener(this);
        findPreference("date").setOnPreferenceClickListener(this);
        findPreference("device_info").setOnPreferenceClickListener(this);
        findPreference("display").setOnPreferenceClickListener(this);
        findPreference("inputmethod").setOnPreferenceClickListener(this);
        findPreference("input_method_subtype").setOnPreferenceClickListener(
                this);
        findPreference("internal_storage").setOnPreferenceClickListener(this);
        findPreference("locale").setOnPreferenceClickListener(this);
        findPreference("location_source").setOnPreferenceClickListener(this);
        findPreference("manage_all_applications").setOnPreferenceClickListener(
                this);
        findPreference("memory_card").setOnPreferenceClickListener(this);
        findPreference("network_operator").setOnPreferenceClickListener(this);
        findPreference("nfcsharing").setOnPreferenceClickListener(this);
        findPreference("privacy").setOnPreferenceClickListener(this);
        findPreference("quick_launch").setOnPreferenceClickListener(this);
        findPreference("search").setOnPreferenceClickListener(this);
        findPreference("security").setOnPreferenceClickListener(this);
        findPreference("setting").setOnPreferenceClickListener(this);
        findPreference("sound").setOnPreferenceClickListener(this);
        findPreference("sync").setOnPreferenceClickListener(this);
        findPreference("user_dictionary").setOnPreferenceClickListener(this);
        findPreference("wifi_ip").setOnPreferenceClickListener(this);
        findPreference("wifi").setOnPreferenceClickListener(this);
        findPreference("wireless").setOnPreferenceClickListener(this);
        findPreference("manage_applications")
                .setOnPreferenceClickListener(this);
        findPreference("switches").setOnPreferenceClickListener(this);
    }

}
