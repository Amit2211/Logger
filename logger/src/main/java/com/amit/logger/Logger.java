/*
 * Created by Amit Solanki on 9/8/2019
 */

package com.amit.logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Logger is a Simple And Powerful library for logging in your application in your customizable way.
 *
 * It also supports and manages the feature of saving logs into the database so that you can send those logs to the server
 * for improvisation in your app and see how your app is performing.
 *
 * Also, you can add a list of classes in Logger for generating logs from a specific class only.
 *
 * Class is final so no one can extend it.
 */


public final class Logger {

    private static volatile Logger logger = null;
    private static Params params;


    private Logger() {
        //cannot initialize singleton.
    }

    /**
     * This method ensures that no other object of Logger class is created.
     * and it uses "ThreadSafe Singleton Mechanism" to create perfect singleton.
     *
     * @return new singleton instance of Logger class.
     */

    public static Logger with(Context context, Logger var0) {

        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    params.dbHelper = new LoggerDbHelper(context);
                    params.database = params.dbHelper.getWritableDatabase();
                    setLogger(var0);
                }
            }
        }
        return logger;
    }


    public static Logger getInstance() {

        return logger;
    }


    private void initParams(Params params) {
        Logger.params = params;
    }


    private static void setLogger(Logger logger) {
        Logger.logger = logger;
    }


    public static void canLogToDbWith(Class<?> cls) {

        if (params.getSelectedClassList() != null) {

            params.getSelectedClassList().put(cls.getSimpleName(), cls);
        }
    }


    public static void removeFromListOfClass(Class<?> cls) {

        if (cls != null && params.getSelectedClassList() != null) {

            params.getSelectedClassList().remove(cls.getSimpleName());
        }
    }


    public static void removeAll() {

        if (params.getSelectedClassList() != null) {

            params.getSelectedClassList().clear();
        }
    }


    public static Params getParams() {
        return params;
    }


    public static SQLiteDatabase getDatabase() {
        return params.getDatabase();
    }


    public static boolean isLoggable() {
        return params.isLoggable();
    }


    public static void d(String tag, String text, Throwable throwable) {
        if (isLoggable()) {
            Log.d(tag, text, throwable);
        }
        log(tag, text);
    }

    public static void v(String tag, String text, Throwable throwable) {
        if (isLoggable()) {
            Log.v(tag, text, throwable);
        }
        log(tag, text);
    }

    public static void i(String tag, String text, Throwable throwable) {
        if (isLoggable()) {
            Log.i(tag, text, throwable);
        }
        log(tag, text);
    }

    public static void w(String tag, String text, Throwable throwable) {
        if (isLoggable()) {
            Log.w(tag, text, throwable);
        }
        log(tag, text);
    }

    public static void e(String tag, String text, Throwable throwable) {
        if (isLoggable()) {
            Log.e(tag, text, throwable);
        }
        log(tag, text);
    }

    public static void d(String tag, String text) {
        d(tag, text, null);
    }

    public static void v(String tag, String text) {
        v(tag, text, null);
    }

    public static void i(String tag, String text) {
        i(tag, text, null);
    }

    public static void w(String tag, String text) {
        w(tag, text, null);
    }

    public static void e(String tag, String text) {
        e(tag, text, null);
    }


    public static void forceLog(int priority, String tag, String msg) {

        Log.println(priority, tag, msg);
    }


    private static void log(String tag, String text) {

        if (params.isInsertToDb() && params.isOnlySelectedClass()) {

            if (params.getSelectedClassList().containsKey(tag)) {

                logToDb(tag + Params.SEPARATOR + text);
            }

        } else if (params.isInsertToDb()) {

            logToDb(tag + Params.SEPARATOR + text);
        }
    }


    private static void logToDb(String message) {

        long currentTime = System.currentTimeMillis();

        LoggerDb myLog = new LoggerDb();

        myLog.setMessageLog(message);
        myLog.setTimeEpoch(currentTime);
        myLog.setTimeFormatted(new SimpleDateFormat(Params.PATTERN_TIME, Locale.getDefault()).format(currentTime));

        long l = myLog.insertIntoDatabase(params.database);
    }


    /**
     * @deprecated because stackTraceElement is not finding correct class from getTag method.
     * Need to work on this.
     *
     */

    @Deprecated
    public static String getTag(StackTraceElement element) {
        String tag = element.getClassName()/*.substringAfterLast('.')*/;
        Matcher m = Params.ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        // Tag length limit was removed in API 24.
        if (tag.length() <= Params.MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        } else {
            return tag.substring(0, Params.MAX_TAG_LENGTH);
        }
    }

    @Deprecated
    public static String getTag(Object o) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        int position = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getFileName().contains(o.getClass().getSimpleName())
                    && !elements[i + 1].getFileName().contains(o.getClass().getSimpleName())) {
                position = i;
                break;
            }
        }

        StackTraceElement element = elements[position];
        String className = element.getFileName().replace(".java", "");
        return "[" + className + "](" + element.getMethodName() + ":" + element.getLineNumber() + ")";
    }


    /**
     * Builder class for creating Logger singleton instance.
     *
     */

    public static class Builder implements ILogger {

        Params p = new Params();

        public Builder() {
        }

        @Override
        public Builder setLoggable(boolean var0) {
            p.loggable = var0;
            return this;
        }

        @Override
        public Builder insertToDb(boolean var0) {
            p.insertToDb = var0;
            return this;
        }

        @Override
        public Builder withSelectedClassOnly(boolean var0) {
            p.onlySelectedClass = var0;
            return this;
        }

        @Override
        public Logger build() {

            Logger logger = new Logger();
            logger.initParams(p);

            return logger;
        }
    }

    /**
     * Blueprint for Builder Class.
     *
     */

    private interface ILogger {

        Builder setLoggable(boolean var0);

        Builder insertToDb(boolean var0);

        Builder withSelectedClassOnly(boolean var0);

        Logger build();
    }

    /**
     * Parameters of Logger Class.
     *
     */

    public static class Params {

        public static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

        public static final String PATTERN_TIME = "dd-MM-yyyy HH:mm:ss a";

        public static final int MAX_LOG_LENGTH = 4000;
        public static final int MAX_TAG_LENGTH = 23;
        public static final String SEPARATOR = " : ";

        boolean loggable = false;
        boolean insertToDb = false;
        boolean onlySelectedClass = false;

        LoggerDbHelper dbHelper = null;
        SQLiteDatabase database = null;

        HashMap<String, Class<?>> selectedClassList = new HashMap<>();


        public boolean isLoggable() {
            return loggable;
        }

        public boolean isInsertToDb() {
            return insertToDb;
        }

        public boolean isOnlySelectedClass() {
            return onlySelectedClass;
        }

        public LoggerDbHelper getDbHelper() {
            return dbHelper;
        }

        public SQLiteDatabase getDatabase() {
            return database;
        }

        public HashMap<String, Class<?>> getSelectedClassList() {
            return selectedClassList;
        }
    }
}