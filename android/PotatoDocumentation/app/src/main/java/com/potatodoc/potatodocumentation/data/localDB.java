package com.potatodoc.potatodocumentation.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.utils.App;

import java.io.FileReader;
import java.io.IOException;
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

    private static final String LOG_TAG = localDB.class.getSimpleName();

    public localDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
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

            String next = sqlScanner.next();
            Log.d(LOG_TAG, next);

            db.execSQL(next);

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG,"Upgrade der Datenbank von Version " + oldVersion + " zu " + newVersion + "; Alle Daten werden gelöscht.");
        dropAll(db);
        onCreate(db);
    }

    public void syncDatabase() {
        syncTable("feld");

    }

    private void syncTable(String tableName) {

    }

    public void dropAll(SQLiteDatabase database) {

        Log.d(LOG_TAG, "Start der Drop stmts");
        database.execSQL("DROP TABLE IF EXISTS `aufg_termin`");
        database.execSQL("DROP TABLE IF EXISTS `aufg_gehoert_zu_parz`");
        database.execSQL("DROP TABLE IF EXISTS `aufg_beinhaltet_eig`");
        database.execSQL("DROP TABLE IF EXISTS `parzellen`");
        database.execSQL("DROP TABLE IF EXISTS `eigenschaft`");
        database.execSQL("DROP TABLE IF EXISTS `sorte`");
        database.execSQL("DROP TABLE IF EXISTS `aufgabe`");
        database.execSQL("DROP TABLE IF EXISTS `feld`");
        Log.d(LOG_TAG, "letzte Tabelle gedroppt");
    }
}
