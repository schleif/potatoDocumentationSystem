package com.potatodoc.potatodocumentation.data;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by fiel on 05.10.2015.
 */
public class ConnectionHelper implements Runnable {

    String TAG = "ConnectionHelper Error";

    private static final String SERVER_ADDRESS = "192.168.178.67";

    // Simply <?php echo "Hallo App"; ?>
    private static final String WEBSERVICE = "/webapp.php";

    // In Android network task in threads
    @Override
    public void run() {
        try {
            URL url = new URL("http://" + SERVER_ADDRESS + WEBSERVICE);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readInputStream(in);
            } catch (IOException e) {
                Log.e(TAG, "Fehler während des Verbindungsaufbaus", e);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL nicht richtig formatierert", e);
        }
    }

    // read the web message or do what ever you like...
    private void readInputStream(InputStream in) {
        Scanner s = new Scanner(in);
        // Hallo
        System.out.println(s.next());
        // App
        System.out.println(s.next());
    }
}
