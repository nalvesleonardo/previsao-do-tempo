package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale; // Importe a classe Locale

public class OpenMeteoService {
    /**
     * Método para consumir a API Open Meteo e obter dados de previsão do tempo.
     *
     * @param latitude A latitude da cidade.
     * @param longitude A longitude da cidade.
     * @return Uma String contendo a resposta JSON da API.
     * @throws Exception se ocorrer um erro ao fazer a requisição HTTP.
     */
    public static String tempoAPI(double latitude, double longitude) throws Exception {
        // **A SOLUÇÃO ESTÁ AQUI:** Usamos Locale.US para garantir o ponto como separador decimal.
        String apiUrl = String.format(Locale.US, "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&hourly=temperature_2m,precipitation_probability,apparent_temperature&current=temperature_2m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America/Sao_Paulo", latitude, longitude);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Java/OpenMeteoConsumer"); // Boa prática

        int responseCode = connection.getResponseCode();
        System.out.println("URL da API: " + apiUrl); // Linha para depuração
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
            // **MELHORIA:** Lê a mensagem de erro detalhada da API.
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            // Lança uma exceção com a mensagem de erro da API para facilitar a depuração.
            throw new RuntimeException("Falha na requisição HTTP: " + responseCode + ". Resposta da API: " + errorResponse);
        }
    }
}