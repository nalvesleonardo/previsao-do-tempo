package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMeteoService {
    /**
     * Método para consumir a API Open Meteo e obter dados de previsão do tempo.
     *
     * @return Uma String contendo a resposta JSON da API.
     * @throws Exception se ocorrer um erro ao fazer a requisição HTTP.
     */
    public static String tempoAPI() throws Exception {
        // URL da API com coordenadas fixas e parâmetros para dados diários, horários e atuais
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=-30.0392&longitude=-52.8939&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&hourly=temperature_2m,precipitation_probability,apparent_temperature&current=temperature_2m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America%2FSao_Paulo";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Java/OpenMeteoConsumer"); // Boa prática

        int responseCode = connection.getResponseCode();
        System.out.println("Código de Resposta: " + responseCode);

        // Se a requisição for bem-sucedida (código 200)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Usa um BufferedReader para ler a resposta da conexão
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Lê a resposta linha por linha e a anexa ao StringBuilder
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Retorna a resposta completa como uma String JSON
            return response.toString();
        } else {
            // Se a resposta não for OK, lança uma exceção com o código de erro
            throw new RuntimeException("Falha na requisição HTTP: " + responseCode);
        }
    }
}