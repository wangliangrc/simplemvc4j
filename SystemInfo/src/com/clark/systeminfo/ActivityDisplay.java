package com.clark.systeminfo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ActivityDisplay extends AbstractTextActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        StringBuilder lBuilder = new StringBuilder();
        lBuilder.append("width : ").append(displayMetrics.widthPixels)
                .append("\n");
        lBuilder.append("height : ").append(displayMetrics.heightPixels)
                .append("\n");
        lBuilder.append("density : ").append(displayMetrics.density)
                .append("\n");
        lBuilder.append("densityDpi : ").append(displayMetrics.densityDpi)
                .append("\n");
        lBuilder.append("scaledDensity : ")
                .append(displayMetrics.scaledDensity).append("\n");
        lBuilder.append("xdpi : ").append(displayMetrics.xdpi).append("\n");
        lBuilder.append("ydpi : ").append(displayMetrics.ydpi).append("\n");
        lBuilder.append("\n");
        lBuilder.append("1dp = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        1.f, displayMetrics))
                .append("px   (value * metrics.density)\n");
        lBuilder.append("1sp = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        1.f, displayMetrics))
                .append("px   (value * metrics.scaledDensity)\n");
        lBuilder.append("1in = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN,
                        1.f, displayMetrics))
                .append("px   (value * metrics.xdpi)\n");
        lBuilder.append("1mm = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
                        1.f, displayMetrics))
                .append("px   (value * metrics.xdpi * (1.0f/25.4f))\n");
        lBuilder.append("1pt = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT,
                        1.f, displayMetrics))
                .append("px   (value * metrics.xdpi * (1.0f/72))\n");
        lBuilder.append("1px = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        1.f, displayMetrics)).append("px   (value)\n");

        lBuilder.append("\n-----------------------------------------\n");
        Configuration configuration = getResources().getConfiguration();

        lBuilder.append("mcc: ").append(configuration.mcc).append("\n");

        lBuilder.append("mnc: ").append(configuration.mnc).append("\n");

        lBuilder.append("locale: ").append(configuration.locale).append("\n");

        if (Utils.supportOsVersion(13)) {
            // XXX API Level 13
            lBuilder.append("smallestScreenWidthDp: ")
                    .append(configuration.smallestScreenWidthDp).append("\n");
            lBuilder.append("screenWidthDp: ")
                    .append(configuration.screenWidthDp).append("\n");
            lBuilder.append("screenHeightDp: ")
                    .append(configuration.screenHeightDp).append("\n");
        }

        // 屏幕尺寸
        lBuilder.append("screenLayout: ").append(configuration.screenLayout)
                .append("\n");
        lBuilder.append(
                "\tconfiguration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK: ")
                .append(configuration.screenLayout
                        & Configuration.SCREENLAYOUT_SIZE_MASK).append("\n");
        lBuilder.append("\t\tSCREENLAYOUT_SIZE_XLARGE: ")
                .append(Configuration.SCREENLAYOUT_SIZE_XLARGE)
                .append("\n\t\tSCREENLAYOUT_SIZE_LARGE: ")
                .append(Configuration.SCREENLAYOUT_SIZE_LARGE)
                .append("\n\t\tSCREENLAYOUT_SIZE_NORMAL: ")
                .append(Configuration.SCREENLAYOUT_SIZE_NORMAL)
                .append("\n\t\tSCREENLAYOUT_SIZE_SMALL: ")
                .append(Configuration.SCREENLAYOUT_SIZE_SMALL)
                .append("\n\t\tSCREENLAYOUT_SIZE_UNDEFINED: ")
                .append(Configuration.SCREENLAYOUT_SIZE_UNDEFINED).append("\n");
        lBuilder.append(
                "\tconfiguration.screenLayout & Configuration.SCREENLAYOUT_LONG_MASK: ")
                .append(configuration.screenLayout
                        & Configuration.SCREENLAYOUT_LONG_MASK).append("\n");
        lBuilder.append("\t\tSCREENLAYOUT_LONG_YES: ")
                .append(Configuration.SCREENLAYOUT_LONG_YES)
                .append("\n\t\tSCREENLAYOUT_LONG_NO: ")
                .append(Configuration.SCREENLAYOUT_LONG_NO)
                .append("\n\t\tSCREENLAYOUT_LONG_UNDEFINED: ")
                .append(Configuration.SCREENLAYOUT_LONG_UNDEFINED).append("\n");

        // 屏幕方向
        lBuilder.append("orientation: ").append(configuration.orientation)
                .append("\n");
        lBuilder.append("\tORIENTATION_LANDSCAPE: ")
                .append(Configuration.ORIENTATION_LANDSCAPE)
                .append("\n\tORIENTATION_PORTRAIT: ")
                .append(Configuration.ORIENTATION_PORTRAIT)
                .append("\n\tORIENTATION_SQUARE: ")
                .append(Configuration.ORIENTATION_SQUARE)
                .append("\n\tORIENTATION_UNDEFINED: ")
                .append(Configuration.ORIENTATION_UNDEFINED).append("\n");

        // Car || Desk
        lBuilder.append("uiMode: ").append(configuration.uiMode).append("\n");
        lBuilder.append(
                "\tconfiguration.uiMode & Configuration.UI_MODE_NIGHT_MASK: ")
                .append(configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK)
                .append("\n");
        lBuilder.append("\t\tUI_MODE_NIGHT_YES: ")
                .append(Configuration.UI_MODE_NIGHT_YES)
                .append("\n\t\tUI_MODE_NIGHT_NO: ")
                .append(Configuration.UI_MODE_NIGHT_NO)
                .append("\n\t\tUI_MODE_NIGHT_UNDEFINED: ")
                .append(Configuration.UI_MODE_NIGHT_UNDEFINED).append("\n");

        // ldpi、hdpi、mdpi、nodpi、tvdpi
        lBuilder.append(
                "\tconfiguration.uiMode & Configuration.UI_MODE_TYPE_MASK: ")
                .append(configuration.uiMode & Configuration.UI_MODE_TYPE_MASK)
                .append("\n");
        lBuilder.append("\t\tUI_MODE_TYPE_CAR: ")
                .append(Configuration.UI_MODE_TYPE_CAR)
                .append("\n\t\tUI_MODE_TYPE_DESK: ")
                .append(Configuration.UI_MODE_TYPE_DESK)
                .append("\n\t\tUI_MODE_TYPE_NORMAL: ")
                .append(Configuration.UI_MODE_TYPE_NORMAL)
                .append("\n\t\tUI_MODE_TYPE_TELEVISION: ")
                .append(Configuration.UI_MODE_TYPE_TELEVISION)
                .append("\n\t\tUI_MODE_TYPE_UNDEFINED: ")
                .append(Configuration.UI_MODE_TYPE_UNDEFINED).append("\n");

        // notouch、stylus、finger
        lBuilder.append("touchscreen: ").append(configuration.touchscreen)
                .append("\n");
        lBuilder.append("\tTOUCHSCREEN_FINGER: ")
                .append(Configuration.TOUCHSCREEN_FINGER)
                .append("\n\tTOUCHSCREEN_STYLUS: ")
                .append(Configuration.TOUCHSCREEN_STYLUS)
                .append("\n\tTOUCHSCREEN_NOTOUCH: ")
                .append(Configuration.TOUCHSCREEN_NOTOUCH)
                .append("\n\tTOUCHSCREEN_UNDEFINED: ")
                .append(Configuration.TOUCHSCREEN_UNDEFINED).append("\n");

        lBuilder.append("keyboardHidden: ")
                .append(configuration.keyboardHidden).append("\n");
        lBuilder.append("\tKEYBOARDHIDDEN_YES: ")
                .append(Configuration.KEYBOARDHIDDEN_YES)
                .append("\n\tKEYBOARDHIDDEN_NO: ")
                .append(Configuration.KEYBOARDHIDDEN_NO)
                .append("\n\tKEYBOARDHIDDEN_UNDEFINED: ")
                .append(Configuration.KEYBOARDHIDDEN_UNDEFINED).append("\n");

        lBuilder.append("keyboard: ").append(configuration.keyboard)
                .append("\n");
        lBuilder.append("\tKEYBOARD_12KEY: ")
                .append(Configuration.KEYBOARD_12KEY)
                .append("\n\tKEYBOARD_QWERTY: ")
                .append(Configuration.KEYBOARD_QWERTY)
                .append("\n\tKEYBOARD_NOKEYS: ")
                .append(Configuration.KEYBOARD_NOKEYS)
                .append("\n\tKEYBOARD_UNDEFINED: ")
                .append(Configuration.KEYBOARD_UNDEFINED).append("\n");

        lBuilder.append("navigationHidden: ")
                .append(configuration.navigationHidden).append("\n");
        lBuilder.append("\tNAVIGATIONHIDDEN_YES: ")
                .append(Configuration.NAVIGATIONHIDDEN_YES)
                .append("\n\tNAVIGATIONHIDDEN_NO: ")
                .append(Configuration.NAVIGATIONHIDDEN_NO)
                .append("\n\tNAVIGATIONHIDDEN_UNDEFINED: ")
                .append(Configuration.NAVIGATIONHIDDEN_UNDEFINED).append("\n");

        lBuilder.append("navigation: ").append(configuration.navigation)
                .append("\n");
        lBuilder.append("\tNAVIGATION_DPAD: ")
                .append(Configuration.NAVIGATION_DPAD)
                .append("\n\tNAVIGATION_TRACKBALL: ")
                .append(Configuration.NAVIGATION_TRACKBALL)
                .append("\n\tNAVIGATION_WHEEL: ")
                .append(Configuration.NAVIGATION_WHEEL)
                .append("\n\tNAVIGATION_NONAV: ")
                .append(Configuration.NAVIGATION_NONAV)
                .append("\n\tNAVIGATION_UNDEFINED: ")
                .append(Configuration.NAVIGATION_UNDEFINED).append("\n");

        lBuilder.append("fontScale: ").append(configuration.fontScale)
                .append("\n");

        lBuilder.append("hardKeyboardHidden: ")
                .append(configuration.hardKeyboardHidden).append("\n");
        lBuilder.append("\tHARDKEYBOARDHIDDEN_YES: ")
                .append(Configuration.HARDKEYBOARDHIDDEN_YES)
                .append("\n\tHARDKEYBOARDHIDDEN_NO: ")
                .append(Configuration.HARDKEYBOARDHIDDEN_NO)
                .append("\n\tHARDKEYBOARDHIDDEN_UNDEFINED: ")
                .append(Configuration.HARDKEYBOARDHIDDEN_UNDEFINED)
                .append("\n");

        mTextView.setText(lBuilder.toString());
    }

}
