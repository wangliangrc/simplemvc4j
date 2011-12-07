package com.clark.android.os;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public final class OsHelper {

    public static boolean match(IntentFilter filter, Intent intent) {
        Uri data = intent.getData();
        String scheme = data == null ? null : data.getScheme();
        return filter.match(intent.getAction(), intent.getType(), scheme, data,
                intent.getCategories(), "OsHelper") > 0;
    }
}