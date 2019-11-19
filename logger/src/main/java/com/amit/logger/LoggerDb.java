package com.amit.logger;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LoggerDb implements Parcelable {

    private long id = -1;
    private long time_epoch = -1;
    private String time_formatted = "";
    private String message_log = "";


    private static final String TABLE_NAME = "table_log";

    private static final String COL_LOCAL_ID = "id";
    private static final String COL_MSGLOG = "message_log";
    private static final String COL_TIMEFORMATTED = "time_formatted";
    private static final String COL_TIMEEPOCH = "time_epoch";

    private static final String[] ALL_COLUMNS = {COL_LOCAL_ID, COL_MSGLOG, COL_TIMEFORMATTED, COL_TIMEEPOCH};

    public static final String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME + " ( " +
            COL_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MSGLOG + " TEXT, " + COL_TIMEFORMATTED + " TEXT, " +
            COL_TIMEEPOCH + " INTEGER );";


    public LoggerDb() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeEpoch() {
        return time_epoch;
    }

    public void setTimeEpoch(long time_epoch) {
        this.time_epoch = time_epoch;
    }

    public String getTimeFormatted() {
        return time_formatted;
    }

    public void setTimeFormatted(String time_formatted) {
        this.time_formatted = time_formatted;
    }

    public String getMessageLog() {
        return message_log;
    }

    public void setMessageLog(String message_log) {
        this.message_log = message_log;
    }


    public long insertIntoDatabase(SQLiteDatabase database) {
        long insertId = 0;
        try {
            insertId = database.insert(TABLE_NAME, null, getValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertId;
    }

    public long updateIntoDatabase(SQLiteDatabase database) {

        try {
            return database.update(TABLE_NAME, getValues(), COL_LOCAL_ID + " = ?", new String[]{String.valueOf(getId())});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteFromDatabase(SQLiteDatabase database) {

        try {
            database.delete(TABLE_NAME, COL_LOCAL_ID + " = ?", new String[]{String.valueOf(getId())});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<LoggerDb> readAllFromDatabase(SQLiteDatabase database) {

        ArrayList<LoggerDb> alMyLog = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, COL_LOCAL_ID);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LoggerDb myLog = new LoggerDb();
                alMyLog.add(myLog.cursorToObject(cursor));

                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return alMyLog;
    }

    private ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(COL_TIMEEPOCH, getTimeEpoch());
        values.put(COL_TIMEFORMATTED, getTimeFormatted());
        values.put(COL_MSGLOG, getMessageLog());

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.time_epoch);
        dest.writeString(this.time_formatted);
        dest.writeString(this.message_log);
    }

    protected LoggerDb(Parcel in) {
        this.id = in.readLong();
        this.time_epoch = in.readLong();
        this.time_formatted = in.readString();
        this.message_log = in.readString();
    }

    public static final Parcelable.Creator<LoggerDb> CREATOR = new Parcelable.Creator<LoggerDb>() {
        @Override
        public LoggerDb createFromParcel(Parcel source) {
            return new LoggerDb(source);
        }

        @Override
        public LoggerDb[] newArray(int size) {
            return new LoggerDb[size];
        }
    };

    private LoggerDb cursorToObject(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(COL_LOCAL_ID)));
        setTimeEpoch(cursor.getLong(cursor.getColumnIndex(COL_TIMEEPOCH)));
        setTimeFormatted(cursor.getString(cursor.getColumnIndex(COL_TIMEFORMATTED)));
        setMessageLog(cursor.getString(cursor.getColumnIndex(COL_MSGLOG)));
        return this;
    }
}
