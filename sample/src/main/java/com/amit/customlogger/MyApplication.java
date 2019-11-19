package com.amit.customlogger;

import android.app.Application;
import android.util.Log;

import com.amit.logger.Logger;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("TESt", "onCreate: ");

        Logger.with(this, new Logger.Builder()
                .setLoggable(true)
                .insertToDb(false)
                .withSelectedClassOnly(false)
                .build());

        Logger.e(TAG, "run: "+Logger.getInstance().hashCode() );

    }

}
