package testing.proiectcolectivback.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SpotifyService {

    private final WebClient webClient;

    public SpotifyService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.spotify.com/v1")
                .build();
    }

    public Map<String, Object> getCurrentUserProfile(String accessToken) {
        return webClient.get()
                .uri("/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
