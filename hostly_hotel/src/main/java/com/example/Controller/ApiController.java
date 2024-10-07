package com.example.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.Model.Hospede;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiController {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final ObjectMapper mapper = new ObjectMapper(); // Jackson ObjectMapper

    // Método para buscar hóspede por CPF
    public Hospede buscarHospedePorCpf(String cpf) throws Exception {
        String url = BASE_URL + "/hospedes/cpf/" + cpf;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Deserializando a resposta JSON para um objeto Hospede
            return mapper.readValue(response.toString(), Hospede.class);
        } else {
            throw new Exception("Erro na requisição: " + responseCode);
        }
    }
}
