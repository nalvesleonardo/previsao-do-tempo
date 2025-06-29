package view;

import com.google.gson.Gson;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class FrmPrevisaoTempo extends JFrame {

    private JComboBox<String> cmbCidade;
    private JLabel lblCidade;
    private JLabel lblHoraAtual;
    private JLabel lblTempoAtual;
    private JLabel lblDescAtual;
    private JLabel lblPrevisao; // Label para a previsão diária
    private JPanel pnlTopo;
    private JPanel pnlMeio;

    public FrmPrevisaoTempo() {
        super("Previsão do Tempo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
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
        menuDados.add(itemAtualizar);

        menuBar.add(menuArquivo);
        menuBar.add(menuDados);
        setJMenuBar(menuBar);

        itemSair.addActionListener(e -> System.exit(0));

        // Ação para atualizar os dados do tempo
        itemAtualizar.addActionListener(e -> atualizarDadosTempo());
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        pnlTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTopo.setBorder(new EmptyBorder(10, 10, 5, 10));
        pnlTopo.setBackground(new Color(44, 62, 80));

        cmbCidade = new JComboBox<>();
        cmbCidade.setFont(new Font("Arial", Font.PLAIN, 14));
        pnlTopo.add(new JLabel("Selecione a cidade:")); // Label adicionado para clareza
        pnlTopo.add(cmbCidade);

        pnlMeio = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel pnlTempoAgora = criarWidget("Tempo Agora");
        lblCidade = new JLabel("Local: [Atualize para ver]");
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
            // 1. Chamar a API para obter o JSON
            String jsonResponse = OpenMeteoService.tempoAPI();

            // 2. Usar Gson para converter o JSON em objetos Java
            Gson gson = new Gson();
            PrevisaoResposta previsao = gson.fromJson(jsonResponse, PrevisaoResposta.class);

            // 3. Extrair os dados e atualizar a interface
            if (previsao != null) {
                // Atualiza o painel "Tempo Agora"
                AtualAPI a = previsao.getAtual();
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

                lblCidade.setText("Local: Cacequi, RS (-30.03, -52.89)"); // Local fixo, conforme API
                lblHoraAtual.setText("Data/Hora: " + LocalDateTime.parse(a.getTempo()).format(formatter));
                lblTempoAtual.setText(String.format("Temperatura: %.1f°C (Sensação: %.1f°C)", a.getTemperatura(), a.getSensacaoTermica()));
                lblDescAtual.setText(String.format("Precipitação: %.1f mm", a.getPrecipitacao()));

                // Atualiza o painel "Previsão Diária"
                DiarioAPI d = previsao.getDiario();
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

                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados do tempo:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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