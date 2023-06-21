package org.example.swing;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JDialog{
    private JPanel here;
    private JTextField textField1;
    private JTextField textField2;
    private JButton loginButton;

    public LoginView(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(here);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
}
