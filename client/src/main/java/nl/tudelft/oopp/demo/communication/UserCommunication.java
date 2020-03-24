package nl.tudelft.oopp.demo.communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class UserCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    // TODO maybe return a flag that will be different if there is an error or if the credentials are wrong
    /**
     * Authenticates a user and sets a session cookie.
     * @return A boolean - true if the server has authenticated the user; false in every other case.
     */
    public static boolean authenticate(String username, String password) {
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/login")).setHeader("Authorization", "Basic " + encodedCredentials).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return false;
        }

//        ObjectMapper mapper = new ObjectMapper();
//        String role = null;
//        try {
//            role = mapper.readValue(response.body(), new TypeReference<ArrayList<String>>(){});
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(response.body());
//        }

        // set received cookie
        Authenticator.SESSION_COOKIE = response.headers().allValues(("Set-cookie")).get(0).split("; ")[0];
        ObjectMapper mapper = new ObjectMapper();
        List<String> responseString = null;
        try {
            responseString = mapper.readValue(response.body(),
                    new TypeReference<List<String>>() {
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // set user's role
        Authenticator.ROLE = responseString.get(0);

        // set the user's id
        Authenticator.ID = Long.parseLong(responseString.get(1));
        Authenticator.USERNAME = username;

        System.out.println(Authenticator.ROLE + "; IS ADMIN - " + Authenticator.isAdmin());
        return true;
    }

}
