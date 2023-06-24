package org.example.swing;

import javax.swing.*;
import java.awt.*;

public class PatientWindow extends  JDialog{
    private JPanel patient;
    private JLabel uvod;

    public JLabel getUvod() {
        return uvod;
    }

    public void setUvod(JLabel uvod) {
        this.uvod = uvod;
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

    }
}
