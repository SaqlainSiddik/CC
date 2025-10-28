package com.currencyconverter.gui;

import com.currencyconverter.model.Conversion;
import com.currencyconverter.model.Currency;
import com.currencyconverter.util.Validator;
import com.currencyconverter.api.ExchangeRateAPI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextField amountField;
    private JComboBox<Currency> fromCurrency;
    private JComboBox<Currency> toCurrency;
    private JLabel resultLabel;
    private JLabel rateLabel; // 💹 new label for live rate

    public MainFrame() {
        setTitle("Live Exchange Rate Currency Converter");
        setSize(560, 390);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 77, 77)); // Dark teal
        header.setPreferredSize(new Dimension(560, 65));
        JLabel title = new JLabel("Live Exchange Rate Currency Converter (Addition via Space)", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245)); // Light grey
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Enter Amount:"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(amountField, gbc);

        // From Currency
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("From:"), gbc);

        gbc.gridx = 1;
        fromCurrency = new JComboBox<>(getCurrencyList());
        fromCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(fromCurrency, gbc);

        // Swap Button 🔁
        gbc.gridx = 2; gbc.gridy = 1;
        JButton swapButton = new JButton("↔");
        swapButton.setBackground(new Color(0, 102, 102));
        swapButton.setForeground(Color.WHITE);
        swapButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        swapButton.setFocusPainted(false);
        swapButton.setBorderPainted(false);
        panel.add(swapButton, gbc);

        // To Currency
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("To:"), gbc);

        gbc.gridx = 1;
        toCurrency = new JComboBox<>(getCurrencyList());
        toCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(toCurrency, gbc);

        // Convert Button
        gbc.gridx = 1; gbc.gridy = 3;
        JButton convertButton = new JButton("Convert Now");
        convertButton.setBackground(new Color(0, 128, 128));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        convertButton.setFocusPainted(false);
        convertButton.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        panel.add(convertButton, gbc);

        // 💹 Rate Label (live rate)
        gbc.gridx = 1; gbc.gridy = 4;
        rateLabel = new JLabel("Live Rate: --");
        rateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rateLabel.setForeground(new Color(0, 77, 77));
        panel.add(rateLabel, gbc);

        // Result Label
        gbc.gridx = 1; gbc.gridy = 5;
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        resultLabel.setForeground(new Color(0, 77, 77));
        panel.add(resultLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(240, 240, 240));
        JLabel credits = new JLabel("Durgesh & Saqlain", SwingConstants.LEFT);
        credits.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        credits.setForeground(new Color(80, 80, 80));
        footer.add(credits, BorderLayout.WEST);
        add(footer, BorderLayout.SOUTH);

        // ===== EVENTS =====
        convertButton.addActionListener(e -> handleConversion());
        swapButton.addActionListener(e -> swapCurrencies());

        // Live rate update when dropdowns change
        fromCurrency.addActionListener(e -> updateLiveRate());
        toCurrency.addActionListener(e -> updateLiveRate());

        // Set initial live rate
        updateLiveRate();

        setVisible(true);
    }

    private Currency[] getCurrencyList() {
        return new Currency[]{
                new Currency("USD", "$", "US Dollar"),
                new Currency("INR", "₹", "Indian Rupee"),
                new Currency("EUR", "€", "Euro"),
                new Currency("GBP", "£", "British Pound"),
                new Currency("JPY", "¥", "Japanese Yen"),
                new Currency("CHF", "₣", "Swiss Franc"),
                new Currency("CAD", "$", "Canadian Dollar"),
                new Currency("AUD", "$", "Australian Dollar")
        };
    }

    private void handleConversion() {
        String text = amountField.getText().trim();
        if (!Validator.isValidInput(text)) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric amounts separated by space or comma!");
            return;
        }

        try {
            double total = 0.0;
            for (String part : text.split("[,\\s]+"))
                if (!part.isEmpty()) total += Double.parseDouble(part);

            Currency from = (Currency) fromCurrency.getSelectedItem();
            Currency to = (Currency) toCurrency.getSelectedItem();

            Conversion conversion = new Conversion();
            double result = conversion.convert(total, from.getCode(), to.getCode());

            resultLabel.setText("Result: " + String.format("%.2f", result) + " " + to.getSymbol() + " (" + to.getCode() + ")");
            updateLiveRate(); // refresh live rate when convert pressed
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void swapCurrencies() {
        int fromIndex = fromCurrency.getSelectedIndex();
        int toIndex = toCurrency.getSelectedIndex();
        fromCurrency.setSelectedIndex(toIndex);
        toCurrency.setSelectedIndex(fromIndex);
        updateLiveRate(); // update after swap
    }

    private void updateLiveRate() {
        try {
            Currency from = (Currency) fromCurrency.getSelectedItem();
            Currency to = (Currency) toCurrency.getSelectedItem();

            double rate = ExchangeRateAPI.getRate(from.getCode(), to.getCode());
            rateLabel.setText(String.format("Live Rate: 1 %s = %.3f %s", from.getCode(), rate, to.getCode()));
        } catch (Exception e) {
            rateLabel.setText("Live Rate: unavailable");
        }
    }
}
