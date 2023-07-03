package org.example.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.example.swing.LoginView.getAuthToken;

public class PatientWindow extends  JDialog{

    private int idPatient;
    private int idDoctor;
    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    private JPanel patient;
    private JLabel uvod;
    private JComboBox comboBox1;

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(String name) {
        this.comboBox1.addItem(name);
    }

    private JLabel vyzva;

    public JButton getVybratButton() {
        return vybratButton;
    }

    public void setVybratButton(String name) {
        this.vybratButton.setText(name);
    }

    private JButton vybratButton;

    public JLabel getUvod() {
        return uvod;
    }

    public void setUvod(String text) {
        this.uvod.setText(text);
    }

    public PatientWindow(JFrame parent){
        super(parent);
        setTitle("PatientWindow");
        setContentPane(patient);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        vybratButton.addActionListener(e -> {
            String selectedDoctor= (String) comboBox1.getSelectedItem();
            getDataFromdoctor(selectedDoctor);
            openTable(parent);
        });
    }

    private void openTable(JFrame parent) {
        TableOfTerms table = new TableOfTerms(parent, getIdDoctor());
        table.setIdPatient(getIdPatient());
        table.setIdDoctor(getIdDoctor());
        setVisible(false);
        table.setVisible(true);
    }

    private void getDataFromdoctor(String selectedDoctor) {
        String[] parts = selectedDoctor.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];
        try {
            URL url = new URL("http://localhost:8080/api/get/getid");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String jsonBody = "{\n" +
                    "    \"firstname\":\"" + firstName + "\",\n" +
                    "    \"lastname\" : \"" + lastName + "\"\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String responseString = reader.readLine();
                reader.close();
                int idDoctor = Integer.parseInt(responseString);
                setIdDoctor(idDoctor);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
