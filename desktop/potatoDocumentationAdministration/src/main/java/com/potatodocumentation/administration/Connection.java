/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

//HTTP Connection
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author fiel
 */
public class Connection {

    private final static String HOST = "http://134.169.47.160/";
    // 
    URL serviceURL;
    HttpURLConnection serviceConn;

    // InputStream
    InputStream res;

    /**
     *
     * @param service the webservice name
     */
    public Connection(String service) {
        String urlString = HOST + service;
        try {
            this.serviceURL = new URL(urlString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param service the webserivce name
     * @param values HashMap Key: Attributename and Values: Value
     */
    public Connection(String service, Map<String, String> values) {
        String urlString = HOST + service + "?";

        // Iterating thru the HashMap
        // Building the url
        Iterator ite = values.entrySet().iterator();
        boolean firstChar = true;
        try {
            while (ite.hasNext()) {
                if (firstChar) {
                    Map.Entry pair = (Map.Entry) ite.next();
                    urlString += pair.getKey() + "=" + URLEncoder.encode((String) pair.getValue(), "UTF-8");
                    firstChar = false;
                } else {
                    Map.Entry pair = (Map.Entry) ite.next();
                    urlString += "&" + pair.getKey() + "=" + URLEncoder.encode((String) pair.getValue(), "UTF-8");
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(urlString);

        try {
            this.serviceURL = new URL(urlString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public InputStream getInputStream() throws IOException {
        if (this.res == null) {
            this.res = getServiceConn().getInputStream();
        }
        return this.res;
    }

    public URL getServiceURL() {
        return this.serviceURL;
    }

    public HttpURLConnection getServiceConn() throws IOException {
        if (this.serviceConn == null) {
            this.serviceConn = (HttpURLConnection) this.serviceURL.openConnection();
        }

        return this.serviceConn;
    }

}
