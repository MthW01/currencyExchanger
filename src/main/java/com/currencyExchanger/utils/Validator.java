package com.currencyExchanger.utils;

public class Validator {
    public static boolean isCodeValid(String code) {
        if (code.length() == 3) {
            for (var codeChar : code.toUpperCase().getBytes()) {
                if (codeChar > 'Z' || codeChar < 'A') {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
