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

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                    String codigoQuarto = reservaJson.getJSONObject("codigoQuarto").getString("codigoQuarto");
                    String checkIn = reservaJson.getString("dataCheckIn");
                    String checkOut = reservaJson.getString("dataCheckOut");
                    int diarias = reservaJson.getInt("quantidadeDiarias");
                    String status = reservaJson.getString("status");

                    // Adicionar a linha na tabela
                    Object[] rowData = {codigoQuarto, checkIn, checkOut, diarias, status};
                    tableModel.addRow(rowData);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao listar reservas. Código de erro: " + con.getResponseCode());
            }
            con.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}
