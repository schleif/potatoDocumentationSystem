package com.potatodoc.potatodocumentation.utils;

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
        Scanner scanner = new Scanner(new InputStreamReader(stream, "UTF-8"));
        String result = "";
        while(scanner.hasNextLine()) {
            result.concat(scanner.nextLine());
        }
        return result;
    }

}
