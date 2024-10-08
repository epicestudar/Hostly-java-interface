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

public class QuartosDisponiveisPanel extends JPanel {

    private JTable tableQuartos;
    private DefaultTableModel tableModel;
    private JButton reservarButton;

    public QuartosDisponiveisPanel() {
        setLayout(new BorderLayout());

        // Criando a tabela de quartos
        tableModel = new DefaultTableModel(new Object[]{"Código", "Tipo", "Status", "Capacidade", "Valor"}, 0);
        tableQuartos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableQuartos);
        add(scrollPane, BorderLayout.CENTER);

        // Botão para fazer a reserva
        reservarButton = new JButton("Fazer Reserva");
        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fazerReserva();
            }
        });
        add(reservarButton, BorderLayout.SOUTH);

        // Listar os quartos disponíveis ao carregar o painel
        listarQuartosDisponiveis();
    }

    // Método para listar quartos disponíveis (status == DISPONIVEL)
    private void listarQuartosDisponiveis() {
        try {
            String apiUrl = "http://localhost:8080/api/quartos"; // Endpoint para buscar os quartos
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

                // Parseando a resposta JSON e adicionando apenas os quartos disponíveis
                JSONArray quartosArray = new JSONArray(response.toString());
                for (int i = 0; i < quartosArray.length(); i++) {
                    JSONObject quartoJson = quartosArray.getJSONObject(i);
                    if (quartoJson.getString("status").equals("DISPONIVEL")) {
                        adicionarQuartoNaTabela(quartoJson);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao listar quartos. Código de erro: " + con.getResponseCode());
            }
            con.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    // Método para adicionar os quartos disponíveis na tabela
    private void adicionarQuartoNaTabela(JSONObject quartoJson) {
        String codigo = quartoJson.getString("codigoQuarto");
        String tipo = quartoJson.getString("tipoQuarto");
        String status = quartoJson.getString("status");
        int capacidade = quartoJson.getInt("capacidadeQuarto");
        double valor = quartoJson.getDouble("valorQuarto");

        Object[] rowData = {codigo, tipo, status, capacidade, valor};
        tableModel.addRow(rowData);
    }

    // Método para tratar a ação de fazer a reserva
    private void fazerReserva() {
        int selectedRow = tableQuartos.getSelectedRow();
        if (selectedRow != -1) {
            String codigoQuarto = (String) tableModel.getValueAt(selectedRow, 0); // Pegar o código do quarto da linha selecionada
            String tipoQuarto = (String) tableModel.getValueAt(selectedRow, 1);
            JOptionPane.showMessageDialog(this, "Quarto " + codigoQuarto + " (" + tipoQuarto + ") selecionado para reserva.");
            
            // Aqui, você pode abrir um novo painel ou janela para solicitar os dados do hóspede
            // e fazer a requisição para a API para criar a reserva.
            // Você pode integrar com o model de reserva e os inputs necessários para concluir a reserva.
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para fazer a reserva.");
        }
    }
}