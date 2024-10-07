package com.example.view.tests;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.example.controller.ApiController;
import com.example.model.Hospede;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestView extends JFrame{
    private JTextField cpfField;
    private JTextArea resultadoArea;
    private ApiController apiController;

    public TestView() {
        // Inicializar o controlador
        apiController = new ApiController();

        // Criar elementos da interface
        cpfField = new JTextField(15);
        JButton buscarButton = new JButton("Buscar Hóspede");
        resultadoArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);

        // Adicionar ação ao botão
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfField.getText();
                try {
                    Hospede hospede = apiController.buscarHospedePorCpf(cpf);
                    resultadoArea.setText("Nome: " + hospede.getNome() + "\nCPF: " + hospede.getCpf());
                } catch (Exception ex) {
                    resultadoArea.setText("Erro ao buscar hóspede: " + ex.getMessage());
                }
            }
        });

        // Organizar os elementos no JFrame
        JPanel panel = new JPanel();
        panel.add(new JLabel("CPF:"));
        panel.add(cpfField);
        panel.add(buscarButton);
        panel.add(new JScrollPane(resultadoArea));

        add(panel);

        // Configurações do JFrame
        setTitle("Busca de Hóspedes");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    }