package com.sina.weibotv.view.layout;

import static com.clark.func.Functions.notNull;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.sina.weibotv.R;
import com.sina.weibotv.global.DrawableUtils;

public class IndexPageUtils extends ContextUtils {

    public IndexPageUtils(Context context) {
        super(context);
    }

    public Drawable mainTabBackground() {
        DrawableUtils drawableUtils = new DrawableUtils(context);
        DrawableUtils.Builder builder = new DrawableUtils.Builder(context);
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();

        Paint unslcPaint = drawableUtils.getPaint();
        unslcPaint.setColor(0xFFDCD7C7);
        unslcPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 34, displayMetrics));
        Paint slcPaint = drawableUtils.getPaint();
        slcPaint.setColor(0xFFFEF4E3);
        slcPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 48, displayMetrics));
        Paint hvrPaint = drawableUtils.getPaint();
        hvrPaint.setColor(0xFFDFEAF8);
        hvrPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics));

        builder.append(R.drawable.ic_home_un, 0);
        builder.append(R.string.mainpage, unslcPaint,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        24, displayMetrics));
        Drawable unslcDrawable = largeBmp(builder.build(), displayMetrics);

        builder.clear();
        builder.append(R.drawable.ic_home_hover, 0);
        builder.append(R.string.mainpage, hvrPaint,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        15, displayMetrics));
        Drawable hvrDrawable = new LayerDrawable(new Drawable[] {
                builder.build(),
                context.getResources().getDrawable(
                        R.drawable.bk_indexleft_hover) });
        hvrDrawable = largeBmp(hvrDrawable, displayMetrics);

        builder.clear();
        builder.append(R.drawable.ic_home_se, 0);
        builder.append(R.string.mainpage, hvrPaint,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        15, displayMetrics));
        Drawable slcDrawable = new LayerDrawable(new Drawable[] {
                builder.build(),
                context.getResources().getDrawable(
                        R.drawable.bk_indexleft_hover) });
        slcDrawable = largeBmp(slcDrawable, displayMetrics);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_enabled },
                unslcDrawable);
        stateListDrawable.addState(new int[] { android.R.attr.state_focused },
                hvrDrawable);
        stateListDrawable.addState(new int[] { android.R.attr.state_selected },
                slcDrawable);
        return stateListDrawable;
    }

    private Drawable largeBmp(Drawable d, DisplayMetrics dm) {
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 124, dm);
        int width = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 320, dm);
        Bitmap tempBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBmp);
        int intrinsicWidth = d.getIntrinsicWidth();
        int intrinsicHeight = d.getIntrinsicHeight();
        int left = (width - intrinsicWidth) / 2;
        int top = (height - intrinsicHeight) / 2;
        d.setBounds(left, top, left + intrinsicWidth, top + intrinsicHeight);
        d.draw(canvas);
        d = new BitmapDrawable(tempBmp);
        return d;
    }

    /**
     * 底部信息栏提示的功能按键信息 <br />
     * new String[]{"收藏", "转发", "评论", "刷新"} 或者 new String[]{"收藏", "转发", null,
     * "刷新"}
     * 
     * @return
     */
    public Drawable functionKeysPrompt(CharSequence... prompt) {
        notNull(prompt);
        if (prompt.length == 0) {
            return null;
        }

        DrawableUtils drawableUtils = new DrawableUtils(context);
        DrawableUtils.Builder builder = new DrawableUtils.Builder(context);

        Paint paint = drawableUtils.getPaint();
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        // 30sp
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                30.f, displayMetrics));
        paint.setColor(0xFFCDCDCD);

        int[] drawableIds = { R.drawable.ic_indexbottom_a,
                R.drawable.ic_indexbottom_b, R.drawable.ic_indexbottom_c,
                R.drawable.ic_indexbottom_d };
        for (int i = 0, len = prompt.length > 4 ? 4 : prompt.length; i < len; i++) {
            if (prompt[i] == null) {
                continue;
            }

            if (builder.size() > 0) {
                builder.append(drawableIds[i],
                // 55dp
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 55.f,
                                displayMetrics));
            } else {
                builder.append(drawableIds[i], 0);
            }
            builder.append(prompt[i].toString(), paint,
            // 17dp
                    (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 17.f, displayMetrics));
        }

        return builder.build();
    }
}
