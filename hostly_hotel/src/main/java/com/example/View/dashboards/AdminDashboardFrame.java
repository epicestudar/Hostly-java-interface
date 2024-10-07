package com.example.view.dashboards;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AdminDashboardFrame extends JFrame {
    public AdminDashboardFrame() {
        setTitle("Painel Administrativo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Bem-vindo ao Painel Administrativo", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
