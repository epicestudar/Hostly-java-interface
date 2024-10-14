package com.example.view.panels;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.model.Reserva;

public class GerarRelatorioPanel extends JPanel {

    public GerarRelatorioPanel() {
        setLayout(new BorderLayout());

        // Busca todas as reservas e cria a tabela
        try {
            List<Reserva> reservas = buscarTodasReservas();
            JTable tabela = criarTabelaComReservas(reservas);
            add(new JScrollPane(tabela), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar reservas.");
        }
    }

    private JTable criarTabelaComReservas(List<Reserva> reservas) {
        String[] colunas = {"ID", "Hóspede", "Quarto", "Ação"};
        Object[][] dados = new Object[reservas.size()][4];

        // Popula a tabela com as reservas
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            dados[i][0] = reserva.getId();
            dados[i][1] = reserva.getHospede() != null ? reserva.getHospede().getNome() : "Não informado";
            dados[i][2] = reserva.getQuarto() != null ? reserva.getQuarto().getCodigoQuarto() : "Não informado";
            dados[i][3] = "Gerar Relatório";  // Texto para o botão
        }

        DefaultTableModel model = new DefaultTableModel(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna de ação (botão) será editável
                return column == 3;
            }
        };

        JTable tabela = new JTable(model);

        // Configura o renderizador e editor para o botão
        tabela.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabela.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        return tabela;
    }

    // Método para buscar todas as reservas da API
    private List<Reserva> buscarTodasReservas() throws IOException {
        URL url = new URL("http://localhost:8080/api/reservas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder json = new StringBuilder();
        while (scanner.hasNext()) {
            json.append(scanner.nextLine());
        }
        scanner.close();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.readValue(json.toString(), new TypeReference<List<Reserva>>() {});
    }

    // Método para gerar um PDF para uma reserva específica
    private void gerarPDF(Reserva reserva) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("relatorio_reserva_" + reserva.getId() + ".pdf"));

        document.open();
        document.add(new Paragraph("Relatório de Reserva"));
        document.add(new Paragraph("ID: " + reserva.getId()));

        if (reserva.getHospede() != null) {
            document.add(new Paragraph("Hóspede: " + reserva.getHospede().getNome()));
            document.add(new Paragraph("CPF: " + reserva.getHospede().getCpf()));
        } else {
            document.add(new Paragraph("Hóspede: Não informado"));
        }

        if (reserva.getQuarto() != null) {
            document.add(new Paragraph("Quarto: " + reserva.getQuarto().getCodigoQuarto()));
            document.add(new Paragraph("Valor: R$ " + reserva.getQuarto().getValorQuarto()));
        } else {
            document.add(new Paragraph("Quarto: Não informado"));
        }

        document.add(new Paragraph("Data Check-in: " + reserva.getDataCheckIn()));
        document.add(new Paragraph("Data Check-out: " + reserva.getDataCheckOut()));
        document.add(new Paragraph("Status: " + reserva.getStatus()));

        document.close();
    }

    // Renderizador para exibir o botão corretamente
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor para permitir a interação com o botão
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private Reserva reserva;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;

            // Busca a reserva correspondente à linha do botão
            String reservaId = table.getValueAt(row, 0).toString();
            try {
                reserva = buscarReservaPorId(reservaId);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                try {
                    gerarPDF(reserva);
                    JOptionPane.showMessageDialog(button, "Relatório gerado com sucesso!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(button, "Erro ao gerar relatório.");
                }
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        private Reserva buscarReservaPorId(String id) throws IOException {
            URL url = new URL("http://localhost:8080/api/reservas/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String json = scanner.useDelimiter("\\A").next();
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.readValue(json, Reserva.class);
        }
    }
}
