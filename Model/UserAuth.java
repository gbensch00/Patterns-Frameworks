package Model;

import java.net.URI;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class UserAuth {
    private String name;
    private String password;
    private String jwt;

    public UserAuth(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean authenticate() {
        try {
            // Connect to XAMPP server with HTTP client
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost/pattern-frameworks/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JWT from response
            JSONObject jsonResponse = new JSONObject(response.body());
            jwt = jsonResponse.getString("jwt");

            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public String getJwt() {
        return jwt;
    }
}
