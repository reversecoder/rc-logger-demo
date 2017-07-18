package com.reversecoder.logger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Logger {

    private Logger() {
        throw new RuntimeException("Private constructor cannot be accessed");
    }

    private static LogType logType = LogType.DEBUG;
    private static boolean isLoggable = true;
    private static String TAG = Logger.class.getSimpleName();
    private static Builder mBuilder = null;
    private static Context mContext;

    private static void checkSettings() {
        if (mBuilder == null) {
            throw new RuntimeException("You must configure \"Logger.Builder\" calling \"Logger.Builder.getInstance()\"");
        }
    }

    private static void init(Builder builder) {
        checkSettings();
        Logger.mContext = builder.mContext;
        Logger.logType = builder.getLogType();
        Logger.TAG = (builder.getTag().equalsIgnoreCase("")) ? Logger.mContext.getApplicationContext().getPackageName() : builder.getTag();
        Logger.isLoggable = builder.isIsLoggable();
    }

    public static void e(Object message) {
        checkSettings();
        if (isLoggable) {
            Log.e(TAG, "| " + makeLog(message, "e"));
        }
    }

    public static void e(Object message, Throwable throwable) {
        checkSettings();
        if (isLoggable) {
            Log.e(TAG, "| " + makeLog(message, "e"), throwable);
        }
    }

    public static void i(Object message) {
        checkSettings();
        if (isLoggable) {
            Log.i(TAG, "| " + makeLog(message, "i"));
        }
    }

    public static void w(Object message) {
        checkSettings();
        if (isLoggable) {
            Log.w(TAG, "| " + makeLog(message, "w"));
        }
    }

    public static void d(Object message) {
        checkSettings();
        if (isLoggable)
            Log.d(TAG, "| " + makeLog(message, "d"));
    }

    public static void log(Object message) {
        checkSettings();
        if (isLoggable) {
            String body = "| " + makeLog(message, "log");
            switch (logType) {
                case INFO:
                    Log.i(TAG, body);
                    break;
                case DEBUG:
                    Log.d(TAG, body);
                    break;
                case ERROR:
                    Log.e(TAG, body);
                    break;
                case WARN:
                    Log.w(TAG, body);
                    break;
            }
        }
    }

    private static String makeLog(Object message, String calledMethodName) {
        checkSettings();
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo(calledMethodName) == 0) {
                currentIndex = ++i;
                break;
            }
        }

        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[currentIndex];
        String fullClassName = traceElement.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();
        String logMessage = message == null ? null : message.toString();
        return logMessage + " | (" + className + ".java:" + lineNumber + ")";
    }

    public static class Builder {

        private static LogType logType = LogType.DEBUG;
        private static boolean isLoggable = true;
        private static String tag = "";
        private static Context mContext;

        private Builder() {
        }

        public static Builder getInstance(Context context) {
            if (mBuilder == null) {
                mBuilder = new Builder();
            }
            mContext = context;
            return mBuilder;
        }

        public Builder logType(LogType logType) {
            Builder.logType = logType;
            return this;
        }

        public Builder isLoggable(boolean isLoggable) {
            Builder.isLoggable = isLoggable;
            return this;
        }

        public Builder tag(String tag) {
            Builder.tag = tag;
            return this;
        }

        public void build() {
            init(this);
        }

        LogType getLogType() {
            return logType;
        }

        boolean isIsLoggable() {
            return isLoggable;
        }

        String getTag() {
            return tag;
        }
    }

    public static void saveLogAndEmailFile(Context context, String devEmail, String[] ccEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{devEmail});
        emailIntent.putExtra(Intent.EXTRA_CC, ccEmail);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[" + AppUtils.getApplicationName(context) + "_" + AppUtils.getApplicationVersion(context) + "] Debug log of " + AppUtils.convertMilliSecondToTime(System.currentTimeMillis(), "yyyy.MM.dd 'at' hh:mm:ss a"));

        StringBuilder sb = new StringBuilder();
        sb.append("\nManufacturer: ").append(Build.MANUFACTURER);
        sb.append("\nModel: ").append(Build.MODEL);
        sb.append("\nAndroid version: ").append(Build.VERSION.RELEASE);

        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        String fileName = "[" + AppUtils.getApplicationName(context) + "_" + AppUtils.getApplicationVersion(context) + "]_" + AppUtils.convertMilliSecondToTime(System.currentTimeMillis(), "yyyy.MM.dd'_'hh.mm.ss'_'a") + ".txt";
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + File.separator + AppUtils.getApplicationName(context));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists()) {
            File outputFile = new File(dir, fileName);
            Uri uri = Uri.fromFile(outputFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            Log.d(TAG, "Going to send logcat from " + outputFile);
            //emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(emailIntent, "Pick an Email provider").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            try {
                @SuppressWarnings("unused")
                Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
