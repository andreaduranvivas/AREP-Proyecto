package edu.arep.service;

import com.google.gson.Gson;
import edu.arep.model.Transaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FraudDetectionService {

    private static final String FRAUD_DETECTION_URL = "https://your-fraud-detection-service-endpoint";
    private final Gson gson = new Gson();

    public boolean isTransactionFraudulent(Transaction transaction) {
        try {
            URL url = new URL(FRAUD_DETECTION_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = gson.toJson(transaction);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return Boolean.parseBoolean(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
