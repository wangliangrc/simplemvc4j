package com.clark.systeminfo;

import android.os.Build;
import android.os.Bundle;

public class ActivityBuild extends AbstractTextActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder builder = new StringBuilder();
        builder.append("DEVICE : ").append(Build.DEVICE);
        builder.append("\nMANUFACTURER : ").append(Build.MANUFACTURER);
        builder.append("\nPRODUCT : ").append(Build.PRODUCT);
        builder.append("\nMODEL : ").append(Build.MODEL);
        builder.append("\nHOST : ").append(Build.HOST);
        builder.append("\nBRAND : ").append(Build.BRAND);
        builder.append("\nBOOTLOADER : ").append(Build.BOOTLOADER);
        builder.append("\nBOARD : ").append(Build.BOARD);
        builder.append("\nCPU_ABI : ").append(Build.CPU_ABI);
        builder.append("\nCPU_ABI2 : ").append(Build.CPU_ABI2);
        builder.append("\nDISPLAY : ").append(Build.DISPLAY);
        builder.append("\nFINGERPRINT : ").append(Build.FINGERPRINT);
        builder.append("\nHARDWARE : ").append(Build.HARDWARE);
        builder.append("\nID : ").append(Build.ID);
        builder.append("\nRADIO : ").append(Build.RADIO);
        builder.append("\nRadioVersion : ").append(Build.getRadioVersion());
        builder.append("\nSERIAL : ").append(Build.SERIAL);
        builder.append("\nTAGS : ").append(Build.TAGS);
        builder.append("\nTIME : ").append(Build.TIME);
        builder.append("\nTYPE : ").append(Build.TYPE);
        builder.append("\nUSER : ").append(Build.USER);
        builder.append("\nUNKNOWN : ").append(Build.UNKNOWN);

        builder.append("\n\n");
        builder.append("SDK : ").append(Build.VERSION.SDK);
        builder.append("\nAPI Level : ").append(Build.VERSION.SDK_INT);
        builder.append("\nRELEASE : ").append(Build.VERSION.RELEASE);
        builder.append("\nINCREMENTAL : ").append(Build.VERSION.INCREMENTAL);
        builder.append("\nCODENAME : ").append(Build.VERSION.CODENAME);

        mTextView.setText(builder.toString());
    }

}
