package com.potatodoc.potatodocumentation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.potatodoc.potatodocumentation.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Marcel W on 09.01.2016.
 * The Class do the keyoperations of the DB.
 */
public class localDBDataSource {

    public SQLiteDatabase database;
    private localDB dbHelper;
    private static final String LOG_TAG = localDBDataSource.class.getSimpleName();

    // Build the local database
    public localDBDataSource(Context context) {

        dbHelper = new localDB(context);
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
    }

    // Open an writeable database connection
    public void open() {

        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Die Db Verbindung wurde geöffnet.");
    }

    //close the database connection
    public void close() {

        dbHelper.close();

    }

    //Begin of the mehtods for the database statments

    public void insertTask(String aufg_name, String fromDate, String toDate) {

        long rowId = -1;

        try {

            ContentValues aufgabe = new ContentValues();
            aufgabe.put("aufg_name", aufg_name);

            ContentValues values = new ContentValues();
            values.put("aufg_name", aufg_name);
            values.put("fromDate", fromDate);
            values.put("toDate", toDate);

            rowId = database.insert("aufgabe", null, aufgabe);
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, "insert()", e);
        } finally {
            Log.d(LOG_TAG,"insert(): rowID =" + rowId);
        }
        //database.insert("aufg_termin", null, values);
        //database.execSQL("INSERT INTO aufg_termin(aufg_name, fromDate, toDate) VALUES (" + aufg_name + "," + fromDate + "," + toDate + ");");


    }
    //query for the outstandingstasks
    @Nullable
    public Cursor queryForOutstandandingTasks() {


        /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //get the right Dateformat
        String date = dateFormat.format(new Date());                //get the current Date

        //Create a String-Array for the rawQuery
        String[] values = new String[1];
        values[0] = date;*/

        Cursor result = database.query("aufgabe",null,null,null,null,null,null);           //rawQuery("SELECT * FROM aufgabe", null);

        //Cursor result = database.rawQuery("SELECT COUNT(aufg_name) FROM aufg_termin WHERE toDate = ? GROUP BY aufg_name", values);
         return result;
    }

    public String showTables() {
        Cursor tables = database.rawQuery("SHOW TABLES;", null);
        String s = "";
        while (!tables.isAfterLast()) {

            s += tables.getString(tables.getColumnCount())+"\n";
            tables.moveToNext();

        }
        return s;

    }


}
