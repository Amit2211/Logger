package com.amit.customlogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amit.logger.Logger;
import com.amit.logger.LoggerDb;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.e(TAG, "run: " + Logger.getInstance().hashCode());

        Logger.e(TAG, "onCreate:loggable " + Logger.isLoggable());
        Logger.e(TAG, "onCreate:onlySelectedClass " + Logger.getParams().isOnlySelectedClass());
        Logger.e(TAG, "onCreate:database " + Logger.getParams().getDatabase());
        Logger.e(TAG, "onCreate:insertToDb " + Logger.getParams().isInsertToDb());
        Logger.e(TAG, "onCreate:getDbHelper " + Logger.getParams().getDbHelper());
        Logger.e(TAG, "onCreate:LoggerDb.readAllFromDatabase " + LoggerDb.readAllFromDatabase(Logger.getDatabase()).size());


    }
}
