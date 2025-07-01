package view;

import dal.DAO;
import model.Localizacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class FrmHistorico extends JFrame {

    private JComboBox<Localizacao> cmbCidade;
    private JTextField txtDataInicio, txtDataFim;
    private JButton btnBuscar;
    private JTable tblResultados;
    private JScrollPane scrollPane;
    private DAO dao;

    public FrmHistorico() {
        super("Histórico de Previsões");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela

        try {
            dao = new DAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initUI();
        carregarCidades();
    }

    private void initUI() {
        // --- PAINEL DE FILTROS (TOPO) ---
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        pnlFiltros.add(new JLabel("Cidade:"));
        cmbCidade = new JComboBox<>();
        cmbCidade.setPreferredSize(new Dimension(200, 25));
        pnlFiltros.add(cmbCidade);

        pnlFiltros.add(new JLabel("Data Início (AAAA-MM-DD):"));
        txtDataInicio = new JTextField(10);
        pnlFiltros.add(txtDataInicio);

        pnlFiltros.add(new JLabel("Data Fim (AAAA-MM-DD):"));
        txtDataFim = new JTextField(10);
        pnlFiltros.add(txtDataFim);

        btnBuscar = new JButton("Buscar Histórico");
        pnlFiltros.add(btnBuscar);

        // --- TABELA DE RESULTADOS (CENTRO) ---
        tblResultados = new JTable();
        scrollPane = new JScrollPane(tblResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // Adiciona os painéis à janela
        setLayout(new BorderLayout(5, 5));
        add(pnlFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Ação do botão buscar
        btnBuscar.addActionListener(e -> buscarHistorico());
    }

    private void carregarCidades() {
        try {
            List<Localizacao> cidades = dao.listarLocalizacoes();
            for (Localizacao cidade : cidades) {
                cmbCidade.addItem(cidade);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarHistorico() {
        Localizacao cidadeSelecionada = (Localizacao) cmbCidade.getSelectedItem();
        if (cidadeSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma cidade.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dataInicio = txtDataInicio.getText();
        String dataFim = txtDataFim.getText();

        try {
            // Usa o método do DAO para buscar os dados
            ResultSet rs = dao.listarHistoricoPorData(cidadeSelecionada.getId(), dataInicio, dataFim);

            // Converte o ResultSet em um TableModel para exibir na JTable
            tblResultados.setModel(buildTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar histórico: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método utilitário para converter um ResultSet em um DefaultTableModel
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Nomes das colunas
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Dados das linhas
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}