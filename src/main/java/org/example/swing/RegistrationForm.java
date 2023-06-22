package org.example.swing;

import javax.swing.*;
import java.awt.*;

public class RegistrationForm extends JDialog{
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton registrateButton;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent) {
        super(parent, "Registration Form", true);
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        setLocationRelativeTo(parent);

}}
