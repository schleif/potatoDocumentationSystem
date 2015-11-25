package com.potatodoc.potatodocumentation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Created by fiel on 20.11.2015.
 */
public class Misc {

    public static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String webPage = "",data="";
        while ((data = reader.readLine()) != null){
            webPage += data + "\n";
        }
        return webPage;
    }

}
