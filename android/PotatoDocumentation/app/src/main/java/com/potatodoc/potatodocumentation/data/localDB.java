package com.potatodoc.potatodocumentation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.utils.App;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by fiel on 02.10.2015.
 */
public class localDB extends SQLiteOpenHelper {

    // SHOULD INCREMENT IF SCHEME CHANGED
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "local";

    public LinkedList<String> tables;


    public localDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // EXEC ALL CREATE STATEMENTS
        InputStream sqlInputStream =
                App.getContext().
                getResources().
                openRawResource(R.raw.sql_build_stmt);
        Scanner sqlScanner = new Scanner(sqlInputStream).useDelimiter(";");
        while (sqlScanner.hasNext()) {
            db.execSQL(sqlScanner.next());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void syncDatabase() {
        syncTable("feld");

    }

    private void syncTable(String tableName) {
        Connection con = new Connection();
        String url = con.formatDEFAULTURL("selectFeld.php");
        con.doInBackground();
    }

}
