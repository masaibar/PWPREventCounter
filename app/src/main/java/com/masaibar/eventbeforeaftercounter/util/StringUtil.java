package com.masaibar.eventbeforeaftercounter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by masaibar on 2016/02/28.
 */
public class StringUtil {

    public static String fromInputeStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        try {
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }
}
