import model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String URL = "http://94.198.50.185:7081/api/users";
        String sessionID = null;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
        HttpHeaders headersResponse = response.getHeaders();
        List<String> cookies = headersResponse.get("Set-Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID")) {
                    sessionID = cookie.split(";")[0];
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionID);

        User user = new User(3L, "James", "Brown", (byte) 25);
        HttpEntity<User> http = new HttpEntity<>(user, headers);
        String response1 = restTemplate.postForEntity(URL, http, String.class).getBody();

        user.setId(3L);
        user.setName("Thomas");
        user.setLastName("Shelby");

        String response2 = restTemplate.exchange(URL, HttpMethod.PUT, http, String.class).getBody();
        String response3 = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, http, String.class).getBody();

        System.out.println(response1 + response2 + response3);
    }
}
