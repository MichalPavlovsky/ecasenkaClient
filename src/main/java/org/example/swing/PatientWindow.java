package org.example.swing;

import javax.swing.*;
import java.awt.*;

public class PatientWindow extends  JDialog{
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

    public void setVybratButton(JButton vybratButton) {
        this.vybratButton = vybratButton;
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

    }
}
