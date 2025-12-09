package testing.proiectcolectivback.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.proiectcolectivback.DTO.SpotifyLoginRequest;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Repository.UserRepository;
import testing.proiectcolectivback.Service.JwtService;
import testing.proiectcolectivback.Service.SpotifyService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final SpotifyService spotifyService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(SpotifyService spotifyService,
                          UserRepository userRepository,
                          JwtService jwtService) {
        this.spotifyService = spotifyService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/spotify")
    public ResponseEntity<?> loginWithSpotify(@RequestBody SpotifyLoginRequest req) {
        if (req.getAccessToken() == null || req.getAccessToken().isBlank()) {
            return ResponseEntity.badRequest().body("Missing accessToken");
        }

        Map<String, Object> profile = spotifyService.getCurrentUserProfile(req.getAccessToken());
        if (profile == null || profile.get("id") == null) {
            return ResponseEntity.status(401).body("Invalid Spotify token");
        }

        String spotifyId = (String) profile.get("id");
        String email = (String) profile.get("email"); // poate fi null dacă nu ai scope-ul de email
        String displayName = (String) profile.get("display_name");

        AppUser user = userRepository.findById(spotifyId)
                .orElse(new AppUser());
        user.setId(spotifyId);
        user.setEmail(email);
        user.setDisplayName(displayName);
        userRepository.save(user);

        String jwt = jwtService.generateToken(spotifyId);

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "spotifyId", spotifyId,
                "email", email,
                "displayName", displayName
        ));
    }
}
