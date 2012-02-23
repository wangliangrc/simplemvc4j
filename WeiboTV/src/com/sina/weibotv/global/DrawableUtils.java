package com.sina.weibotv.global;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
    private Context context;

    public DrawableUtils(Context context) {
        super();
        this.context = context;
    }

    /**
     * 获取一个反锯齿画笔的简单方法
     * 
     * @return
     */
    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(false);
        return paint;
    }

    /**
     * 获得一个指定字符串的 drawable 对象
     * 
     * @param paint
     *            指定字符串的大小，颜色等属性。
     * @param text
     *            需要绘制的字符串
     * @return 背景透明的带有字符串的 drawable 对象
     */
    public Drawable stringToDrawable(@Nonnull Paint paint, @Nonnull String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width() + 1; // 加一是为了防止某些时候出现的浮点数舍弃问题
        int height = (int) (-paint.ascent() + paint.descent() + .5);
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, -paint.ascent(), paint);
        return new BitmapDrawable(bitmap);
    }

    /**
     * 
     * @param layerProvider
     *            作为背景 drawable
     * @param newLayer
     *            作为绘制到背景上的 drawable
     * @param bounds
     *            新的一层 drawable 的位置
     * @return 背景 drawable
     */
    public LayerDrawable combineLayers(@Nonnull LayerDrawable layerProvider,
            @Nonnull Drawable newLayer, @Nonnull Rect bounds) {
        newLayer.setBounds(bounds);
        layerProvider.setDrawableByLayerId(newLayer.hashCode(), newLayer);
        return layerProvider;
    }

    public LayerDrawable combineLayers(@Nonnull LayerDrawable layerProvider,
            @Nonnull int newLayerId, @Nonnull Rect bounds) {
        return combineLayers(layerProvider,
                context.getResources().getDrawable(newLayerId), bounds);
    }

    /**
     * 
     * @param listProvider
     *            作为返回值
     * @param newState
     *            新的 state 下的 drawable
     * @param newStateSet
     *            新的 state
     * @return 返回 listProvider 参数
     */
    public StateListDrawable combineLists(
            @Nonnull StateListDrawable listProvider,
            @Nonnull Drawable newState, @Nonnull int[] newStateSet) {
        listProvider.addState(newStateSet, newState);
        return listProvider;
    }

    public static class Builder {
        private Context context;
        private List<Drawable> drawables = new LinkedList<Drawable>();
        // 每一个 drawable 的起始 x 轴位置
        private List<Integer> startxList = new LinkedList<Integer>();
        // 绘制所有 drawable 所需宽度
        private int width;
        // 所有 drawable 中的最大的高度
        private int height;
        private DrawableUtils utils;

        public Builder(Context context) {
            super();
            this.context = context;
            utils = new DrawableUtils(context);
        }

        /**
         * 
         * @param drawable
         *            新添加的 drawable
         * @param padding
         *            新添加的 drawable 与前一个 drawable 的 padding
         * @return
         */
        public Builder append(Drawable drawable, int padding) {
            if (drawable != null) {
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                if (width > 0 && height > 0) {
                    padding = padding > 0 ? padding : 0;
                    drawables.add(drawable);
                    startxList.add(this.width + padding);

                    this.height = this.height < height ? height : this.height;
                    this.width += width + padding;
                }
            }
            return this;
        }

        /**
         * 
         * @param resId
         *            添加的 drawable 的资源 id
         * @param padding
         *            新添加的 drawable 与前一个 drawable 的 padding
         * @return
         */
        public Builder append(int resId, int padding) {
            return append(context.getResources().getDrawable(resId), padding);
        }

        /**
         * 
         * @param text
         *            文字
         * @param paint
         * @param padding
         * @return
         */
        public Builder append(String text, Paint paint, int padding) {
            return append(utils.stringToDrawable(paint, text), padding);
        }

        /**
         * 
         * @param textId
         *            文字资源 ID
         * @param paint
         * @param padding
         * @return
         */
        public Builder append(int textId, Paint paint, int padding) {
            return append(
                    utils.stringToDrawable(paint, context.getString(textId)),
                    padding);
        }

        public int size() {
            return drawables.size();
        }

        public void clear() {
            drawables.clear();
            startxList.clear();
            width = 0;
            height = 0;
        }

        /**
         * 构造一个新的 drawable
         * 
         * @return
         */
        public Drawable build() {
            if (size() > 0) {
                Bitmap bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                Rect rect = new Rect();
                int left = 0;
                int top = 0;
                Drawable target = null;
                for (int i = 0, len = drawables.size(); i < len; i++) {
                    target = drawables.get(i);
                    left = startxList.get(i);
                    top = (height - target.getIntrinsicHeight()) / 2;
                    rect.set(left, top, left + target.getIntrinsicWidth(), top
                            + target.getIntrinsicHeight());
                    target.setBounds(rect);
                    target.draw(canvas);
                    rect.setEmpty();
                }

                return new BitmapDrawable(bitmap);
            }

            return null;
        }
    }
}
