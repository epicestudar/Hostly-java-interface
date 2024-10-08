package com.example.view.dashboards;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.example.view.panels.QuartosDisponiveisPanel;
import com.example.view.panels.SuasReservasPanel;

public class HospedeDashboard extends JFrame {
    public HospedeDashboard() {
        setTitle("Painel Administrativo");
        setSize(800, 600); // Tamanho ajustado para comportar as abas
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando o JTabbedPane para gerenciar abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Primeira aba: Gerenciamento de Hóspedes
        JPanel quartosDisponiveis = new QuartosDisponiveisPanel();
        tabbedPane.addTab("Quartos Disponíveis", quartosDisponiveis);

        JPanel reservasHospede = new SuasReservasPanel(getName());
        tabbedPane.addTab("Suas Reservas", reservasHospede);

        // Adicionando o JTabbedPane à janela
        add(tabbedPane);
        setVisible(true);
    }
}
