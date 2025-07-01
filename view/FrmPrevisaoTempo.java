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

public class FrmPrevisaoTempo extends JFrame {

    private JComboBox<Localizacao> cmbCidade;
    private JLabel lblCidade;
    private JLabel lblHoraAtual;
    private JLabel lblTempoAtual;
    private JLabel lblDescAtual;
    private JLabel lblPrevisao; // Label para a previsão diária
    private JPanel pnlTopo;
    private JPanel pnlMeio;

    private PrevisaoResposta previsaoAtual;

    public FrmPrevisaoTempo() {
        super("Previsão do Tempo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        initMenu();
        initUI();

        if (cmbCidade.getItemCount() > 0) {
            cmbCidade.setSelectedIndex(0);
            atualizarDadosTempo();
        }
    }

    private void carregarCidadesDoBanco() {
        try {
            DAO<Localizacao> dao = new DAO<>();
            List<Localizacao> cidades = dao.listarLocalizacoes(); //

            // Limpa o ComboBox antes de adicionar novos itens
            cmbCidade.removeAllItems();

            // Adiciona as cidades carregadas ao ComboBox
            for (Localizacao cidade : cidades) {
                cmbCidade.addItem(cidade);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cidades do banco de dados:\n" + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        menuArquivo.add(itemSair);

        JMenu menuDados = new JMenu("Dados");
        JMenuItem itemAtualizar = new JMenuItem("Atualizar dados");
        menuDados.add(itemAtualizar);
        itemAtualizar.addActionListener(e -> atualizarDadosTempo());


        JMenuItem itemHistorico = new JMenuItem("Ver Histórico");
        menuDados.add(itemHistorico);
        itemHistorico.addActionListener(e -> {
            FrmHistorico frmHistorico = new FrmHistorico();
            frmHistorico.setVisible(true);
        });

        menuBar.add(menuArquivo);
        menuBar.add(menuDados);
        setJMenuBar(menuBar);

        itemSair.addActionListener(e -> System.exit(0));

    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        pnlTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTopo.setBorder(new EmptyBorder(10, 10, 5, 10));


        cmbCidade = new JComboBox<>();
        cmbCidade.setFont(new Font("Arial", Font.PLAIN, 14));
        pnlTopo.add(new JLabel("Selecione a cidade:"));
        pnlTopo.add(cmbCidade);

        carregarCidadesDoBanco();

        cmbCidade.addActionListener(e -> {
            // Apenas atualiza se o evento for de seleção real e não de remoção de itens
            if (e.getActionCommand().equals("comboBoxChanged") && cmbCidade.getSelectedItem() != null) {
                atualizarDadosTempo();
            }
        });


        pnlMeio = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel pnlTempoAgora = criarWidget("Tempo Agora");
        lblCidade = new JLabel("Local: [Selecione uma cidade e atualize]");
        lblHoraAtual = new JLabel("Data/Hora: [N/A]");
        lblTempoAtual = new JLabel("Temperatura: [N/A]");
        lblDescAtual = new JLabel("Umidade: [N/A]");
        pnlTempoAgora.add(lblCidade);
        pnlTempoAgora.add(lblHoraAtual);
        pnlTempoAgora.add(lblTempoAtual);
        pnlTempoAgora.add(lblDescAtual);
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlMeio.add(pnlTempoAgora, gbc);

        JPanel pnlPrevisao = criarWidget("Previsão Diária");
        lblPrevisao = new JLabel("<html>Próximos dias: [Atualize para ver a previsão]</html>");
        pnlPrevisao.add(lblPrevisao);
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlMeio.add(pnlPrevisao, gbc);

        add(pnlTopo, BorderLayout.NORTH);
        add(pnlMeio, BorderLayout.CENTER);
    }

    /**
     * Método que chama a API, processa o JSON e atualiza a interface.
     */
    private void atualizarDadosTempo() {
        try {
            Localizacao cidadeSelecionada = (Localizacao) cmbCidade.getSelectedItem();
            if (cidadeSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma cidade primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. Chamar a API para obter o JSON
            String jsonResponse = OpenMeteoService.tempoAPI(cidadeSelecionada.getLatitude(), cidadeSelecionada.getLongitude());

            // 2. Usar Gson para converter o JSON em objetos Java
            Gson gson = new Gson();
            previsaoAtual = gson.fromJson(jsonResponse, PrevisaoResposta.class);

            exibirDadosTempo();

            try {
                DAO dao = new DAO();
                int localizacaoId = cidadeSelecionada.getId();

                // 1. Salva os dados atuais (horários)
                AtualAPI a = previsaoAtual.getAtual();
                DadosHorarios dadosAtuais = new DadosHorarios(
                        LocalDateTime.parse(a.getTempo()),
                        a.getTemperatura(),
                        a.getSensacaoTermica(),
                        a.getPrecipitacao()
                );
                dao.inserir(dadosAtuais, localizacaoId);

                // 2. Salva os dados da previsão diária
                DiarioAPI d = previsaoAtual.getDiario();
                for (int i = 0; i < d.getTempo().length; i++) {
                    DadosDiarios dadosDoDia = new DadosDiarios(
                            d.getTempo()[i], // A data já está como String "yyyy-MM-dd"
                            d.getTemperaturaMax()[i],
                            d.getTemperaturaMin()[i],
                            (double) d.getPrecipitacaoMax()[i] // A API devolve um int, o banco espera um double
                    );
                    dao.inserir(dadosDoDia, localizacaoId);
                }

                System.out.println("Dados salvos no banco para a cidade: " + cidadeSelecionada.getCidade());

            } catch (Exception dbException) {
                dbException.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar dados no banco:\n" + dbException.getMessage(),
                        "Erro de Banco de Dados",
                        JOptionPane.ERROR_MESSAGE);
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
            // Atualiza o painel "Tempo Agora"
            AtualAPI a = previsaoAtual.getAtual();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

            lblCidade.setText(String.format("Local: %s (%.2f, %.2f)", cidadeSelecionada.getCidade(), cidadeSelecionada.getLatitude(), cidadeSelecionada.getLongitude()));
            lblHoraAtual.setText("Data/Hora: " + LocalDateTime.parse(a.getTempo()).format(formatter));
            lblTempoAtual.setText(String.format("Temperatura: %.1f°C (Sensação: %.1f°C)", a.getTemperatura(), a.getSensacaoTermica()));
            lblDescAtual.setText(String.format("Precipitação: %.1f mm", a.getPrecipitacao()));

            // Atualiza o painel "Previsão Diária"
            DiarioAPI d = previsaoAtual.getDiario();
            StringBuilder previsaoHtml = new StringBuilder("<html>");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
            for (int i = 0; i < d.getTempo().length; i++) {
                String data = LocalDateTime.parse(d.getTempo()[i] + "T00:00:00").format(dateFormatter);
                previsaoHtml.append(String.format("<b>%s:</b> Min %.1f°C, Max %.1f°C, Chuva %d%%<br>",
                        data,
                        d.getTemperaturaMin()[i],
                        d.getTemperaturaMax()[i],
                        d.getPrecipitacaoMax()[i]));
            }
            previsaoHtml.append("</html>");
            lblPrevisao.setText(previsaoHtml.toString());
        }
    }


    private JPanel criarWidget(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel titleLabel = new JLabel("<html><font color='#e67e22'>" + title + "</font></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }
}