package org.example.swing;

import javax.swing.*;
import java.awt.*;

public class DoctorWindow extends JDialog {
    private JPanel doctorpanel;
    private JLabel namefield;

    public JLabel getNamefield() {
        return namefield;
    }

    public void setNamefield(JLabel namefield) {
        this.namefield = namefield;
    }

    public DoctorWindow(JFrame parent){
        super(parent);
        setTitle("DoctorWindow");
        setContentPane(doctorpanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

    }
}
