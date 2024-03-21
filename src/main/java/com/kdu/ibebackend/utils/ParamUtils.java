package com.kdu.ibebackend.utils;

public class ParamUtils {
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }
}
