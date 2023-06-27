package org.example.swing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginView extends JDialog{
    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        LoginView.authToken = authToken;
    }

    public static String authToken;
    private JPanel here;
    private JTextField emailField;
    private JButton loginButton;
    private JPasswordField passwordField1;
    public static String NIECO;

    public static String getNIECO() {
        return NIECO;
    }

    public static void setNIECO(String NIECO) {
        LoginView.NIECO = NIECO;
    }

    public LoginView(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(here);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        loginButton.addActionListener(e -> submitForm(parent));
    }

    private void submitForm(JFrame parent) {
        String email = emailField.getText();
        String password = passwordField1.getText();
        try {
            URL url = new URL("http://localhost:8080/api/v1/auth/authenticate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonBody =  "{\n" +
                    "    \"email\":\""+email+"\",\n" +
                    "    \"password\" : \""+password+"\"\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String responseText = response.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseText);

                String role = jsonResponse.get("role").asText();
                String token = jsonResponse.get("token").asText();
                setAuthToken(token);
                String username  = jsonResponse.get("name").asText();


                if ("USER".equals(role)) {
                    JOptionPane.showMessageDialog(this, "POST request successful");
                    PatientWindow patientWindow = new PatientWindow(parent);
                    setVisible(false);
                    setComBox(patientWindow.getComboBox1());
                    patientWindow.setUvod("ahoj "+username);
                    patientWindow.setVisible(true);
                    patientWindow.getVybratButton().addActionListener(e -> {
                        String selectedDoctor= (String) patientWindow.getComboBox1().getSelectedItem();
                        getDataFromdoctor(selectedDoctor);
                    });

                }
                else if ("DOCTOR".equals(role)) {
                    JOptionPane.showMessageDialog(this, "POST request successful");
                    DoctorWindow doctorWindow = new DoctorWindow(parent);
                    setVisible(false);
                    doctorWindow.setNamefield("Dobry den pan doktor "+ username);
                    doctorWindow.setVisible(true);

                }


            } else {
                JOptionPane.showMessageDialog(this, "POST request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void getDataFromdoctor(String selectedDoctor) {

    }

    private void setComBox(JComboBox comboBox1) {
        try {
            URL url = new URL("http://localhost:8080/api/get/doctors");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken());

            // Získať JSON response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResponse.append(line);
                }
                reader.close();

                // Spracovať JSON response a získať zoznam mien
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, String>> responseList = objectMapper.readValue(jsonResponse.toString(), new TypeReference<>() {});

                // Vytvorenie zoznamu mien
                List<String> nameList = new ArrayList<>();
                for (Map<String, String> responseItem : responseList) {
                    String fullName = responseItem.get("fullName");
                    nameList.add(fullName);
                }

                // Vypíšte zoznam mien
                for (String name : nameList) {
                    comboBox1.addItem(name);
                }
            } else {
                // Neúspešná odpoveď
                System.out.println("Chyba pri získavaní JSON response. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
