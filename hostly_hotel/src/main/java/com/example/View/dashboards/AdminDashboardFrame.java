package com.example.view.dashboards;

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

public class AdminDashboardFrame extends JFrame {

    private JTextField emailField;
    private JTextField cpfField;
    private JTable hospedesTable;
    private DefaultTableModel tableModel;
    private JButton searchByEmailButton;
    private JButton searchByCpfButton;
    private JButton listAllHospedesButton;

    public AdminDashboardFrame() {
        setTitle("Painel Administrativo");
        setSize(800, 600); // Tamanho aumentado para comportar a tabela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando o JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Primeira aba: Procurar Hóspede
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10)); // Define layout de borda para o painel principal
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens ao redor

        // Painel superior para entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // Layout em grade para labels e campos

        // Adicionando componentes de input ao painel superior
        inputPanel.add(new JLabel("Procurar por Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);
        searchByEmailButton = new JButton("Buscar por Email");
        inputPanel.add(searchByEmailButton);

        inputPanel.add(new JLabel("Procurar por CPF:"));
        cpfField = new JTextField();
        inputPanel.add(cpfField);
        searchByCpfButton = new JButton("Buscar por CPF");
        inputPanel.add(searchByCpfButton);

        inputPanel.add(new JLabel()); // Espaço vazio para alinhamento
        listAllHospedesButton = new JButton("Listar Todos os Hóspedes");
        inputPanel.add(listAllHospedesButton);

        searchPanel.add(inputPanel, BorderLayout.NORTH); // Adiciona o painel de entrada ao topo

        // Configurando a tabela para exibir os hóspedes
        String[] columnNames = {"Nome", "Email", "CPF", "Telefone", "Data de Nascimento"};
        tableModel = new DefaultTableModel(columnNames, 0); // Criando o modelo da tabela
        hospedesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(hospedesTable);
        hospedesTable.setFillsViewportHeight(true); // Preencher toda a área de visualização da tabela
        searchPanel.add(scrollPane, BorderLayout.CENTER); // Adiciona a tabela ao centro

        // Adiciona a aba ao JTabbedPane
        tabbedPane.addTab("Buscar Hóspede", searchPanel);

        // Adiciona o JTabbedPane à janela
        add(tabbedPane);
        setVisible(true);

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
        atualizarTabelaComResposta(response); // Atualiza a JTable com os dados recebidos
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
            // Limpa a tabela antes de atualizar
            tableModel.setRowCount(0);

            if (response.startsWith("[")) { // Caso seja uma lista de hóspedes
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject hospedeJson = jsonArray.getJSONObject(i);
                    adicionarHospedeNaTabela(hospedeJson);
                }
            } else { // Caso seja um único hóspede
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
        tableModel.addRow(rowData); // Adiciona a linha à tabela
    }
}