package com.neighbourhood.online.neighbourhood.utils;


import java.util.Base64;

import static java.util.Base64.getEncoder;

public class Utility {

    public static String encodePassword(String password){
        return getEncoder().encodeToString(password.getBytes());
    }

    public static String decodePassword(String encodedString){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}
