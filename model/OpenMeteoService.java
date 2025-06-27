package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenMeteoService {
    /**
     * Método para consumir a API Open Meteo e obter dados de previsão do tempo.
     *
     * @throws Exception se ocorrer um erro ao fazer a requisição HTTP.
     */
    public static void tempoAPI{
        Double latitude = -30.0392;
        Double longitude = -52.8939;

        String apiUrl = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=-30.0392&longitude=-52.8939&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&hourly=temperature_2m,precipitation_probability,apparent_temperature&current=temperature_2m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America%2FSao_Paulo",
                latitude.toString().replace(",", "."), longitude.toString().replace(",", "."));
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java/OpenMeteoConsumer"); // Boa prática
            int responseCode = connection.getResponseCode();
            System.out.println("Código de Resposta: " + responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



            }



    }
}