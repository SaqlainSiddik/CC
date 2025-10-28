package com.currencyconverter.model;

public class Currency {
    private String code;
    private String symbol;
    private String name;

    public Currency(String code, String symbol, String name) {
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getSymbol() { return symbol; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name + " (" + symbol + " - " + code + ")";
    }
}
