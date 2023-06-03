package e1.i3.e1i3.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class createAuth {


    static String token = null;
    public static String createToken(String NodeId, String KeyId, String SecretKey) throws IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://web3.luniverse.io/v1/auth-token"))
                .header("accept", "application/json")
                .header("X-NODE-ID", NodeId)
                .header("X-Key-ID", KeyId)
                .header("X-Key-Secret", SecretKey)
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        if (response != null) {
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            token = rootNode.get("access_token").asText();

        }
        else{
            System.out.println("실패함");
        }

        return token;
    }
}
