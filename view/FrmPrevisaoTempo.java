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
        super("Previsão do Tempo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        initMenu();
        initUI();
    }

    /**
     * Método para inicializar a barra de menu da aplicação.
     * Adiciona menus e itens de menu com ações básicas.
     */
    private void initMenu(){
        // Barra de menu
        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemDashboard = new JMenuItem("Dashboard");
        JMenuItem itemSair = new JMenuItem("Sair");
        menuArquivo.add(itemDashboard);
        menuArquivo.add(itemSair);

        // Menu Dados
        JMenu menuDados = new JMenu("Dados");
        JMenuItem itemAtualizar = new JMenuItem("Atualizar dados");
        JMenuItem itemHistorico = new JMenuItem("Histórico");
        menuDados.add(itemAtualizar);
        menuDados.add(itemHistorico);

        // Menu Configurações
        JMenu menuConfiguracoes = new JMenu("Configurações");
        JMenuItem itemPreferencias = new JMenuItem("Preferências");
        menuConfiguracoes.add(itemPreferencias);

        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemSobre);

        // Adiciona os menus à barra de menu
        menuBar.add(menuArquivo);
        menuBar.add(menuDados);
        menuBar.add(menuConfiguracoes);
        menuBar.add(menuAjuda);

        // Define a barra de menu na janela
        setJMenuBar(menuBar);

        // Exemplo de ação para o item "Sair"
        itemSair.addActionListener(e -> System.exit(0));

        itemDashboard.addActionListener(e -> {


        });

        itemAtualizar.addActionListener(e -> {
            // Aqui você pode implementar a lógica para atualizar os dados
            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
        });

        itemHistorico.addActionListener(e -> {
            // Aqui você pode implementar a lógica para exibir o histórico
            JOptionPane.showMessageDialog(this, "Histórico de dados ainda não implementado.");
        });

        itemPreferencias.addActionListener(e -> {
            // Aqui você pode implementar a lógica para abrir as preferências
            JOptionPane.showMessageDialog(this, "Preferências ainda não implementadas.");
        });

        itemSobre.addActionListener(e -> {
            // Aqui você pode implementar a lógica para exibir informações sobre o aplicativo
            JOptionPane.showMessageDialog(this, "Aplicativo de Previsão do Tempo - Versão 1.0");
        });
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