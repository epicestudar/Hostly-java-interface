package com.example.view;

import javax.swing.*;

import com.example.model.Hospede;
import com.example.view.dashboards.AdminDashboardFrame;
import com.example.view.dashboards.HospedeDashboard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
        
                try {
                    // Monta o JSON com as credenciais
                    String jsonInputString = String.format(
                        "{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha
                    );
        
                    // Fazendo a requisição POST para a API de login
                    URL url = new URL("http://localhost:8080/api/administrador/login");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setDoOutput(true);
        
                    // Enviando o corpo da requisição
                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
        
                    // Verificando a resposta da API
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Administrador logado com sucesso!");
                        // Aqui você pode redirecionar o administrador para o dashboard
                        new AdminDashboardFrame();
                        dispose(); // Fecha o frame atual
                    } else {
                        // Ler a resposta do servidor
                        InputStream is = con.getErrorStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
        
                        JOptionPane.showMessageDialog(null, "Erro: " + response.toString());
                    }
        
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao realizar login: " + ex.getMessage());
                }
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
        
                try {
                    // Montar o JSON com as credenciais
                    String jsonInputString = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);
        
                    // Fazer a requisição POST para o endpoint de login
                    URL url = new URL("http://localhost:8080/api/hospedes/login");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setDoOutput(true);
        
                    // Enviar o JSON com as credenciais
                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
        
                    // Verificar a resposta do servidor
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                        // Redirecionar para o dashboard ou nova tela
                        new HospedeDashboard();
                        dispose(); // Fecha a tela de login
                    } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        JOptionPane.showMessageDialog(null, "Credenciais inválidas");
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        JOptionPane.showMessageDialog(null, "Hóspede não encontrado");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao realizar login: " + ex.getMessage());
                }
            }
        });
        

        // Ação do botão de cadastro
        cadastroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HospedeCadastroFrame cadastroFrame = new HospedeCadastroFrame();
                cadastroFrame.setVisible(true);
                setVisible(false); // Esconde o frame de login
            }
        });
    }

    // Método para realizar login via API
    private void realizarLogin(String email, String senha) {
        try {
            // Montando o JSON com as credenciais
            String jsonInputString = String.format("{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

            // Fazendo a requisição POST para a API de login
            URL url = new URL("http://localhost:8080/api/hospedes/login"); // Ajuste conforme o endpoint correto
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            // Enviando o corpo da requisição (JSON)
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificando a resposta da API
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Login bem-sucedido, redirecionar para o dashboard
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                new HospedeDashboard(); // Redireciona para o dashboard
                dispose(); // Fecha o frame de login
            } else {
                // Credenciais inválidas ou erro no login
                JOptionPane.showMessageDialog(this, "Credenciais incorretas.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + e.getMessage());
        }
    }
}
