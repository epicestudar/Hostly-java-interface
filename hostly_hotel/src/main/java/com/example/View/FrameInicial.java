package com.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameInicial extends JFrame{
    private JButton loginButton;
    private JButton cadastroButton;

    public FrameInicial() {
         setTitle("Sistema de Reservas de Hotel");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout
        setLayout(new GridLayout(3, 1));
        
        // Criação dos botões
        loginButton = new JButton("Login");
        cadastroButton = new JButton("Cadastro");
        
        // Adicionando os botões ao frame
        add(new JLabel("Bem-vindo ao Sistema de Reservas de Hotel", JLabel.CENTER));
        add(loginButton);
        add(cadastroButton);
        
        // Ações dos botões
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
                dispose(); // Fechar o frame atual
            }
        });

        cadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroFrame cadastroFrame = new CadastroFrame();
                cadastroFrame.setVisible(true); // Tornar o frame de cadastro visível
                // dispose(); // Não fechar o frame inicial
            }
        });
        
        setVisible(true);
    }
    }
