package com.dtp;

/**
 * Created by ner on 4/6/17.
 */

public class Util {

    public static String toLowerFistLetter(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    public static String toUpperFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
