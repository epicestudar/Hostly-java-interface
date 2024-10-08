package com.example.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.example.test.TipoQuarto;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CadastroQuartoPanel extends JPanel {
    private JTextField codigoField, capacidadeField, valorField;
    private JComboBox<TipoQuarto> tipoBox;
    private JButton cadastrarButton, editarButton, deletarButton;
    private JTable quartoTable;
    private DefaultTableModel tableModel;

    public CadastroQuartoPanel() {
        setLayout(new BorderLayout());

        // Parte superior com inputs
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Código do Quarto:"));
        codigoField = new JTextField();
        inputPanel.add(codigoField);

        inputPanel.add(new JLabel("Tipo de Quarto:"));
        tipoBox = new JComboBox<>(TipoQuarto.values());
        inputPanel.add(tipoBox);

        inputPanel.add(new JLabel("Capacidade do Quarto:"));
        capacidadeField = new JTextField();
        inputPanel.add(capacidadeField);

        inputPanel.add(new JLabel("Valor do Quarto:"));
        valorField = new JTextField();
        valorField.setEditable(false); // Definir como não editável
        inputPanel.add(valorField);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[] { "ID", "Código", "Tipo", "Capacidade", "Valor", "Status" }, 0);
        quartoTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(quartoTable);
        add(scrollPane, BorderLayout.CENTER);

        // Botões para ações de editar e deletar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        cadastrarButton = new JButton("Cadastrar Quarto");
        editarButton = new JButton("Editar Quarto");
        deletarButton = new JButton("Deletar Quarto");

        buttonPanel.add(cadastrarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(deletarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Ações dos botões
        cadastrarButton.addActionListener(e -> cadastrarQuarto());
        editarButton.addActionListener(e -> editarQuarto());
        deletarButton.addActionListener(e -> deletarQuarto());

        // Adicionar um ActionListener para atualizar o valor automaticamente
        tipoBox.addActionListener(e -> atualizarValorQuarto());

        // Chama listarQuartos para carregar os quartos ao inicializar o painel
        listarQuartos();
    }

    private void atualizarValorQuarto() {
        TipoQuarto tipoSelecionado = (TipoQuarto) tipoBox.getSelectedItem();
        if (tipoSelecionado != null) {
            valorField.setText(String.valueOf(tipoSelecionado.getValorDiaria())); // Atualiza o campo de valor
        }
    }

    private void cadastrarQuarto() {
        String codigo = codigoField.getText();
        TipoQuarto tipo = (TipoQuarto) tipoBox.getSelectedItem();
        int capacidade = Integer.parseInt(capacidadeField.getText());
        double valor = tipo.getValorDiaria(); // O valor é obtido com base no tipo do quarto
    
        // Fazer requisição à API para salvar o quarto
        try {
            String apiUrl = "http://localhost:8080/api/quartos";
            JSONObject quartoJson = new JSONObject();
            quartoJson.put("codigoQuarto", codigo);
            quartoJson.put("tipoQuarto", tipo.name());
            quartoJson.put("capacidadeQuarto", capacidade);
            quartoJson.put("valorQuarto", valor); // Valor calculado com base no tipo
    
            HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(quartoJson.toString().getBytes());
    
            if (con.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Quarto cadastrado com sucesso!");
                listarQuartos(); // Atualiza a tabela com os quartos
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar o quarto.");
            }
    
            con.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    

    // Método para listar quartos
    private void listarQuartos() {
        tableModel.setRowCount(0); // Limpar tabela antes de listar

        try {
            String apiUrl = "http://localhost:8080/api/quartos";
            HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray quartosArray = new JSONArray(response.toString());
                for (int i = 0; i < quartosArray.length(); i++) {
                    JSONObject quartoJson = quartosArray.getJSONObject(i);
                    adicionarQuartoNaTabela(quartoJson);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao listar quartos. Código de erro: " + con.getResponseCode());
            }
            con.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void editarQuarto() {
        int selectedRow = quartoTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);

            String codigo = codigoField.getText();
            TipoQuarto tipo = (TipoQuarto) tipoBox.getSelectedItem();
            int capacidade = Integer.parseInt(capacidadeField.getText());
            double valor = tipo.getValorDiaria(); // Valor calculado com base no tipo

            // Fazer requisição à API para atualizar o quarto
            try {
                String apiUrl = "http://localhost:8080/api/quartos/" + id;
                JSONObject quartoJson = new JSONObject();
                quartoJson.put("codigoQuarto", codigo);
                quartoJson.put("tipoQuarto", tipo.name());
                quartoJson.put("capacidadeQuarto", capacidade);
                quartoJson.put("valorQuarto", valor);

                HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.getOutputStream().write(quartoJson.toString().getBytes());

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(this, "Quarto atualizado com sucesso!");
                    listarQuartos(); // Atualiza a tabela com os quartos
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar o quarto. Código de erro: " + con.getResponseCode());
                }

                con.disconnect();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para editar.");
        }
    }

    private void deletarQuarto() {
        int selectedRow = quartoTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0); // Agora pegando o ID

            // Fazer requisição à API para deletar o quarto
            try {
                String apiUrl = "http://localhost:8080/api/quartos/" + id;
                HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
                con.setRequestMethod("DELETE");

                if (con.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) { // 204
                    JOptionPane.showMessageDialog(this, "Quarto deletado com sucesso!");
                    listarQuartos(); // Atualiza a tabela com os quartos
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao deletar o quarto. Código de erro: " + con.getResponseCode());
                }

                con.disconnect();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para deletar.");
        }
    }

    private void adicionarQuartoNaTabela(JSONObject quartoJson) {
        String id = quartoJson.getString("id");
        String codigo = quartoJson.getString("codigoQuarto");
        String tipo = quartoJson.getString("tipoQuarto");
        int capacidade = quartoJson.getInt("capacidadeQuarto");
        double valor = quartoJson.getDouble("valorQuarto");
        String status = quartoJson.getString("status");

        Object[] rowData = { id, codigo, tipo, capacidade, valor, status };
        tableModel.addRow(rowData);
    }

}
