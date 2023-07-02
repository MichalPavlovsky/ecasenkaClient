package org.example.swing;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import static org.example.swing.LoginView.getAuthToken;

public class TableOfTerms extends JDialog {
    private int idPatient;
    private int idDoctor;

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    private JButton[][] buttons;

    public TableOfTerms(JFrame parent) {
        super(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Ecasenka");

        int pocetDni = 5;
        int pocetCasov = 8;

        buttons = new JButton[pocetCasov][pocetDni];

        setLayout(new GridLayout(pocetCasov + 1, pocetDni + 1));

        for (int den = 0; den < pocetDni; den++) {
            add(new JLabel(formatuj(LocalDate.now().plusDays(den))));
        }

        for (int cas = 0; cas < pocetCasov; cas++) {
            for (int den = 0; den < pocetDni; den++) {
                buttons[cas][den] = new VlastneTlacidlo(formatuj(LocalDate.now().plusDays(den)), cas + 7 + ":00");
                buttons[cas][den].setText(cas + 7 + ":00");
                add(buttons[cas][den]);
                if (checkbutton((VlastneTlacidlo) buttons[cas][den])){
                    buttons[cas][den].disable();
                };
            }
        }

        pack();
        setVisible(true);
    }

    private boolean checkbutton(VlastneTlacidlo jButton) {
        boolean check=false;
        try {
            URL url = new URL("http://localhost:8080/api/get/getterms");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String jsonBody = "{\n" +
                    "    \"id\":\"" + getIdDoctor()+ "\"\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            System.out.println(jsonBody);
            System.out.println(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println(responseCode);
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String responseText = response.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                List<TermDto> termDtos = objectMapper.readValue(responseText, new TypeReference<>() {});
                for(TermDto term:termDtos) {
                    System.out.println("term.getDate "+term.getDate() +" "+jButton.den +"&& " +term.getTime()+" jbutoncas: "+jButton.cas);
                    if (term.getDate() == jButton.den && term.getTime()==jButton.cas){
                        check=true;
                        System.out.println("som tu ");
                    }
                    }

                };
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }return check;

    }

    private String formatuj(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " ";
    }

    // Trieda pre vlastné tlačidlo
    class VlastneTlacidlo extends JButton {
        private String den;
        private String cas;

        public VlastneTlacidlo(String den, String cas) {
            this.den = den;
            this.cas = cas;

            addActionListener(e -> {
                System.out.println("Kliklo sa na tlačidlo: Deň " + (den) + ", Čas: " + cas);
                createTerm(VlastneTlacidlo.this);
            });
        }
    }

    private void createTerm(VlastneTlacidlo button) {
        try {
            URL url = new URL("http://localhost:8080/api/get/term");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + getAuthToken());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String jsonBody = "{\n" +
                    "    \"time\":\"" + button.cas + "\",\n" +
                    "    \"date\" : \"" + button.den + "\",\n" +
                    "    \"idDoctor\":\"" + getIdDoctor() + "\",\n" +
                    "    \"idPatient\" : \"" + getIdPatient() + "\"\n" +
                    "}";
            System.out.println(getIdDoctor());
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonBody.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

