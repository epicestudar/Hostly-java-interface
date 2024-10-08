package com.example.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    // Método para abrir o painel de pagamento
private void abrirPainelDePagamento(String codigoQuarto, String tipoQuarto, int diarias, double valorTotal) {
    JDialog pagamentoDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Pagamento", true);
    pagamentoDialog.setSize(300, 300);
    pagamentoDialog.setLayout(new BorderLayout());

    // Painel com inputs para pagamento
    JPanel painelCentral = new JPanel(new GridLayout(4, 2));

    JLabel labelMetodoPagamento = new JLabel("Método de Pagamento:");
    String[] opcoesPagamento = {"CREDITO", "DEBITO", "PIX"};
    JComboBox<String> campoMetodoPagamento = new JComboBox<>(opcoesPagamento);

    JLabel labelValorTotal = new JLabel("Valor Total:");
    JTextField campoValorTotal = new JTextField(String.valueOf(valorTotal));
    campoValorTotal.setEditable(false);

    painelCentral.add(labelMetodoPagamento);
    painelCentral.add(campoMetodoPagamento);
    painelCentral.add(labelValorTotal);
    painelCentral.add(campoValorTotal);

    // Botão para confirmar o pagamento e realizar a reserva
    JButton confirmarPagamentoButton = new JButton("Confirmar Pagamento");
    confirmarPagamentoButton.addActionListener(e -> {
        // Obter o método de pagamento selecionado
        String metodoPagamento = (String) campoMetodoPagamento.getSelectedItem();

        // Realizar a reserva após o pagamento
        realizarReserva(codigoQuarto, diarias, valorTotal, metodoPagamento);

        // Fechar o painel de pagamento
        pagamentoDialog.dispose();
    });

    // Adicionando os componentes ao diálogo de pagamento
    pagamentoDialog.add(painelCentral, BorderLayout.CENTER);
    pagamentoDialog.add(confirmarPagamentoButton, BorderLayout.SOUTH);

    pagamentoDialog.setVisible(true);
}


// Método para realizar a reserva e pagamento
private void realizarReserva(String codigoQuarto, int diarias, double valorTotal, String metodoPagamento) {
    try {
        // URL da API de reservas
        String apiUrlReserva = "http://localhost:8080/api/reservas";

        // Criando a conexão HTTP para a reserva
        HttpURLConnection conReserva = (HttpURLConnection) new URL(apiUrlReserva).openConnection();
        conReserva.setRequestMethod("POST");
        conReserva.setRequestProperty("Content-Type", "application/json");
        conReserva.setDoOutput(true);

        // Criando o JSON da reserva
        JSONObject reservaJson = new JSONObject();
        reservaJson.put("codigoQuarto", codigoQuarto);
        reservaJson.put("quantidadeDiarias", diarias);
        reservaJson.put("valorTotal", valorTotal);
        reservaJson.put("status", "CONFIRMADO"); // Definindo como CONFIRMADO após o pagamento

        // Enviando a requisição de reserva
        try (OutputStream os = conReserva.getOutputStream()) {
            byte[] input = reservaJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Verificando a resposta da API de reservas
        if (conReserva.getResponseCode() == HttpURLConnection.HTTP_OK) {
            JOptionPane.showMessageDialog(this, "Reserva realizada com sucesso!");

            // Agora, enviar os dados do pagamento
            realizarPagamento(metodoPagamento, valorTotal);

            // Atualizar o status do quarto no banco de dados
            atualizarStatusQuarto(codigoQuarto, "RESERVADO");

            // Atualizar status do quarto na tabela para "RESERVADO" e removê-lo da lista
            removerQuartoDaTabela(tableQuartos.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao realizar reserva. Código de erro: " + conReserva.getResponseCode());
        }

        conReserva.disconnect();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar a reserva: " + e.getMessage());
    }
}

// Método para realizar o pagamento
private void realizarPagamento(String metodoPagamento, double valorTotal) {
    try {
        // URL da API de pagamentos
        String apiUrlPagamento = "http://localhost:8080/api/pagamentos";

        // Criando a conexão HTTP para o pagamento
        HttpURLConnection conPagamento = (HttpURLConnection) new URL(apiUrlPagamento).openConnection();
        conPagamento.setRequestMethod("POST");
        conPagamento.setRequestProperty("Content-Type", "application/json");
        conPagamento.setDoOutput(true);

        // Criando o JSON do pagamento
        JSONObject pagamentoJson = new JSONObject();
        pagamentoJson.put("metodoPagamento", metodoPagamento);
        pagamentoJson.put("valor", valorTotal);

        // Enviando a requisição de pagamento
        try (OutputStream os = conPagamento.getOutputStream()) {
            byte[] input = pagamentoJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Verificando a resposta da API de pagamentos
        if (conPagamento.getResponseCode() == HttpURLConnection.HTTP_OK) {
            JOptionPane.showMessageDialog(this, "Pagamento realizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao processar pagamento. Código de erro: " + conPagamento.getResponseCode());
        }

        conPagamento.disconnect();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar o pagamento: " + e.getMessage());
    }
}



// Método para atualizar o status do quarto no banco de dados
private void atualizarStatusQuarto(String codigoQuarto, String novoStatus) {
    try {
        // URL da API para buscar os dados do quarto pelo código
        String apiUrlGet = "http://localhost:8080/api/quartos/codigo/" + codigoQuarto;

        // Criar conexão para GET e buscar dados do quarto atual
        HttpURLConnection conGet = (HttpURLConnection) new URL(apiUrlGet).openConnection();
        conGet.setRequestMethod("GET");

        if (conGet.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conGet.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parseando o JSON do quarto atual
            JSONObject quartoJson = new JSONObject(response.toString());

            // Atualizando apenas o status do quarto
            quartoJson.put("status", novoStatus);

            // Criando a conexão HTTP para o PUT (pelo código do quarto)
            String apiUrlPut = "http://localhost:8080/api/quartos/codigo/" + codigoQuarto;
            HttpURLConnection conPut = (HttpURLConnection) new URL(apiUrlPut).openConnection();
            conPut.setRequestMethod("PUT");
            conPut.setRequestProperty("Content-Type", "application/json");
            conPut.setDoOutput(true);

            // Enviando o JSON atualizado para a API
            try (OutputStream os = conPut.getOutputStream()) {
                byte[] input = quartoJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Verificando a resposta da API para o PUT
            if (conPut.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Status do quarto atualizado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar status do quarto. Código de erro: " + conPut.getResponseCode());
            }

            conPut.disconnect();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao buscar quarto. Código de erro: " + conGet.getResponseCode());
        }
        conGet.disconnect();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar status do quarto: " + e.getMessage());
    }
}



// Método para remover o quarto da tabela após a reserva
private void removerQuartoDaTabela(int selectedRow) {
    tableModel.removeRow(selectedRow); // Remove a linha selecionada da tabela
}

// Método para tratar a ação de fazer a reserva
private void fazerReserva() {
    int selectedRow = tableQuartos.getSelectedRow();
    if (selectedRow != -1) {
        String codigoQuarto = (String) tableModel.getValueAt(selectedRow, 0); // Pegar o código do quarto da linha selecionada
        String tipoQuarto = (String) tableModel.getValueAt(selectedRow, 1);
        double valorQuarto = (Double) tableModel.getValueAt(selectedRow, 4); // Valor do quarto
        JOptionPane.showMessageDialog(this, "Quarto " + codigoQuarto + " (" + tipoQuarto + ") selecionado para reserva.");

        // Abrir um diálogo para solicitar a quantidade de diárias
        String diariasStr = JOptionPane.showInputDialog(this, "Digite a quantidade de diárias:");
        if (diariasStr != null && !diariasStr.isEmpty()) {
            try {
                int diarias = Integer.parseInt(diariasStr);
                double valorTotal = diarias * valorQuarto;

                // Abrir o painel de pagamento com o valor calculado
                abrirPainelDePagamento(codigoQuarto, tipoQuarto, diarias, valorTotal);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade de diárias inválida.");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selecione um quarto para fazer a reserva.");
    }
}



}
