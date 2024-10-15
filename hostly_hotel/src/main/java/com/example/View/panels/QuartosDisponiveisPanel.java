package com.example.view.panels;

import java.time.LocalDate;
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
    private String cpfHospede; // Novo campo para armazenar o CPF do hóspede

    // Modifique o construtor para aceitar o CPF do hóspede
    public QuartosDisponiveisPanel(String cpfHospede) {
        this.cpfHospede = cpfHospede; // Atribui o CPF recebido ao campo da classe
        setLayout(new BorderLayout());

        // Criando a tabela de quartos
        tableModel = new DefaultTableModel(new Object[] { "Código", "Tipo", "Status", "Capacidade", "Valor" }, 0);
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

        Object[] rowData = { codigo, tipo, status, capacidade, valor };
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
        String[] opcoesPagamento = { "CREDITO", "DEBITO", "PIX" };
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

    private JSONObject buscarQuartoPorCodigo(String codigoQuarto) {
        try {
            String apiUrl = "http://localhost:8080/api/quartos/codigo/" + codigoQuarto;
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
                return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar quarto: " + e.getMessage());
        }
        return null;
    }

    private JSONObject buscarHospedePorCpf(String cpf) {
        try {
            String apiUrl = "http://localhost:8080/api/hospedes/cpf/" + cpf;
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
                return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar hóspede: " + e.getMessage());
        }
        return null;
    }

    private void realizarReserva(String codigoQuarto, int diarias, double valorTotal, String metodoPagamento) {
        try {
            // Buscar informações completas do quarto pela API
            JSONObject quartoJson = buscarQuartoPorCodigo(codigoQuarto);
            if (quartoJson == null) {
                JOptionPane.showMessageDialog(this, "Erro: Quarto não encontrado.");
                return;
            }

            // Buscar informações completas do hóspede pela API
            JSONObject hospedeJson = buscarHospedePorCpf(cpfHospede);
            if (hospedeJson == null) {
                JOptionPane.showMessageDialog(this, "Erro: Hóspede não encontrado.");
                return;
            }

            // URL da API de reservas
            String apiUrlReserva = "http://localhost:8080/api/reservas";
            HttpURLConnection conReserva = (HttpURLConnection) new URL(apiUrlReserva).openConnection();
            conReserva.setRequestMethod("POST");
            conReserva.setRequestProperty("Content-Type", "application/json");
            conReserva.setDoOutput(true);

            // Montando o JSON da reserva completo
            JSONObject reservaJson = new JSONObject();
            reservaJson.put("quarto", quartoJson);
            reservaJson.put("hospede", hospedeJson);
            reservaJson.put("quantidadeDiarias", diarias);
            reservaJson.put("status", "CONFIRMADO");
            reservaJson.put("dataCheckIn", LocalDate.now().toString()); // Exemplo de data atual

            // Enviar a requisição
            try (OutputStream os = conReserva.getOutputStream()) {
                byte[] input = reservaJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

             // Verificando a resposta da API de reservas
        if (conReserva.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Lendo a resposta da API para capturar o reservaId
            BufferedReader reader = new BufferedReader(new InputStreamReader(conReserva.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parseando a resposta JSON para obter o id da reserva
            JSONObject reservaResponse = new JSONObject(response.toString());
            String reservaId = reservaResponse.getString("id");

            JOptionPane.showMessageDialog(this, "Reserva realizada com sucesso!");

            // Agora, enviar os dados do pagamento usando o reservaId
            realizarPagamento(metodoPagamento, valorTotal, reservaId);

            // Atualizar o status do quarto no banco de dados
            atualizarStatusQuarto(codigoQuarto, "RESERVADO");

            // Atualizar status do quarto na tabela para "RESERVADO" e removê-lo da lista
            removerQuartoDaTabela(tableQuartos.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar reserva. Código de erro: " + conReserva.getResponseCode());
        }

        conReserva.disconnect();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar a reserva: " + e.getMessage());
    }
    }

    // Método para realizar o pagamento
    private void realizarPagamento(String metodoPagamento, double valorTotal, String reservaId) {
        try {
            // URL da API de pagamentos
            String apiUrlPagamento = "http://localhost:8080/api/pagamentos";

            // Criando a conexão HTTP para o pagamento
            HttpURLConnection conPagamento = (HttpURLConnection) new URL(apiUrlPagamento).openConnection();
            conPagamento.setRequestMethod("POST");
            conPagamento.setRequestProperty("Content-Type", "application/json");
            conPagamento.setDoOutput(true);

            // Criando o JSON para a requisição de pagamento
            JSONObject pagamentoJson = new JSONObject();
            pagamentoJson.put("reserva", new JSONObject().put("id", reservaId)); // Reserva associada
            pagamentoJson.put("hospede", new JSONObject().put("cpf", cpfHospede)); // CPF do hóspede
            pagamentoJson.put("dataPagamento", LocalDate.now().toString()); // Data atual do pagamento
            pagamentoJson.put("valorPago", valorTotal); // Valor pago
            pagamentoJson.put("metodoPagamento", metodoPagamento); // Método de pagamento escolhido

            // Enviando a requisição de pagamento;
            try (OutputStream os = conPagamento.getOutputStream()) {
                byte[] input = pagamentoJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Verificando a resposta da API de pagamentos
            if (conPagamento.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Pagamento realizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao processar pagamento. Código de erro: " + conPagamento.getResponseCode());
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
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar status do quarto. Código de erro: " + conPut.getResponseCode());
                }

                conPut.disconnect();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao buscar quarto. Código de erro: " + conGet.getResponseCode());
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
            String codigoQuarto = (String) tableModel.getValueAt(selectedRow, 0); // Pegar o código do quarto da linha
                                                                                  // selecionada
            String tipoQuarto = (String) tableModel.getValueAt(selectedRow, 1);
            double valorQuarto = (Double) tableModel.getValueAt(selectedRow, 4); // Valor do quarto
            JOptionPane.showMessageDialog(this,
                    "Quarto " + codigoQuarto + " (" + tipoQuarto + ") selecionado para reserva.");

            // Abrir um diálogo para solicitar a quantidade de diárias
            String diariasStr = JOptionPane.showInputDialog(this, "Quantas diárias?");
            int diarias = Integer.parseInt(diariasStr);

            // Calcular o valor total com base nas diárias
            double valorTotal = diarias * valorQuarto;

            // Abrir o painel de pagamento
            abrirPainelDePagamento(codigoQuarto, tipoQuarto, diarias, valorTotal);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um quarto para fazer a reserva.");
        }
    }
}