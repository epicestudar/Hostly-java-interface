package com.example.view.dashboards;

import com.example.view.panels.CadastroQuartoPanel;
import com.example.view.panels.GerarRelatorioPanel;
import com.example.view.panels.HospedePanel;
import com.example.view.panels.LogoutPanel;

import javax.swing.*;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame() {
        setTitle("Painel Administrativo");
        setSize(800, 600); // Tamanho ajustado para comportar as abas
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando o JTabbedPane para gerenciar abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Primeira aba: Gerenciamento de Hóspedes
        JPanel hospedePanel = new HospedePanel();
        tabbedPane.addTab("Gerenciar Hóspedes", hospedePanel);

        // Segunda aba: Cadastro de Quartos
        JPanel cadastroQuartoPanel = new CadastroQuartoPanel();
        tabbedPane.addTab("Gerenciar Quartos", cadastroQuartoPanel);

        // Terceira aba: Emissões Fiscais
        JPanel gerarRelatorioPanel = new GerarRelatorioPanel();
        tabbedPane.addTab("Relatório de Reservas", gerarRelatorioPanel);

        JPanel logoutPanel = new LogoutPanel();
        tabbedPane.addTab("Sair da Conta", logoutPanel);

        // Adicionando o JTabbedPane à janela
        add(tabbedPane);
        setVisible(true);
    }
}
