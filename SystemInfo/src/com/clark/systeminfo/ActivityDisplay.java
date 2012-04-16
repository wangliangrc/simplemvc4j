package com.clark.systeminfo;

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
                        1.f, displayMetrics)).append("px   (value * metrics.density)\n");
        lBuilder.append("1sp = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        1.f, displayMetrics)).append("px   (value * metrics.scaledDensity)\n");
        lBuilder.append("1in = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN,
                        1.f, displayMetrics)).append("px   (value * metrics.xdpi)\n");
        lBuilder.append("1mm = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
                        1.f, displayMetrics)).append("px   (value * metrics.xdpi * (1.0f/25.4f))\n");
        lBuilder.append("1pt = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT,
                        1.f, displayMetrics)).append("px   (value * metrics.xdpi * (1.0f/72))\n");
        lBuilder.append("1px = ")
                .append(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        1.f, displayMetrics)).append("px   (value)\n");
        mTextView.setText(lBuilder.toString());
    }

}
