package com.reversecoder.logger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {

    public static final String SIMPLE_DATE_FORMAT = "EEE, d MMM yyyy hh:mm:ss a";

    /*
     *  @see <a href="https://developer.android.com/reference/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * */
    public static String convertMilliSecondToTime(long milliSecond, String dateFormat) {
        Date date = new Date(milliSecond);
        SimpleDateFormat dateformat;
        if (dateFormat == null || dateFormat.equalsIgnoreCase("")) {
            dateformat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        } else {
            dateformat = new SimpleDateFormat(dateFormat);
        }
        return dateformat.format(date);
    }

    /**
     * Get application version.
     *
     * @param context only the application context.
     * @return String the value in string is the application's version name.
     */
    public static String getApplicationVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return (pInfo != null ? pInfo.versionName : "(unknown)");
    }

    /**
     * Get application name.
     *
     * @param context only the application context.
     * @return String the value in string is the application's name.
     */
    public static String getApplicationName(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    /**
     * Get application icon.
     *
     * @param context only the application context.
     * @return String the value in drawable is the application's icon.
     */
    public static Drawable getApplicationIcon(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        Drawable appIcon = null;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
            appIcon = ai.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appIcon;
    }
}
