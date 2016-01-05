package com.potatodoc.potatodocumentation.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.potatodoc.potatodocumentation.gui.NavigationItem;
import com.potatodoc.potatodocumentation.gui.SyncFragment;
import com.potatodoc.potatodocumentation.utils.Misc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fiel on 20.11.2015.
 * This class helps you to download stuff from a given url.
 * If you want to use it you have to implement the onPostExecute Method
 * In this method you can write your code that should executet if the download is complete
 *
 * You have also access to some static URL format methods.
 */
public abstract class Connection extends AsyncTask<String, Void, String> {

    static final String TAG = "CON";

    private static final String CONNECTION_TYPE = "http";
    private static final String HOST = "134.169.47.160";

    public Connection() {
        super();
    }

    @Override
    public String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            Log.e(TAG, "Exception", e);
            return "Cannot connect.";
        }
    }

    @Override
    public abstract void onPostExecute(String result);

    private String downloadUrl(String myUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = Misc.readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * This method format the url for you. Express the:
     * @param host could be an IP or Domain
     * @param service the specific Webpage on host. You may use this to represent folder structures
     * @param values the keys are the get param and value the get value
     * @return well formed url
     */
    public static String formatURL(String conType, String host, String service, Map<String, String> values) {
        if(values == null || values.isEmpty()) return conType.concat("://").concat(host).concat("/").concat(service);
        String url = conType.concat("://").concat(host).concat("/").concat(service + "?");
        boolean first = true;
        for(Map.Entry<String, String> entry : values.entrySet()) {
            if (first) {
                first = false;
                url.concat("?" + entry.getKey() + "=" + entry.getValue());
            } else {
                url.concat("&" + entry.getKey() + "=" + entry.getValue());
            }
        }
        Log.d(TAG, url);
        return url;
    }

    /**
     *
     */
    public static String formatHTTPURL(String host, String service, Map<String, String> values) {
        return formatURL(CONNECTION_TYPE, host, service, values);
    }

    /**
     *
     */
    public static String formatDEFAULTURL(String service, Map<String, String> values) {
        return formatURL(CONNECTION_TYPE, HOST, service, values);
    }

    public static String formatDEFAULTURLnoValues(String service) {
        return formatURL(CONNECTION_TYPE, HOST, service, null);
    }
}
