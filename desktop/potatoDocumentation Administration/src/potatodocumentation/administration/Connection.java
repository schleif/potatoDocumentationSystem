/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potatodocumentation.administration;

//HTTP Connection
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.io.IOException;

import java.util.HashMap;
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
            this.serviceConn = (HttpURLConnection) serviceURL.openConnection();
            this.res = this.serviceConn.getInputStream();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param service the webserivce name
     * @param values HashMap Key: Attributename and Values: Value
     */
    public Connection(String service, HashMap<String, String> values) {
        String urlString = HOST + service + "?";

        // Iterating thru the HashMap
        // Building the url
        Iterator ite = values.entrySet().iterator();
        boolean firstChar = true;
        while (ite.hasNext()) {
            if (firstChar) {
                Map.Entry pair = (Map.Entry) ite.next();
                urlString += pair.getKey() + "=" + pair.getValue();
                firstChar = false;
            } else {
                Map.Entry pair = (Map.Entry) ite.next();
                urlString += "&" + pair.getKey() + "=" + pair.getValue();
            }

        }
        
        System.out.println(urlString);
        
        try {
            this.serviceURL = new URL(urlString);
            this.serviceConn = (HttpURLConnection) serviceURL.openConnection();
            this.res = this.serviceConn.getInputStream();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        return this.res;
    }

}
