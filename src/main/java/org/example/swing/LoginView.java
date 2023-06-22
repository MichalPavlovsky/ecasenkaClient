package org.example.swing;

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
        loginButton.addActionListener(e -> submitForm());
    }

    private void submitForm() {
        String email = emailField.getText();
        String password = passwordField1.getUIClassID();
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
                reader.close();

                authToken = response.toString();
                System.out.println(authToken);
                JOptionPane.showMessageDialog(this, "POST request successful");
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
