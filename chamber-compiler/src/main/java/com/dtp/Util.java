package com.dtp;

/**
 * Created by ner on 4/6/17.
 */

class Util {

    static String toLowerFistLetter(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    static String toUpperFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    static String formatAsStaticFinalName(String name) {
        String formattedVariableName = "";

        String[] splitName = name.split("(?=\\p{Upper})");

        for (int i = 0; i < splitName.length; i++) {
            formattedVariableName += splitName[i].toUpperCase();

            if (i < splitName.length - 1)
                formattedVariableName += "_";
        }

        return formattedVariableName;
    }
}
