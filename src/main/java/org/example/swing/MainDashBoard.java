package org.example.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashBoard extends JFrame{
    private JButton signUpButton;
    private JPanel panel1;
    private JButton loginButton;
    private boolean starting=true;

    public MainDashBoard() {
    setTitle("Dashborad");
    setContentPane(panel1);
    setMinimumSize(new Dimension(500, 429));
    setSize(1200, 700);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    pack();
    signUpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            starting = false;
            setVisible(false);
            RegistrationForm registrationForm = new RegistrationForm(MainDashBoard.this);
            registrationForm.setLocationRelativeTo(null);
            registrationForm.setVisible(true);
        }
    }
    );
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                starting = false;
                setVisible(false);
                LoginView loginView = new LoginView(MainDashBoard.this);
                loginView.setLocationRelativeTo(null);
                loginView.setVisible(true);
            }
        });
        if (starting){
            setVisible(true);
        }
    }
    public static void main(String[] args) {
        MainDashBoard myForm = new MainDashBoard();
    }
}
