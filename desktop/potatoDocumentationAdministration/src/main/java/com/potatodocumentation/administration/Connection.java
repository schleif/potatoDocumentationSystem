/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

//HTTP Connection
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fiel
 */
public class Connection {

    private final static String HOST = "http://134.169.47.160/";
    // 
    URL url;
    HttpURLConnection connection;
    private String requestBody;
    private String urlString;
    private Map<String, String> params;

    // InputStream
    private InputStream responseStream;

    /**
     *
     * @param service the webservice name
     */
    public Connection(String service) {
        this(service, null);
    }

    /**
     *
     * @param service the webserivce name
     * @param values HashMap Key: Attributename and Values: Value
     */
    public Connection(String service, Map<String, String> values) {
        params = values;

        urlString = HOST + service;

        requestBody = buildRequestBody();

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        sendPOST();

        try {
            responseStream = connection.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("URL " + urlString + ", POST: " + requestBody);
    }

    public URL getServiceURL() {
        return this.url;
    }

    public HttpURLConnection getServiceConn() throws IOException {
        if (this.connection == null) {
            this.connection = (HttpURLConnection) this.url.openConnection();
        }

        return this.connection;
    }

    private String buildRequestBody() {

        String requestString = "";

        if (params == null) {
            return requestString;
        }

        // Iterating thru the HashMap
        // Building the POST-body and encoding it
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                String keyString = URLEncoder.encode(entry.getKey(), "UTF-8");
                String valueString = URLEncoder.encode(entry.getValue(), "UTF-8");
                requestString += keyString + "=" + valueString + "&";
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }

        int length = requestString.length();
        //Delete last '&'
        if (length > 0) {
            requestString = requestString.substring(0, length - 1);
        }

        return requestString;

    }

    private void sendPOST() {
        try {
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);

            DataOutputStream out
                    = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(requestBody);

            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public InputStream getResponseStream() {
        return responseStream;
    }

}
