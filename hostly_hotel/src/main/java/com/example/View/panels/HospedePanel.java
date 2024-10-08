package com.example.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class HospedePanel extends JPanel {
    private JTextField emailField;
    private JTextField cpfField;
    private JTable hospedesTable;
    private DefaultTableModel tableModel;
    private JButton searchByEmailButton;
    private JButton searchByCpfButton;
    private JButton listAllHospedesButton;

    public HospedePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240)); // Cor de fundo
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel superior para entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240)); // Fundo do painel de entrada

        // Estilo dos rótulos
        JLabel emailLabel = new JLabel("Procurar por Email:");
        JLabel cpfLabel = new JLabel("Procurar por CPF:");
        
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Adicionando componentes de input ao painel superior
        inputPanel.add(emailLabel);
        emailField = new JTextField();
        inputPanel.add(emailField);
        searchByEmailButton = new JButton("Buscar por Email");
        inputPanel.add(searchByEmailButton);

        inputPanel.add(cpfLabel);
        cpfField = new JTextField();
        inputPanel.add(cpfField);
        searchByCpfButton = new JButton("Buscar por CPF");
        inputPanel.add(searchByCpfButton);

        inputPanel.add(new JLabel()); // Espaço vazio para alinhamento
        listAllHospedesButton = new JButton("Listar Todos os Hóspedes");
        inputPanel.add(listAllHospedesButton);

        add(inputPanel, BorderLayout.NORTH);

        // Configurando a tabela para exibir os hóspedes
        String[] columnNames = {"Nome", "Email", "CPF", "Telefone", "Data de Nascimento"};
        tableModel = new DefaultTableModel(columnNames, 0);
        hospedesTable = new JTable(tableModel);
        hospedesTable.setFillsViewportHeight(true);

        // Estilo da tabela
        hospedesTable.setBackground(Color.WHITE);
        hospedesTable.setGridColor(new Color(200, 200, 200)); // Cor da grade
        hospedesTable.setRowHeight(25);
        hospedesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Adicionando a barra de rolagem
        JScrollPane scrollPane = new JScrollPane(hospedesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        searchByEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHospedePorEmail(emailField.getText());
            }
        });

        searchByCpfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHospedePorCpf(cpfField.getText());
            }
        });

        listAllHospedesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTodosHospedes();
            }
        });

        // Estilizando os botões
        styleButton(searchByEmailButton);
        styleButton(searchByCpfButton);
        styleButton(listAllHospedesButton);
    }

    // Método para estilizar botões
    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Espaçamento interno
        button.setFocusPainted(false); // Remove o foco
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor de mão
    }

    // Método para buscar hóspede por email
    private void buscarHospedePorEmail(String email) {
        try {
            String apiUrl = "http://localhost:8080/api/hospedes/email/" + email;
            String response = sendGetRequest(apiUrl);
            atualizarTabelaComResposta(response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar hóspede por email: " + ex.getMessage());
        }
    }

    // Método para buscar hóspede por CPF
    private void buscarHospedePorCpf(String cpf) {
        try {
            String apiUrl = "http://localhost:8080/api/hospedes/cpf/" + cpf;
            String response = sendGetRequest(apiUrl);
            atualizarTabelaComResposta(response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar hóspede por CPF: " + ex.getMessage());
        }
    }

    // Método para listar todos os hóspedes
    private void listarTodosHospedes() {
        try {
            String apiUrl = "http://localhost:8080/api/hospedes";
            String response = sendGetRequest(apiUrl);
            atualizarTabelaComResposta(response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar hóspedes: " + ex.getMessage());
        }
    }

    // Método genérico para enviar requisições GET
    private String sendGetRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return content.toString();
    }

    // Método para atualizar a tabela com a resposta JSON
    private void atualizarTabelaComResposta(String response) {
        try {
            tableModel.setRowCount(0);

            if (response.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject hospedeJson = jsonArray.getJSONObject(i);
                    adicionarHospedeNaTabela(hospedeJson);
                }
            } else {
                JSONObject hospedeJson = new JSONObject(response);
                adicionarHospedeNaTabela(hospedeJson);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao processar resposta: " + ex.getMessage());
        }
    }

    // Método para adicionar um hóspede na tabela
    private void adicionarHospedeNaTabela(JSONObject hospedeJson) {
        String nome = hospedeJson.getString("nome");
        String email = hospedeJson.getString("email");
        String cpf = hospedeJson.getString("cpf");
        String telefone = hospedeJson.optString("telefone", "Não informado");
        String dataNascimento = hospedeJson.optString("dataNascimento", "Não informado");

        Object[] rowData = {nome, email, cpf, telefone, dataNascimento};
        tableModel.addRow(rowData);
    }
}
