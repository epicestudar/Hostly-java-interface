package com.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CadastroFrame extends JFrame {
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField emailField;
    private JTextField telefoneField;
    private JTextField senhaField;
    private JTextField dataNascimentoField;

    public CadastroFrame() {
        setTitle("Cadastro de Hóspede");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(7, 2));

        // Adicionando campos de texto para o formulário
        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        panel.add(cpfField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        panel.add(telefoneField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        panel.add(new JLabel("Data de Nascimento (yyyy-mm-dd):"));
        dataNascimentoField = new JTextField();
        panel.add(dataNascimentoField);

        // Botão de cadastro
        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarHospede();
            }
        });
        panel.add(cadastrarButton);

        add(panel);
    }

    // Método para enviar os dados do hóspede para a API via POST
    private void cadastrarHospede() {
        try {
            // Capturando os dados dos campos
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String email = emailField.getText();
            String telefone = telefoneField.getText();
            String senha = senhaField.getText();
            String dataNascimento = dataNascimentoField.getText();
    
            // Montando o JSON com os dados do hóspede
            String jsonInputString = String.format(
                    "{\"nome\":\"%s\", \"cpf\":\"%s\", \"email\":\"%s\", \"telefone\":\"%s\", \"senha\":\"%s\", \"dataNascimento\":\"%s\"}",
                    nome, cpf, email, telefone, senha, dataNascimento
            );
    
            // Fazendo a requisição POST para a API
            URL url = new URL("http://localhost:8080/api/hospedes"); // URL do endpoint da API
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
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Hóspede cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar hóspede. Código de resposta: " + responseCode);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar hóspede: " + e.getMessage());
        }
    }
    
}