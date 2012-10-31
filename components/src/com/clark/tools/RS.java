package com.clark.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 和 Android 资源相关的工具方法。
 * 
 * @author guangongbo
 *
 */
public class RS {
    protected Context mContext;
    protected DisplayMetrics mDisplayMetrics;
    protected Resources mResources;

    public RS(Context context) {
        super();
        mContext = context.getApplicationContext();
        mResources = mContext.getResources();
        mDisplayMetrics = mResources.getDisplayMetrics();
    }

    public int dipToPx(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, mDisplayMetrics) + .5f);
    }

    public int spToPx(int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                mDisplayMetrics) + .5f);
    }

    public int inToPx(int in) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, in,
                mDisplayMetrics) + .5f);
    }

    public int mmToPx(int mm) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm,
                mDisplayMetrics) + .5f);
    }

    public int ptToPx(int pt) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, pt,
                mDisplayMetrics) + .5f);
    }

    public String getString(int resId) {
        return mResources.getString(resId);
    }

    public String getString(int id, Object... formatArgs) {
        return mResources.getString(id, formatArgs);
    }

    public String[] getStringArray(int id) {
        return mResources.getStringArray(id);
    }

    public int[] getIntArray(int id) {
        return mResources.getIntArray(id);
    }

    public int getDimension(int id) {
        return (int) (mResources.getDimension(id) + .5f);
    }

    public Drawable getDrawable(int id) {
        return mResources.getDrawable(id);
    }

    public Movie getMovie(int id) {
        return mResources.getMovie(id);
    }

    public int getColor(int id) {
        return mResources.getColor(id);
    }

    public boolean getBoolean(int id) {
        return mResources.getBoolean(id);
    }

    public int getInteger(int id) {
        return mResources.getInteger(id);
    }

    private BitmapFactory.Options ensureOptions(BitmapFactory.Options opts) {
        if (opts == null) {
            opts = new Options();
        }
        // XXX 默认统一按照 dpi=160 处理
        if (opts.inDensity <= 0) {
            opts.inDensity = DisplayMetrics.DENSITY_MEDIUM;
        }
        return opts;
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param is
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(InputStream is, Options opts) {
        opts = ensureOptions(opts);
        return BitmapFactory.decodeResourceStream(mResources, null, is, null,
                opts);
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param data
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(byte[] data, Options opts) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            return decodeBitmap(inputStream, opts);
        } finally {
            GC.close(inputStream);
        }
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param data
     * @param offset
     * @param length
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(byte[] data, int offset, int length, Options opts) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data,
                offset, length);
        try {
            return decodeBitmap(inputStream, opts);
        } finally {
            GC.close(inputStream);
        }
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param in
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(File in, Options opts) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(in);
            return decodeBitmap(inputStream, opts);
        } catch (FileNotFoundException e) {
            // ignore
            return null;
        } finally {
            GC.close(inputStream);
        }
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param path
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(String path, Options opts) {
        return decodeBitmap(new File(path), opts);
    }

    /**
     * 根据当前的 {@link Resources}对象生成 {@link Bitmap}对象。
     * 
     * @param resId
     * @param opts
     * @return
     */
    public Bitmap decodeBitmap(int resId, Options opts) {
        // 不修改opts，系统根据文件夹位置自行处理
        return BitmapFactory.decodeResource(mResources, resId, opts);
    }
}
