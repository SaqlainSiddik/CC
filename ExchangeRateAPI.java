package com.currencyconverter.api;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Handles live exchange rates using Frankfurter API
 * Supports major global currencies (USD, INR, EUR, GBP, JPY, CHF, CAD, AUD)
 */
public class ExchangeRateAPI {
    public static double getRate(String from, String to) {
        try {
            // API URL — fetches real-time rate from Frankfurter.app
            String urlStr = "https://api.frankfurter.app/latest?from="
                    + URLEncoder.encode(from, "UTF-8")
                    + "&to=" + URLEncoder.encode(to, "UTF-8");

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(7000);
            conn.setReadTimeout(7000);

            // Read API response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONObject rates = json.getJSONObject("rates");

            // Return conversion rate if available
            if (rates.has(to)) {
                return rates.getDouble(to);
            } else {
                System.out.println("Error: Currency not supported in Frankfurter API");
                return 1.0;
            }

        } catch (Exception e) {
            System.out.println("API error: " + e.getMessage());
            return 1.0; // fallback if API fails
        }
    }
}
