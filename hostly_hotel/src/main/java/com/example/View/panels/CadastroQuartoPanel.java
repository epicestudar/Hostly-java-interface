package com.example.view.panels;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.example.test.TipoQuarto;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CadastroQuartoPanel extends JPanel {
    private JTextField codigoField, capacidadeField, valorField;
    private JComboBox<TipoQuarto> tipoBox;
    private JButton cadastrarButton, listarButton, editarButton, deletarButton;
    private JTable quartoTable;
    private DefaultTableModel tableModel;

    public CadastroQuartoPanel() {
        setLayout(new BorderLayout());

        // Parte superior com inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
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
        inputPanel.add(valorField);

        cadastrarButton = new JButton("Cadastrar Quarto");
        inputPanel.add(cadastrarButton);

        add(inputPanel, BorderLayout.NORTH);

        // Tabela para exibir os quartos
        tableModel = new DefaultTableModel(new Object[]{"Código", "Tipo", "Capacidade", "Valor", "Status"}, 0);
        quartoTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(quartoTable);
        add(scrollPane, BorderLayout.CENTER);

        // Botões para ações de listar, editar e deletar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        listarButton = new JButton("Listar Quartos");
        editarButton = new JButton("Editar Quarto");
        deletarButton = new JButton("Deletar Quarto");

        buttonPanel.add(listarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(deletarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Ações dos botões
        cadastrarButton.addActionListener(e -> cadastrarQuarto());
        listarButton.addActionListener(e -> listarQuartos());
        editarButton.addActionListener(e -> editarQuarto());
        deletarButton.addActionListener(e -> deletarQuarto());
    }

    private void cadastrarQuarto() {
        String codigo = codigoField.getText();
        TipoQuarto tipo = (TipoQuarto) tipoBox.getSelectedItem();
        int capacidade = Integer.parseInt(capacidadeField.getText());
        double valor = Double.parseDouble(valorField.getText());

        // Fazer requisição à API para salvar o quarto
        // Implementação da chamada à API
    }

    private void listarQuartos() {
        // Limpar a tabela
        tableModel.setRowCount(0);

        // Fazer requisição à API para listar quartos
        // Adicionar resultados à tabela
        // Implementação da chamada à API
    }

    private void editarQuarto() {
        // Implementar lógica para editar quarto selecionado
    }

    private void deletarQuarto() {
        // Implementar lógica para deletar quarto selecionado
    }
}
