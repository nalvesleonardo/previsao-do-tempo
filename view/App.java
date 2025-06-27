package view;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
    public static void main(String[] args) {

        FrmPrevisaoTempo frm = new FrmPrevisaoTempo(); // Cria e exibe a janela
        frm.setVisible(true);
    }
}