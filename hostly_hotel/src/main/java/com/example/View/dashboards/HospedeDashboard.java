package com.example.view.dashboards;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class HospedeDashboard extends JFrame {
    public HospedeDashboard() {
        setTitle("Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem-vindo ao Dashboard!", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
