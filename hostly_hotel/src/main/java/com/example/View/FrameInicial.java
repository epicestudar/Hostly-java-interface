package com.example.view;

import javax.swing.*;

import org.json.JSONObject;

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
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Criação dos botões
        loginAdminButton = new JButton("Login Administrador");
        loginAdminButton.setBackground(new Color(70, 130, 180)); // Cor de fundo
        loginAdminButton.setForeground(Color.WHITE); // Cor do texto
        loginAdminButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte
        loginAdminButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem

        loginHospedeButton = new JButton("Login Hóspede");
        loginHospedeButton.setBackground(new Color(34, 139, 34));
        loginHospedeButton.setForeground(Color.WHITE);
        loginHospedeButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginHospedeButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adicionando os botões ao frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa duas colunas
        add(new JLabel("Bem-vindo ao Sistema de Reservas de Hotel", JLabel.CENTER), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reduz para uma coluna
        add(loginAdminButton, gbc);

        gbc.gridx = 1; // Botão do hóspede na próxima coluna
        add(loginHospedeButton, gbc);

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
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre componentes

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; // Próxima coluna
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 20)); // Tamanho fixo
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        senhaField = new JPasswordField();
        senhaField.setPreferredSize(new Dimension(150, 20));
        panel.add(senhaField, gbc);

        loginButton = new JButton("Realizar Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa duas colunas
        panel.add(loginButton, gbc);

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
                            "{\"email\":\"%s\", \"senha\":\"%s\"}", email, senha);

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
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre componentes

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 20));
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        senhaField = new JPasswordField();
        senhaField.setPreferredSize(new Dimension(150, 20));
        panel.add(senhaField, gbc);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa duas colunas
        panel.add(loginButton, gbc);

        cadastroButton = new JButton("Fazer Cadastro");
        cadastroButton.setBackground(new Color(34, 139, 34));
        cadastroButton.setForeground(Color.WHITE);
        cadastroButton.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 3; // Nova linha para o botão de cadastro
        panel.add(cadastroButton, gbc);

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
                        // Ler a resposta do servidor (dados do hóspede)
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Converter a resposta JSON para um objeto Hospede
                        JSONObject hospedeJson = new JSONObject(response.toString());
                        String cpfHospede = hospedeJson.getString("cpf"); // Captura o CPF do hóspede logado

                        JOptionPane.showMessageDialog(null, "Login bem-sucedido!");

                        // Passar o CPF para o dashboard
                        new HospedeDashboard(cpfHospede);
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
                // Login bem-sucedido, capturar os dados do hóspede
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Converter a resposta JSON para um objeto JSONObject
                JSONObject hospedeJson = new JSONObject(response.toString());
                String cpfHospede = hospedeJson.getString("cpf"); // Captura o CPF do hóspede logado

                // Exibe mensagem de sucesso
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");

                // Redireciona para o dashboard, passando o CPF do hóspede
                new HospedeDashboard(cpfHospede); // Chama o construtor com o CPF do hóspede
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