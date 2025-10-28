package com.currencyconverter.util;

public class Validator {
    public static boolean isValidInput(String text) {
        return text.matches("^[0-9.,\\s]+$");
    }
}
