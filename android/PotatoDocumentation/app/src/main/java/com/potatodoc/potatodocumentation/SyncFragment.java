package com.potatodoc.potatodocumentation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.TextView;

import com.potatodoc.potatodocumentation.data.localDB;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
=======
>>>>>>> origin/android

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

                String[] names = {"Agata", "Agave", "Lolita", "Laura"};
                for (int i = 0; i < 4; i++) {
                    ContentValues cv = new ContentValues(2);
                    cv.put("name", names[i]);
                    cv.put("height", i*i);
                    db.insert("potatos", null, cv);
                }

                Log.d(TAG, "db created and filled");

                String datas = "";
                Cursor c = db.rawQuery("SELECT * FROM potatos", null);
                if(c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        datas = datas + c.getString(0) + c.getString(1) + c.getString(2);
                    } while (c.moveToNext());
                } else {
                    System.out.println("Kein Daten zum Ausgeben!");
                }

            }
        });
        return v;
    }
}
