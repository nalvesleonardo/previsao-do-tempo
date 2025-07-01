package view;

import dal.DAO;

import javax.swing.*;
import java.awt.*;

public class FrmConfiguracoes extends JFrame {

    private JTextField txtApiUrl;
    private JButton btnSalvar;
    private DAO dao;

    public FrmConfiguracoes() {
        super("Configurações");
        setSize(500, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            dao = new DAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro de conexão: " + e.getMessage());
            return;
        }

        initUI();
        carregarConfiguracoes();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("URL Base da API:"), gbc);

        // Campo de Texto
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApiUrl = new JTextField(30);
        panel.add(txtApiUrl, gbc);

        // Botão Salvar
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        btnSalvar = new JButton("Salvar");
        panel.add(btnSalvar, gbc);

        add(panel);

        btnSalvar.addActionListener(e -> salvarConfiguracoes());
    }

    private void carregarConfiguracoes() {
        try {
            String apiUrl = dao.getConfiguracao("API_BASE_URL");
            txtApiUrl.setText(apiUrl);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage());
        }
    }

    private void salvarConfiguracoes() {
        try {
            dao.salvarConfiguracao("API_BASE_URL", txtApiUrl.getText());
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!");
            this.dispose(); // Fecha a janela após salvar
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configurações: " + e.getMessage());
        }
    }
}