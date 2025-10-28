package com.currencyconverter.model;

import com.currencyconverter.api.ExchangeRateAPI;

public class Conversion implements Convertible {
    @Override
    public double convert(double amount, String from, String to) {
        double rate = ExchangeRateAPI.getRate(from, to);
        return amount * rate;
    }
}
