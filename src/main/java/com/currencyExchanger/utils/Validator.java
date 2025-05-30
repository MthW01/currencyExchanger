package com.currencyExchanger.utils;

import com.currencyExchanger.controller.customExceptions.WrongRequestException;

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

    public static Double parseDouble(String rate) {
        try {
            return Double.parseDouble(rate);
        } catch (Exception e) {
            throw new WrongRequestException("Rate value");
        }
    }

    public static String parseCode(String code) {
        if (code.length() != 3 || !code.toUpperCase().matches("[A-Z]{3}")) {
            throw new WrongRequestException("Currency code value " + code);
        }
        return code.toUpperCase();
    }
}
