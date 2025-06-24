package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WeatherDashboardApp extends JFrame {

    // Componentes da UI (apenas para visualização, sem funcionalidade por enquanto)
    private JTextField cityInput;
    private JComboBox<String> timeComboBox;
    private JLabel currentLocationLabel;
    private JLabel currentDateTimeLabel;
    private JLabel currentTempLabel;
    private JLabel currentDescriptionLabel;
    private JPanel hourlyForecastPanel;
    private JPanel dailyForecastPanel;

    public WeatherDashboardApp() {
        super("Dashboard de Meteorologia (Apenas UI)"); // Título da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o que acontece ao fechar a janela
        setSize(800, 650); // Tamanho inicial da janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        initUI(); // Chama o método para construir a interface
    }

    private void initUI() {
        // Define o layout principal da janela como BorderLayout
        setLayout(new BorderLayout(10, 10)); // 10px de espaçamento horizontal e vertical

        // --- Painel de Controle (Topo da Tela) ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout de fluxo centralizado
        controlPanel.setBorder(new EmptyBorder(10, 10, 5, 10)); // Espaçamento interno
        controlPanel.setBackground(new Color(44, 62, 80)); // Cor de fundo escura

        // Campo para a cidade
        controlPanel.add(new JLabel("<html><font color='white'>Cidade:</font></html>"));
        cityInput = new JTextField(15); // Campo de texto com 15 colunas
        cityInput.setFont(new Font("Arial", Font.PLAIN, 14));
        cityInput.setText("São Paulo"); // Texto padrão
        controlPanel.add(cityInput);

        // Botão de busca (sem ação por enquanto)
        JButton searchButton = new JButton("Buscar");
        controlPanel.add(searchButton);

        // Seletor de Data e Hora (DatePicker não nativo, simulado com ComboBox para a hora)
        controlPanel.add(new JLabel("<html><font color='white'>Data/Hora:</font></html>"));
        // Para a data, você pode adicionar um JTextField aqui para digitação manual ou integrar uma lib externa.
        // JButton datePickerButton = new JButton("Selecionar Data"); // Poderia abrir um calendário
        // controlPanel.add(datePickerButton);

        timeComboBox = new JComboBox<>();
        for (int i = 0; i < 24; i++) { // Preenche o ComboBox com horas de 00:00 a 23:00
            timeComboBox.addItem(String.format("%02d:00", i));
        }
        timeComboBox.setSelectedItem("12:00"); // Hora padrão
        controlPanel.add(timeComboBox);

        add(controlPanel, BorderLayout.NORTH); // Adiciona o painel de controle na parte superior da janela

        // --- Painel Central (Onde os Widgets Vão Ficar) ---
        JPanel dashboardPanel = new JPanel(new GridBagLayout()); // Usa GridBagLayout para flexibilidade
        dashboardPanel.setBackground(new Color(52, 73, 94)); // Cor de fundo do dashboard
        dashboardPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espaçamento interno

        // Configurações base para GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margem entre os widgets
        gbc.fill = GridBagConstraints.BOTH; // Componentes preenchem o espaço disponível
        gbc.weightx = 1.0; // Distribuem o espaço horizontalmente
        gbc.weighty = 1.0; // Distribuem o espaço verticalmente

        // --- 1. Widget: Dados do Tempo Atual (Agora) ---
        JPanel currentWeatherPanel = createWidgetPanel("Tempo Agora");
        currentLocationLabel = createStyledLabel("Local: [Cidade, País]");
        currentDateTimeLabel = createStyledLabel("Data/Hora: [DD/MM/YYYY HH:MM]");
        currentTempLabel = createStyledLabel("Temperatura: [XX.X°C]");
        currentDescriptionLabel = createStyledLabel("Descrição: [Céu limpo]");
        currentWeatherPanel.add(currentLocationLabel);
        currentWeatherPanel.add(currentDateTimeLabel);
        currentWeatherPanel.add(currentTempLabel);
        currentWeatherPanel.add(currentDescriptionLabel);

        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        dashboardPanel.add(currentWeatherPanel, gbc);

        // --- 2. Widget: Previsão Horária Resumida (Próximas Horas) ---
        hourlyForecastPanel = createWidgetPanel("Previsão Horária");
        hourlyForecastPanel.setLayout(new BoxLayout(hourlyForecastPanel, BoxLayout.Y_AXIS)); // Conteúdo vertical
        // Placeholders para previsão horária
        hourlyForecastPanel.add(createStyledLabel("10:00 - 20°C (Ensolarado)"));
        hourlyForecastPanel.add(createStyledLabel("13:00 - 22°C (Parcialmente Nublado)"));
        hourlyForecastPanel.add(createStyledLabel("16:00 - 21°C (Chuva Leve)"));
        hourlyForecastPanel.add(createStyledLabel("19:00 - 18°C (Nublado)"));

        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 0; // Linha 0
        dashboardPanel.add(hourlyForecastPanel, gbc);

        // --- 3. Widget: Previsão Diária Resumida (Próximos Dias) ---
        dailyForecastPanel = createWidgetPanel("Previsão Diária");
        dailyForecastPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Layout horizontal com espaçamento
        // Placeholders para previsão diária
        dailyForecastPanel.add(createDailyForecastItem("Qua", "25/06", "25°/15°C", "Sol"));
        dailyForecastPanel.add(createDailyForecastItem("Qui", "26/06", "23°/14°C", "Nublado"));
        dailyForecastPanel.add(createDailyForecastItem("Sex", "27/06", "20°/12°C", "Chuva"));
        dailyForecastPanel.add(createDailyForecastItem("Sáb", "28/06", "24°/13°C", "Parcial"));
        dailyForecastPanel.add(createDailyForecastItem("Dom", "29/06", "26°/16°C", "Sol"));


        gbc.gridx = 0; // Começa na coluna 0
        gbc.gridy = 1; // Linha 1
        gbc.gridwidth = 2; // Ocupa duas colunas
        dashboardPanel.add(dailyForecastPanel, gbc);

        add(dashboardPanel, BorderLayout.CENTER); // Adiciona o painel de dashboard no centro da janela

        setVisible(true); // Torna a janela visível
    }

    /**
     * Método auxiliar para criar um painel de widget padronizado.
     * @param title O título do widget.
     * @return Um JPanel configurado como um widget.
     */
    private JPanel createWidgetPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Organiza os itens verticalmente
        panel.setBackground(new Color(52, 73, 94)); // Cor de fundo do widget
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

    /**
     * Método auxiliar para criar um JLabel com estilo padronizado.
     * @param text O texto do label.
     * @return Um JLabel com cor de texto e fonte definidas.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel("<html><font color='#ecf0f1'>" + text + "</font></html>"); // Texto branco claro
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    /**
     * Método auxiliar para criar um item visual para a previsão diária.
     * @param day Nome do dia da semana (ex: "Seg", "Ter").
     * @param date Data (ex: "DD/MM").
     * @param tempMinMax Temperaturas mínima e máxima (ex: "XX°/YY°C").
     * @param description Descrição do tempo (ex: "Sol", "Chuva").
     * @return Um JPanel representando um dia da previsão.
     */
    private JPanel createDailyForecastItem(String day, String date, String tempMinMax, String description) {
        JPanel dayBox = new JPanel();
        dayBox.setLayout(new BoxLayout(dayBox, BoxLayout.Y_AXIS));
        dayBox.setBackground(new Color(41, 128, 185)); // Cor de fundo do item de dia (azul)
        dayBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dayBox.setPreferredSize(new Dimension(120, 120)); // Tamanho fixo para cada item de dia

        JLabel dayLabel = new JLabel("<html><font color='white'>" + day + "</font></html>");
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayBox.add(dayLabel);

        JLabel dateLabel = new JLabel("<html><font color='white'>" + date + "</font></html>");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayBox.add(dateLabel);

        JLabel tempLabel = new JLabel("<html><font color='white'>" + tempMinMax + "</font></html>");
        tempLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayBox.add(tempLabel);

        JLabel descLabel = new JLabel("<html><font color='white'>" + description + "</font></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayBox.add(descLabel);

        return dayBox;
    }
}