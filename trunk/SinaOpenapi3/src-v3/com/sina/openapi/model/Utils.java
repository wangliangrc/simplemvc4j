package com.sina.openapi.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Utils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss Z yyyy", new Locale("en", "CN"));

    public synchronized static Date parserWeiboDate(String text)
            throws ParseException {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("text is empty!");
        }
        return sdf.parse(text);
    }
}
