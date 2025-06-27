package dal;

import java.sql.*;
import model.*;

public class DAO <T>{
    private Conexao conn;

    public DAO() throws SQLException, ClassNotFoundException {
        conn = new Conexao();
    }

    public void inserir(T obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO";
        PreparedStatement st;

        if (obj instanceof Localizacao) {
            sql += " localizacao (cidade, estado, latitude, longitude, data_hora) VALUES (?, ?, ?, ?)";
            Localizacao l = (Localizacao) obj; // fazer o cast

            st = conn.prepareStatement(sql);
            st.setString(1, l.getCidade());
            st.setDouble(3, l.getLatitude());
            st.setDouble(4, l.getLongitude());
            st.setDate(5, l.getDataHora());

        } else {
            throw new IllegalArgumentException("Esta classe não possui tratamento para inserir no BD");
        }

        st.executeUpdate();
        st.close();
    }

}
