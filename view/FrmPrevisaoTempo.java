package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrmPrevisaoTempo extends JFrame {

    // Componentes da UI (apenas para visualização, sem funcionalidade por enquanto)
    private JComboBox<String> cmbCidade;
    private JButton btnAtualizar;
    private JLabel lblLocAtual;
    private JLabel lblHoraAtual;
    private JLabel lblTempoAtual;
    private JLabel lblDescAtual;
    private JPanel pnlTopo;
    private JPanel pnlMeio;
    private JLabel lblCidade;

    public FrmPrevisaoTempo() {
        super("Previsão do Tempo"); // Título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o que acontece ao fechar a janela
        setSize(800, 650); // Tamanho inicial da janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        initUI(); // Chama o método para construir a interface
    }

    private void initUI() {
        // Define o layout principal da janela como BorderLayout
        setLayout(new BorderLayout(10, 10)); // 10px de espaçamento horizontal e vertical

        // --- Painel de Controle (Topo da Tela) ---
        pnlTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout de fluxo centralizado
        pnlTopo.setBorder(new EmptyBorder(10, 10, 5, 10)); // Espaçamento interno
        pnlTopo.setBackground(new Color(44, 62, 80)); // Cor de fundo escura

        cmbCidade = new JComboBox<>();
        cmbCidade.setFont(new Font("Arial", Font.PLAIN, 14));
        pnlTopo.add(cmbCidade);

        btnAtualizar = new JButton("Atualizar");
        pnlTopo.add(btnAtualizar);


        // --- Painel Central (Onde os Widgets Vão Ficar) ---
        pnlMeio = new JPanel(new GridBagLayout()); // Usa GridBagLayout para flexibilidade

        // Configurações base para GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem entre os widgets
        gbc.fill = GridBagConstraints.BOTH; // Componentes preenchem o espaço disponível
        gbc.weightx = 1.0; // Distribuem o espaço horizontalmente
        gbc.weighty = 1.0; // Distribuem o espaço verticalmente

        // --- 1. Widget: Dados do Tempo Atual (Agora) ---
        JPanel pnlTempoAgora = criarWidget("Tempo Agora");
        lblCidade = new JLabel("Local: [Cidade, País]");
        lblHoraAtual = new JLabel("Data/Hora: [DD/MM/YYYY HH:MM]");
        lblTempoAtual = new JLabel("Temperatura: [XX.X°C]");
        lblDescAtual = new JLabel("Descrição: [Céu limpo]");
        pnlTempoAgora.add(lblCidade);
        pnlTempoAgora.add(lblHoraAtual);
        pnlTempoAgora.add(lblTempoAtual);
        pnlTempoAgora.add(lblDescAtual);

        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        pnlMeio.add(pnlTempoAgora, gbc);

        add(pnlTopo, BorderLayout.NORTH); // Adiciona o painel de controle na parte superior da janela
        add(pnlMeio, BorderLayout.CENTER);

        // --- 2. Widget: Previsão do Tempo ---
        JPanel pnlPrevisao = criarWidget("Previsão Diária");
        JLabel lblPrevisao = new JLabel("Próximos dias: [Dados de previsão aqui]");
        pnlPrevisao.add(lblPrevisao);
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 1; // Linha 1
        pnlMeio.add(pnlPrevisao, gbc);


    }

    /**
     * Método auxiliar para criar um painel de widget padronizado.
     * @param title O título do widget.
     * @return Um JPanel configurado como um widget.
     */

    private JPanel criarWidget(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Organiza os itens verticalmente
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1), // Borda cinza
                new EmptyBorder(15, 15, 15, 15) // Preenchimento interno
        ));

        // Título do widget
        JLabel titleLabel = new JLabel("<html><font color='#e67e22'>" + title + "</font></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o título
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10)); // Espaço abaixo do título
        return panel;
    }
}