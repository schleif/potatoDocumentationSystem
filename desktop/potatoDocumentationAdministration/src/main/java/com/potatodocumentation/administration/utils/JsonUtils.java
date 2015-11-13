/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potatodocumentation.administration.Connection;
import com.potatodocumentation.administration.MainApplication;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Ochi
 */
public class JsonUtils {
    
    private static final AtomicInteger openConnections = new AtomicInteger(0);

    /**
     * Converts a JsonArray from a given URL with the given JsonTag
     * <i>resultTag</i>
     * to an ArrayList with Map<String,Object>-Elements.
     * <p>
     * This method creates a {@link Connection}-Instance, therefore the String
     * <i>serviceURL</i> is concatenated to {@link Connection.HOST}. For example
     * to connect to http://google.com/example.php, with {@link Connection.HOST}
     * being equal to "http://google.com/", <i>serviceURL</i> has to be equal to
     * "example.php".
     * <p>
     * If the Map<String, String> <i>params</i> doesn't equal <b>null</b> the
     * contained Key-Value pairs are going to be concatenated to the URL in form
     * of a GET-request. See
     * {@link Connection#Connection(java.lang.String, java.util.Map)} for more
     * information.
     * <p>
     * This method checks if a given JsonTag <i>successTag</i> is equal to the
     * given Object <i>successIndicator</i>. If thats not case <b>null</b> is
     * going to be returned.
     *
     *
     * @param serviceURL The serviceURL which is going to be concatenated to the
     * HOST-URL
     * @param params GET-Params (OPTIONAL)
     * @param successTag JsonTag which is going to be compared to
     * <i>successIndicator</i>
     * @param successIndicator The Object that is going to be comapred to the
     * JsonTag <i>successTag</i>
     * @param resultTag
     * @return An ArrayList<Map<String,Object>> of the given JsonTag
     * <i>resultTag</i> if existing and if the operation succeeded, <b>null</b>
     * otherwise.
     */
    public static ArrayList<Map<String, Object>> getJsonResultArray(String serviceURL,
            Map<String, String> params, String successTag, Object successIndicator, String resultTag) {
        //Indicate network usage
        MainApplication mainApp = MainApplication.getInstance();
        mainApp.showLoadBar(true);
        openConnections.incrementAndGet();
        mainApp.updateConnections();
        
        //Create connection to get URL
        Connection connection;
        if (params == null) {
            connection = new Connection(serviceURL);
        } else {
            connection = new Connection(serviceURL, params);
        }

        URL url = connection.getServiceURL();

        //Map the Json result
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonResult = null;

        try {
            jsonResult = mapper.readValue(url, Map.class);
        } catch (IOException ex) {
        }

        //Check if success
        boolean success = (jsonResult != null)
                && (jsonResult.get(successTag).equals(successIndicator));

        ArrayList<Map<String, Object>> resultList = null;

        if (success) {
            resultList = (ArrayList<Map<String, Object>>) jsonResult.get(resultTag);
        }
        
        if(openConnections.decrementAndGet() <= 0){
            mainApp.showLoadBar(false);
        }
        mainApp.updateConnections();

        
        return resultList;
    }

    /**
     * Returns {@link #getJsonResultArray(java.lang.String, java.lang.String, java.lang.Object, java.lang.String)
     * } with the following parameters:
     * <pre>
     * serviceURL = serviceURL
     * params = <b>null</b>
     * successTag = "success"
     * successIndicator = 1
     * resultTag = "Result"
     * </pre>
     *
     * @param serviceURL The serviceURL of the HOST-URL
     * @return The JsonArray as ArrayList
     * @see #getJsonResultArray(java.lang.String, java.lang.String,
     * java.lang.Object, java.lang.String)
     */
    public static ArrayList<Map<String, Object>> getJsonResultArray(String serviceURL) {
        return getJsonResultArray(serviceURL, null, "success", 1, "Result");
    }

    /**
     * Returns {@link #getJsonResultArray(java.lang.String, java.lang.String, java.lang.Object, java.lang.String)
     * } with the following parameters:
     * <pre>
     * serviceURL = serviceURL
     * params = params
     * successTag = "success"
     * successIndicator = 1
     * resultTag = "Result"
     * </pre>
     *
     * @param serviceURL
     * @param params
     * @return
     */
    public static ArrayList<Map<String, Object>> getJsonResultArray(String serviceURL,
            Map<String, String> params) {
        return getJsonResultArray(serviceURL, params, "success", 1, "Result");
    }

    /**
     * Work similiar to 
     * {@link #getJsonResultArray(java.lang.String, java.util.Map, java.lang.String, java.lang.Object, java.lang.String) }
     * , except just the SuccessStatus is returned.
     *
     * @param serviceURL
     * @param params
     * @param successTag
     * @param successIndicator
     * @return The SuccessStatus
     *
     */
    public static boolean getJsonSuccessStatus(String serviceURL,
            Map<String, String> params, String successTag, Object successIndicator) {

        //Create connection to get URL
        Connection connection;
        if (params == null) {
            connection = new Connection(serviceURL);
        } else {
            connection = new Connection(serviceURL, params);
        }

        URL url = connection.getServiceURL();

        //Map the Json result
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonResult = null;

        try {
            jsonResult = mapper.readValue(url, Map.class);
        } catch (IOException ex) {
        }

        //Check if success
        return (jsonResult != null)
                && (jsonResult.get(successTag).equals(successIndicator));

    }

    /**
     * Returns {@link #getJsonSuccessStatus(java.lang.String, java.lang.String, java.lang.Object)
     * } with the following parameters:
     * <pre>
     * serviceURL = serviceURL
     * params = <b>null</b>
     * successTag = "success"
     * successIndicator = 1
     * </pre>
     *
     * @param serviceURL
     * @return
     */
    public static boolean getJsonSuccessStatus(String serviceURL) {
        return getJsonSuccessStatus(serviceURL, null, "success", 1);
    }

    /**
     * Returns {@link #getJsonSuccessStatus(java.lang.String, java.lang.String, java.lang.Object)
     * } with the following parameters:
     * <pre>
     * serviceURL = serviceURL
     * params = params
     * successTag = "success"
     * successIndicator = 1
     * </pre>
     *
     * @param serviceURL
     * @return
     */
    public static boolean getJsonSuccessStatus(String serviceURL,
            Map<String, String> params) {
        return getJsonSuccessStatus(serviceURL, params, "success", 1);
    }

    public static ObservableList<String> getJsonResultObservableList(
            String key, String serviceURL, Map<String, String> params) {

        ObservableList<String> observables = FXCollections.observableArrayList();

        ArrayList<Map<String, Object>> jsonResult
                = getJsonResultArray(serviceURL, params);

        if (jsonResult != null) {
            jsonResult.stream().forEach((value) -> {
                observables.add((String) value.get(key));
            });
        } else {
            observables.add("Ladefehler!");
        }
        
        return observables;

    }

    public static int getOpenConnections() {
        return openConnections.get();
    }

}
