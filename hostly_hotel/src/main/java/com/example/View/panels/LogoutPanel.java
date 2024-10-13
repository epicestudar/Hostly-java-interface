package com.example.view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.example.view.FrameInicial;

public class LogoutPanel extends JPanel {

    public LogoutPanel() {
        setLayout(new GridBagLayout()); // Centraliza o botão no painel

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new LogoutAction());

        add(logoutButton);
    }

    // Classe interna para definir a ação do botão de Logout
    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fecha o painel administrativo e retorna à tela inicial
            SwingUtilities.getWindowAncestor(LogoutPanel.this).dispose();
            new FrameInicial().setVisible(true);
        }
    }
}
