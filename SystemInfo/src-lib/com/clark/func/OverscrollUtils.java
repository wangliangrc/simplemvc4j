package com.clark.func;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

public class OverscrollUtils {

    public static void initAbsListView(AbsListView absListView, View up,
            View down, int overScrollDistance) {
        if (absListView == null) {
            throw new NullPointerException("absListView is null!");
        }

        if (Build.VERSION.SDK_INT < 9) {
            // 避免Android2.3之前版本误用
            return;
        }

        final Context context = absListView.getContext()
                .getApplicationContext();
        final Resources resources = context.getResources();

        absListView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        absListView.setScrollIndicators(new View(context), new View(context));

        // if (absListView instanceof ListView) {
        // ListView listView = (ListView) absListView;
        // int paddingUp = 0;
        // int paddingDown = 0;
        // if (up != null) {
        // paddingUp = -up.getMeasuredHeight();
        // listView.addHeaderView(up);
        // }
        //
        // if (down != null) {
        // paddingDown = -down.getMeasuredHeight();
        // listView.addFooterView(down);
        // }
        // absListView.setPadding(0, -48, 0, paddingDown);
        // }

        try {
            if (mOverscrollDistanceAbsListViewField == null) {
                mOverscrollDistanceAbsListViewField = AbsListView.class
                        .getDeclaredField("mOverscrollDistance");
                mOverscrollDistanceAbsListViewField.setAccessible(true);
            }

            if (mOverflingDistanceAbsListViewField == null) {
                mOverflingDistanceAbsListViewField = AbsListView.class
                        .getDeclaredField("mOverflingDistance");
                mOverflingDistanceAbsListViewField.setAccessible(true);
            }

            if (overScrollDistance <= 0) {
                overScrollDistance = resources.getDisplayMetrics().heightPixels;
            }
            mOverscrollDistanceAbsListViewField.set(absListView,
                    overScrollDistance);
            // mOverflingDistanceField.set(absListView, overScrollDistance);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initAbsListView(ListView absListView, View up, View down) {
        initAbsListView(absListView, up, down, 0);
    }

    public static void initAbsListView(AbsListView absListView,
            int overScrollDistance) {
        initAbsListView(absListView, null, null, overScrollDistance);
    }

    public static void initAbsListView(AbsListView absListView) {
        initAbsListView(absListView, null, null, 0);
    }

    public static void initScrollView(ScrollView scrollView) {
        initScrollView(scrollView, 0);
    }

    public static void initScrollView(ScrollView scrollView,
            int overScrollDistance) {
        if (scrollView == null) {
            throw new NullPointerException("absListView is null!");
        }

        if (Build.VERSION.SDK_INT < 9) {
            // 避免Android2.3之前版本误用
            return;
        }

        final Context context = scrollView.getContext().getApplicationContext();
        final Resources resources = context.getResources();
        scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        try {
            if (mOverscrollDistanceScrollViewField == null) {
                mOverscrollDistanceScrollViewField = ScrollView.class
                        .getDeclaredField("mOverscrollDistance");
                mOverscrollDistanceScrollViewField.setAccessible(true);
            }

            if (mOverflingDistanceScrollViewField == null) {
                mOverflingDistanceScrollViewField = ScrollView.class
                        .getDeclaredField("mOverflingDistance");
                mOverflingDistanceScrollViewField.setAccessible(true);
            }

            if (overScrollDistance <= 0) {
                overScrollDistance = resources.getDisplayMetrics().heightPixels;
            }
            mOverscrollDistanceScrollViewField.set(scrollView,
                    overScrollDistance);
            // mOverflingDistanceScrollViewField.set(absListView,
            // overScrollDistance);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field mOverscrollDistanceAbsListViewField;
    private static Field mOverflingDistanceAbsListViewField;
    private static Field mOverscrollDistanceScrollViewField;
    private static Field mOverflingDistanceScrollViewField;
}
