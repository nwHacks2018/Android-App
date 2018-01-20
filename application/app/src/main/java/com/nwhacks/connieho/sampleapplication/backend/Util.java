package com.nwhacks.connieho.sampleapplication.backend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by connieho on 2018-01-13.
 */

public class Util {

    static StringBuilder inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (Exception e) {
        }
        return answer;
    }
}
