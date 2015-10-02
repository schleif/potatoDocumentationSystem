package com.potatodoc.potatodocumentation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;

/**
 * Created by fiel on 02.10.2015.
 */
public class localDB extends SQLiteOpenHelper {

    // SHOULD INCREMENT IF SCHEME CHANGED
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "local.db";

    // CREATE STATEMENTS
    private static final String createTableSTMT_potatos =
            "CREATE TABLE IF NOT EXISTS potatos" +
            "(" +
                    "timestamp DEFAULT CURRENT_TIMESTAMP," +
                    "name VARCHAR(255)," +
                    "height INTEGER" +
            ");";

    public localDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // EXEC ALL CREATE STATEMENTS
        db.execSQL(createTableSTMT_potatos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
