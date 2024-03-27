package com.kdu.ibebackend.constants;

import java.util.List;

/**
 * Constants for use in Security filters and services
 */
public class AuthConstants {
    public static String AUTH_TOKEN_HEADER = "X-Api-Key";
    public static String AUTH_TOKEN = "AXydZlI2gmTmB/XSX/r4UQncEU+O3ZjPtolBcMKYpvc=";

    public static List<String> WhiteListURLs = List.of(
            "http://kdu-team1-frontend.s3-website.ap-south-1.amazonaws.com",
            "http://localhost:4173",
            "http://localhost:5173",
            "https://sprint-3.d33gvmzav8u7ie.amplifyapp.com",
            "https://sprint-4.d33gvmzav8u7ie.amplifyapp.com"
    );
}
