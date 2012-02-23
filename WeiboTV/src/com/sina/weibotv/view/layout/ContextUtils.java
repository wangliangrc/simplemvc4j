package com.sina.weibotv.view.layout;

import static com.clark.func.Functions.notNull;
import android.content.Context;

public abstract class ContextUtils {

    protected Context context;

    public ContextUtils(Context context) {
        super();
        notNull(context);
        this.context = context;
    }

}
