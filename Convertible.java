package com.currencyconverter.model;

public interface Convertible {
    double convert(double amount, String from, String to);
}
