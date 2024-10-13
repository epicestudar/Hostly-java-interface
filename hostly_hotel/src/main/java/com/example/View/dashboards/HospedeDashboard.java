package com.example.view.dashboards;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.example.view.panels.LogoutPanel;
import com.example.view.panels.QuartosDisponiveisPanel;
import com.example.view.panels.SuasReservasPanel;

public class HospedeDashboard extends JFrame {
    private String cpfHospede; // Armazenar o CPF do hóspede logado

    public HospedeDashboard(String cpfHospede) {
        this.cpfHospede = cpfHospede; // Armazena o CPF do hóspede

        setTitle("Painel do Hóspede");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando o JTabbedPane para gerenciar abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Primeira aba: Quartos Disponíveis
        JPanel quartosDisponiveis = new QuartosDisponiveisPanel(cpfHospede);
        tabbedPane.addTab("Quartos Disponíveis", quartosDisponiveis);

        // Segunda aba: Suas Reservas, passando o CPF para o painel
        JPanel reservasHospede = new SuasReservasPanel(cpfHospede);
        tabbedPane.addTab("Suas Reservas", reservasHospede);

         JPanel logoutPanel = new LogoutPanel();
        tabbedPane.addTab("Sair da Conta", logoutPanel);

        // Adicionando o JTabbedPane à janela
        add(tabbedPane);
        setVisible(true);
    }
}
