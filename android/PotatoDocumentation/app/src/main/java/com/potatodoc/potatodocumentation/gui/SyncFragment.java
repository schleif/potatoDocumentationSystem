package com.potatodoc.potatodocumentation.gui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.data.Connection;
import com.potatodoc.potatodocumentation.data.localDB;

import java.util.LinkedList;

/**
 * Created by fiel on 30.09.2015.
 */
public class SyncFragment extends Fragment {

    private final static String TAG = "SyncFragment";

    Button b;
    TextView data;

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sync_layout, container, false);
        b = (Button) v.findViewById(R.id.syncbutton);
        data = (TextView) v.findViewById(R.id.data_textView);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick syncbutton");

                localDB dbhelper = new localDB(getContext());
                SQLiteDatabase db = dbhelper.getWritableDatabase();

                Connection con = new Connection() {
                    @Override
                    public void onPostExecute(String result) {
                        data.setText(result);
                    }
                };
                String url = con.formatDEFAULTURLnoValues("selectFeld.php");
                Log.d(TAG, url);
                String[] urls = {url};
                con.execute(urls);
            }
        });
        return v;
    }
}
