package dal;

import java.sql.*;


public class Conexao {

    private final String driver = "org.gjt.mm.mysql.Driver";
    private final String url = "jdbc:mysql://localhost/" + "climadb";
    private final String usuario = "root";
    private final String senha = "1234";
    private final Connection conexao;

    public Conexao() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conexao = DriverManager.getConnection(url, usuario, senha);
    }

    public Connection getConexao() {
        return conexao;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conexao.prepareStatement(sql);
    }

    public void fechar() throws SQLException {
        conexao.close();
    }
}