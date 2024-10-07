package com.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameInicial extends JFrame {
    private JButton loginAdminButton;
    private JButton loginHospedeButton;

    public FrameInicial() {
        setTitle("Sistema de Reservas de Hotel");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new GridLayout(3, 1));

        // Criação dos botões
        loginAdminButton = new JButton("Login Administrador");
        loginHospedeButton = new JButton("Login Hóspede");

        // Adicionando os botões ao frame
        add(new JLabel("Bem-vindo ao Sistema de Reservas de Hotel", JLabel.CENTER));
        add(loginAdminButton);
        add(loginHospedeButton);

        // Ações dos botões
        loginAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginAdminFrame(); // Abre o frame de login do administrador
                dispose(); // Fechar o frame atual
            }
        });

        loginHospedeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginHospedeFrame(); // Abre o frame de login do hóspede
                dispose(); // Fechar o frame atual
            }
        });

        setVisible(true);
    }
}

class LoginAdminFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;

    public LoginAdminFrame() {
        setTitle("Login Administrador");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        loginButton = new JButton("Realizar Login");
        panel.add(loginButton);

        add(panel);

        setVisible(true);

        // Adicionando ação do botão de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
                // Lógica de autenticação para o administrador
                JOptionPane.showMessageDialog(null, "Administrador logado com sucesso!");
            }
        });
    }
}

class LoginHospedeFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastroButton;

    public LoginHospedeFrame() {
        setTitle("Login Hóspede");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        loginButton = new JButton("Login");
        panel.add(loginButton);

        cadastroButton = new JButton("Fazer Cadastro");
        panel.add(cadastroButton);

        add(panel);

        setVisible(true);

        // Adicionando ação do botão de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
                // Lógica de autenticação para o hóspede
                JOptionPane.showMessageDialog(null, "Hóspede logado com sucesso!");
            }
        });

        // Adicionando ação do botão de cadastro
        cadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroFrame cadastroFrame = new CadastroFrame(); // Abre o frame de cadastro
                cadastroFrame.setVisible(true); // Torna visível o frame de cadastro
                setVisible(false); // Apenas esconde o frame de login do hóspede em vez de fechar
            }
        });
    }
}