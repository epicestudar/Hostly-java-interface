package com.example.view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.model.Reserva;
import com.fasterxml.jackson.core.type.TypeReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class GerarRelatorioPanel extends JPanel {

    public GerarRelatorioPanel() {
        setLayout(new GridBagLayout());

        JButton gerarRelatorioButton = new JButton("Gerar Relatório de Todas Reservas");
        gerarRelatorioButton.addActionListener(new GerarRelatorioAction());

        add(gerarRelatorioButton);
    }

    private class GerarRelatorioAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Chamar a API e obter todas as reservas
                List<Reserva> reservas = buscarTodasReservas();
                gerarPDF(reservas);
                JOptionPane.showMessageDialog(null, "Relatório PDF gerado com sucesso!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao gerar relatório.");
            }
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
        
            // Configuração do ObjectMapper para suportar LocalDate
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // Adiciona suporte a tipos Java 8, como LocalDate
        
            return mapper.readValue(json.toString(), new TypeReference<List<Reserva>>() {});
        }
        
        

        // Método para gerar o PDF com as reservas
        private void gerarPDF(List<Reserva> reservas) throws DocumentException, IOException {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("relatorio_reservas.pdf"));
        
            document.open();
            document.add(new Paragraph("Relatório de Reservas"));
        
            for (Reserva reserva : reservas) {
                document.add(new Paragraph("ID: " + reserva.getId()));
        
                // Verifica se o hóspede não é nulo antes de acessar seus atributos
                if (reserva.getHospede() != null) {
                    document.add(new Paragraph("Hóspede: " + reserva.getHospede().getNome()));
                    document.add(new Paragraph("CPF: " + reserva.getHospede().getCpf()));
                } else {
                    document.add(new Paragraph("Hóspede: Não informado"));
                }
        
                // Verifica se o quarto não é nulo antes de acessar seus atributos
                if (reserva.getQuarto() != null) {
                    document.add(new Paragraph("Quarto: " + reserva.getQuarto().getCodigoQuarto()));
                    document.add(new Paragraph("Valor: R$ " + reserva.getQuarto().getValorQuarto()));
                } else {
                    document.add(new Paragraph("Quarto: Não informado"));
                }
        
                document.add(new Paragraph("Data Check-in: " + reserva.getDataCheckIn()));
                document.add(new Paragraph("Data Check-out: " + reserva.getDataCheckOut()));
                document.add(new Paragraph("Status: " + reserva.getStatus()));
                document.add(new Paragraph("--------"));
            }
        
            document.close();
        }
        
    }
}