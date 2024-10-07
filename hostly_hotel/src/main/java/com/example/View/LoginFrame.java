package com.example.View;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout do painel
        setLayout(new GridLayout(3, 2));
        
        // Campos de entrada
        emailField = new JTextField();
        senhaField = new JPasswordField();
        loginButton = new JButton("Entrar");

        // Adicionando os campos ao frame
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Senha:"));
        add(senhaField);
        add(new JLabel());  // Placeholder
        add(loginButton);
        
        setVisible(true);
    }
}