package org.patrickmusicas.screensound.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {
    public static String obterTraducao(String texto) {
        // The client gets the API key from the environment variable `GOOGLE_API_KEY`.
        Client client = Client.builder().apiKey(System.getenv("GEMINI_API_KEY")).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "Forneça para mim um resumo, de no máximo 10 linhas, sobre o estilo e carreira do seguinte cantor " + texto,
                        null);

        return response.text();
    }
}
