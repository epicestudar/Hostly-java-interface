package com.example.View;

import javax.swing.JFrame;
import javax.swing.JLabel;

class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem-vindo ao Dashboard!", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
