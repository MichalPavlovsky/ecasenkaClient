package org.example.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationForm extends JDialog {
    private JTextField emailField;
    private JTextField surname;
    private JButton registrateButton;
    private JPanel registerPanel;
    private JPasswordField passwordField1;
    private JTextField user;

    public RegistrationForm(JFrame parent) {
        super(parent, "Registration Form", true);
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        registrateButton.addActionListener(e -> submitForm());
    }

    private void submitForm() {
        String name = user.getText();
        String lastname = surname.getText();
        String email = emailField.getText();
        String password = passwordField1.getUIClassID();
        try {
            URL url = new URL("http://localhost:8080/api/v1/auth/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonBody = "{" +
                    "  \"firstname\":\"" + name + "\",\n" +
                    "  \"lastname\":\"" + lastname + "\",\n" +
                    "  \"email\": \"" + email + "\",\n" +
                    "  \"password\": \"" + password + "\"\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
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
