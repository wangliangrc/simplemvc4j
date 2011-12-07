package com.clark.android.os;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.clark.android.os.AndroidWorker.Status;

public final class OsHelper {

    public static boolean match(IntentFilter filter, Intent intent) {
        Uri data = intent.getData();
        String scheme = data == null ? null : data.getScheme();
        return filter.match(intent.getAction(), intent.getType(), scheme, data,
                intent.getCategories(), "OsHelper") > 0;
    }

    public static String printIntent(Intent intent) {
        return intent.toURI();
    }

    public static String printBundle(Bundle bundle) {
        return bundle.toString();
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void recycle(Message message) {
        if (message != null) {
            message.recycle();
        }
    }

    public static boolean isTaskFinished(AndroidWorker<?, ?, ?> worker) {
        return worker == null || worker.getStatus() == Status.FINISHED;
    }

    public static void recycle(AndroidWorker<?, ?, ?> worker) {
        if (!isTaskFinished(worker)) {
            worker.cancel(true);
        }
    }

    private static String USER_AGENT = "Mozilla/5.0 (Linux; U; Android %s)AppleWebKit/530.17 "
            + "(KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";

    public static String getUserAgent() {
        final StringBuffer buffer = new StringBuffer();
        final Locale locale = Locale.getDefault();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        return String.format(USER_AGENT, buffer);
    }

    public static String getIMEI(Context ctx) {
        final TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = mTelephonyMgr.getDeviceId();

        return imei;
    }

    public static String getIMSI(Context ctx) {
        final TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String imsi = mTelephonyMgr.getSubscriberId();

        return imsi;
    }

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 判断飞行模式是否开启
     * 
     * @param context
     * @return
     */
    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * Regular expression pattern to match all IANA top-level domains. List
     * accurate as of 2007/06/15. List taken from:
     * http://data.iana.org/TLD/tlds-alpha-by-domain.txt This pattern is
     * auto-generated by //device/tools/make-iana-tld-pattern.py
     */
    public static final Pattern TOP_LEVEL_DOMAIN_PATTERN = Pattern
            .compile("((aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                    + "|(biz|b[abdefghijmnorstvwyz])"
                    + "|(cat|com|coop|c[acdfghiklmnoruvxyz])" + "|d[ejkmoz]"
                    + "|(edu|e[cegrstu])" + "|f[ijkmor]"
                    + "|(gov|g[abdefghilmnpqrstuwy])" + "|h[kmnrtu]"
                    + "|(info|int|i[delmnoqrst])" + "|(jobs|j[emop])"
                    + "|k[eghimnrwyz]" + "|l[abcikrstuvy]"
                    + "|(mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                    + "|(name|net|n[acefgilopruz])" + "|(org|om)"
                    + "|(pro|p[aefghklmnrstwy])" + "|qa" + "|r[eouw]"
                    + "|s[abcdeghijklmnortuvyz]"
                    + "|(tel|travel|t[cdfghjklmnoprtvwz])" + "|u[agkmsyz]"
                    + "|v[aceginu]" + "|w[fs]" + "|y[etu]" + "|z[amw])");

    /**
     * Regular expression pattern to match RFC 1738 URLs List accurate as of
     * 2007/06/15. List taken from:
     * http://data.iana.org/TLD/tlds-alpha-by-domain.txt This pattern is
     * auto-generated by //device/tools/make-iana-tld-pattern.py
     */
    public static final Pattern WEB_URL_PATTERN = Pattern
            .compile("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+" // named
                    // host
                    + "(?:" // plus top level domain
                    + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                    + "|(?:biz|b[abdefghijmnorstvwyz])"
                    + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
                    + "|d[ejkmoz]"
                    + "|(?:edu|e[cegrstu])"
                    + "|f[ijkmor]"
                    + "|(?:gov|g[abdefghilmnpqrstuwy])"
                    + "|h[kmnrtu]"
                    + "|(?:info|int|i[delmnoqrst])"
                    + "|(?:jobs|j[emop])"
                    + "|k[eghimnrwyz]"
                    + "|l[abcikrstuvy]"
                    + "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                    + "|(?:name|net|n[acefgilopruz])"
                    + "|(?:org|om)"
                    + "|(?:pro|p[aefghklmnrstwy])"
                    + "|qa"
                    + "|r[eouw]"
                    + "|s[abcdeghijklmnortuvyz]"
                    + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
                    + "|u[agkmsyz]"
                    + "|v[aceginu]"
                    + "|w[fs]"
                    + "|y[etu]"
                    + "|z[amw]))"
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9])))"
                    + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~" // plus
                    // option
                    // query
                    // params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$)"); // and finally, a word boundary or end of
    // input. This is to stop foo.sure from
    // matching as foo.su

    public static final Pattern IP_ADDRESS_PATTERN = Pattern
            .compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");

    public static final Pattern DOMAIN_NAME_PATTERN = Pattern
            .compile("(((([a-zA-Z0-9][a-zA-Z0-9\\-]*)*[a-zA-Z0-9]\\.)+"
                    + TOP_LEVEL_DOMAIN_PATTERN + ")|" + IP_ADDRESS_PATTERN
                    + ")");

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    /**
     * This pattern is intended for searching for things that look like they
     * might be phone numbers in arbitrary text, not for validating whether
     * something is in fact a phone number. It will miss many things that are
     * legitimate phone numbers.
     * 
     * <p>
     * The pattern matches the following:
     * <ul>
     * <li>Optionally, a + sign followed immediately by one or more digits.
     * Spaces, dots, or dashes may follow.
     * <li>Optionally, sets of digits in parentheses, separated by spaces, dots,
     * or dashes.
     * <li>A string starting and ending with a digit, containing digits, spaces,
     * dots, and/or dashes.
     * </ul>
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile( // sdd = space,
            // dot, or
            // dash
            "(\\+[0-9]+[\\- \\.]*)?" // +<digits><sdd>*
                    + "(\\([0-9]+\\)[\\- \\.]*)?" // (<digits>)<sdd>*
                    + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"); // <digit><digit|sdd>+<digit>
}
