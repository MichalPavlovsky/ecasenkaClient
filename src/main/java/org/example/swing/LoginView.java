package org.example.swing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginView extends JDialog{
    public static String authToken;
    private JPanel here;
    private JTextField emailField;
    private JButton loginButton;
    private JPasswordField passwordField1;

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
            System.out.println(jsonBody);

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
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
                String username  = jsonResponse.get("name").asText();


                if ("USER".equals(role)) {
                    JOptionPane.showMessageDialog(this, "POST request successful");
                    PatientWindow patientWindow = new PatientWindow(parent);
                    setVisible(false);

                    patientWindow.setUvod("ahoj "+username);
                    patientWindow.setVisible(true);

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

    }
