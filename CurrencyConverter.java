import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> baseCurrencyBox;
    private JComboBox<String> targetCurrencyBox;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;

    private static final String[] CURRENCIES = {
        "USD", "EUR", "GBP", "INR", "JPY", "AUD", "CAD", "CHF", "CNY"
    };

    private static final String API_KEY = "9fff775dad10b254dc833916";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 5));

        baseCurrencyBox = new JComboBox<>(CURRENCIES);
        targetCurrencyBox = new JComboBox<>(CURRENCIES);
        amountField = new JTextField();
        convertButton = new JButton("Convert");
        resultLabel = new JLabel("Converted amount will appear here.", SwingConstants.CENTER);

        add(createLabeledPanel("Select Base Currency:", baseCurrencyBox));
        add(createLabeledPanel("Select Target Currency:", targetCurrencyBox));
        add(createLabeledPanel("Enter Amount:", amountField));
        add(convertButton);
        add(resultLabel);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void convertCurrency() {
        String base = (String) baseCurrencyBox.getSelectedItem();
        String target = (String) targetCurrencyBox.getSelectedItem();
        String amountStr = amountField.getText();

        try {
            double amount = Double.parseDouble(amountStr);
            double rate = fetchExchangeRate(base, target);
            double converted = amount * rate;
            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, base, converted, target));
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter a valid number.");
        } catch (Exception ex) {
            resultLabel.setText("Error fetching exchange rate.");
        }
    }

    private double fetchExchangeRate(String base, String target) throws Exception {
        String urlStr = API_URL + API_KEY + "/latest/" + base;
        URL url = new URL(urlStr);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if (status != 200) throw new IOException("Failed to connect: HTTP " + status);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        String json = content.toString();
        String search = "\"" + target + "\":";
        int index = json.indexOf(search);
        if (index == -1) throw new RuntimeException("Currency not found in response.");

        int start = index + search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start); // last entry

        String valueStr = json.substring(start, end).trim();
        return Double.parseDouble(valueStr);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyConverter converter = new CurrencyConverter();
            converter.setVisible(true);
        });
    }
}