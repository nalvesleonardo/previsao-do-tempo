package view;

import com.google.gson.Gson;
import model.*;
import dal.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import java.util.List;

class FrmPrevisaoTempo extends JFrame {
    private JComboBox<Localizacao> cmbCidade;
    private JLabel lblCidade;
    private JLabel lblHoraAtual;
    private JLabel lblTempoAtual;
    private JLabel lblDescAtual;
    private JLabel lblPrevisao;
    private JPanel pnlTopo;
    private JPanel pnlMeio;
    private PrevisaoResposta previsaoAtual;

    public FrmPrevisaoTempo() {
        super("Previsão do Tempo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initMenu();
        initUI();
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        menuArquivo.add(itemSair);

        JMenu menuDados = new JMenu("Dados");
        JMenuItem itemAtualizar = new JMenuItem("Atualizar dados");
        JMenuItem itemHistorico = new JMenuItem("Ver Histórico");
        menuDados.add(itemAtualizar);
        menuDados.add(itemHistorico);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemConfiguracoes = new JMenuItem("Configurações");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemConfiguracoes);
        menuAjuda.add(itemSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuDados);
        menuBar.add(menuAjuda);
        setJMenuBar(menuBar);

        itemSair.addActionListener(e -> System.exit(0));
        itemAtualizar.addActionListener(e -> atualizarDadosTempo());
        itemHistorico.addActionListener(e -> new FrmHistorico().setVisible(true));
        itemConfiguracoes.addActionListener(e -> new FrmConfiguracoes().setVisible(true));
        itemSobre.addActionListener(e -> new FrmSobre().setVisible(true));
    }

    private void initUI() {
        pnlTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTopo.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblSelecionar = new JLabel("Selecione a cidade:");
        lblSelecionar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlTopo.add(lblSelecionar);

        cmbCidade = new JComboBox<>();
        cmbCidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCidade.setPreferredSize(new Dimension(220, 25));
        pnlTopo.add(cmbCidade);

        carregarCidadesDoBanco();

        cmbCidade.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged") && cmbCidade.getSelectedItem() != null) {
                atualizarDadosTempo();
            }
        });

        pnlMeio = new JPanel(new GridBagLayout());
        pnlMeio.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel pnlTempoAgora = criarWidget("Tempo Agora");
        lblCidade = criarLabelInfo("Local: [Selecione uma cidade e atualize]");
        lblHoraAtual = criarLabelInfo("Data/Hora: [N/A]");
        lblTempoAtual = criarLabelInfo("Temperatura: [N/A]");
        lblDescAtual = criarLabelInfo("Precipitação: [N/A]");

        pnlTempoAgora.add(lblCidade);
        pnlTempoAgora.add(lblHoraAtual);
        pnlTempoAgora.add(lblTempoAtual);
        pnlTempoAgora.add(lblDescAtual);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlMeio.add(pnlTempoAgora, gbc);

        JPanel pnlPrevisao = criarWidget("Previsão Diária");
        lblPrevisao = criarLabelInfo("<html>Próximos dias: [Atualize para ver a previsão]</html>");
        pnlPrevisao.add(lblPrevisao);

        gbc.gridy = 1;
        pnlMeio.add(pnlPrevisao, gbc);

        add(pnlTopo, BorderLayout.NORTH);
        add(pnlMeio, BorderLayout.CENTER);
    }

    private JLabel criarLabelInfo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JPanel criarWidget(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel("<html><font color='#2980b9'>" + titulo + "</font></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private void carregarCidadesDoBanco() {
        try {
            DAO<Localizacao> dao = new DAO<>();
            List<Localizacao> cidades = dao.listarLocalizacoes();
            cmbCidade.removeAllItems();
            for (Localizacao cidade : cidades) {
                cmbCidade.addItem(cidade);
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades do banco de dados:\n" + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDadosTempo() {
        try {
            Localizacao cidadeSelecionada = (Localizacao) cmbCidade.getSelectedItem();
            if (cidadeSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma cidade primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String jsonResponse = OpenMeteoService.tempoAPI(cidadeSelecionada.getLatitude(), cidadeSelecionada.getLongitude());
            Gson gson = new Gson();
            previsaoAtual = gson.fromJson(jsonResponse, PrevisaoResposta.class);
            exibirDadosTempo();

            try {
                DAO dao = new DAO();
                int localizacaoId = cidadeSelecionada.getId();
                AtualAPI a = previsaoAtual.getAtual();
                DadosHorarios dadosAtuais = new DadosHorarios(LocalDateTime.parse(a.getTempo()), a.getTemperatura(), a.getSensacaoTermica(), a.getPrecipitacao());
                dao.inserir(dadosAtuais, localizacaoId);

                DiarioAPI d = previsaoAtual.getDiario();
                for (int i = 0; i < d.getTempo().length; i++) {
                    DadosDiarios dadosDoDia = new DadosDiarios(
                            d.getTempo()[i],
                            d.getTemperaturaMax()[i],
                            d.getTemperaturaMin()[i],
                            (double) d.getPrecipitacaoMax()[i]
                    );
                    dao.inserir(dadosDoDia, localizacaoId);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar dados no banco:\n" + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso para " + cidadeSelecionada.getCidade() + "!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados do tempo:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirDadosTempo() {
        Localizacao cidadeSelecionada = (Localizacao) cmbCidade.getSelectedItem();
        if (previsaoAtual != null && cidadeSelecionada != null) {
            AtualAPI a = previsaoAtual.getAtual();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            lblCidade.setText(String.format("Local: %s (%.2f, %.2f)", cidadeSelecionada.getCidade(), cidadeSelecionada.getLatitude(), cidadeSelecionada.getLongitude()));
            lblHoraAtual.setText("Data/Hora: " + LocalDateTime.parse(a.getTempo()).format(formatter));
            lblTempoAtual.setText(String.format("Temperatura: %.1f°C (Sensação: %.1f°C)", a.getTemperatura(), a.getSensacaoTermica()));
            lblDescAtual.setText(String.format("Precipitação: %.1f mm", a.getPrecipitacao()));

            DiarioAPI d = previsaoAtual.getDiario();
            StringBuilder previsaoHtml = new StringBuilder("<html>");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

            for (int i = 0; i < d.getTempo().length; i++) {
                String data = LocalDateTime.parse(d.getTempo()[i] + "T00:00:00").format(dateFormatter);
                previsaoHtml.append(String.format("<b>%s:</b> Min %.1f°C, Max %.1f°C, Chuva %d%%<br>",
                        data, d.getTemperaturaMin()[i], d.getTemperaturaMax()[i], d.getPrecipitacaoMax()[i]));
            }

            previsaoHtml.append("</html>");
            lblPrevisao.setText(previsaoHtml.toString());
        }
    }
}