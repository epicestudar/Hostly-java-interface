package com.example.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class SuasReservasPanel extends JPanel {

    private JTable tableReservas;
    private DefaultTableModel tableModel;

    public SuasReservasPanel(String cpfHospede) {
        setLayout(new BorderLayout());

        // Criar a tabela de reservas
        tableModel = new DefaultTableModel(new Object[]{"Código Quarto", "Check-in", "Check-out", "Diárias", "Status"}, 0);
        tableReservas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableReservas);
        add(scrollPane, BorderLayout.CENTER);

        // Botão para atualizar as reservas
        JButton atualizarButton = new JButton("Atualizar Reservas");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarReservasHospede(cpfHospede);
            }
        });
        add(atualizarButton, BorderLayout.SOUTH);

        // Carregar as reservas ao iniciar o painel
        listarReservasHospede(cpfHospede);
    }

    // Método para listar as reservas do hóspede
    private void listarReservasHospede(String cpfHospede) {
        try {
            String apiUrl = "http://localhost:8080/api/reservas/hospede/" + cpfHospede; // URL da API
            HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parseando a resposta JSON e atualizando a tabela
                JSONArray reservasArray = new JSONArray(response.toString());
                tableModel.setRowCount(0); // Limpar a tabela antes de adicionar novas linhas

                for (int i = 0; i < reservasArray.length(); i++) {
                    JSONObject reservaJson = reservasArray.getJSONObject(i);

                    // Verifica se os campos existem e são não nulos
                    JSONObject quartoJson = reservaJson.getJSONObject("quarto");

                    String codigoQuarto = quartoJson.optString("codigoQuarto", "Não informado");
                    String checkIn = reservaJson.optString("dataCheckIn", "Não informado");
                    String checkOut = reservaJson.optString("dataCheckOut", "Não informado");
                    int diarias = reservaJson.optInt("quantidadeDiarias", 0); // Se o campo for null, retorna 0
                    String status = reservaJson.optString("status", "Não informado");

                    // Adicionar a linha na tabela
                    Object[] rowData = {codigoQuarto, checkIn, checkOut, diarias, status};
                    tableModel.addRow(rowData);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao listar reservas. Código de erro: " + responseCode);
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao listar reservas: " + e.getMessage());
        }
    }
}
