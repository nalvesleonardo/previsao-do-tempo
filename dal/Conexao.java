package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexao {
    private final String driver = "org.gjt.mm.mysql.Driver";
    private final String url = "jdbc:mysql://localhost/" + "climadb";
    private final String usuario = "root";
    private final String senha = "1234";
    private final Connection conexao;

    private static Conexao instancia=null;
    public static Conexao getInstancia(){
        try {
            if(instancia==null)
                instancia = new Conexao();

            return instancia;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private Conexao() throws ClassNotFoundException, SQLException {
        //Class.forName(driver);
        conexao = DriverManager.getConnection(url, usuario, senha);
    }

    public PreparedStatement preparar(String sql) throws SQLException{
        return conexao.prepareStatement(sql);
    }

    public ResultSet consultar(String sql){
        try {
            PreparedStatement st = preparar(sql);
            ResultSet rs = st.executeQuery();

            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}