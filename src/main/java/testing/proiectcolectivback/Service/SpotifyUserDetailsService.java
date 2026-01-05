package testing.proiectcolectivback.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testing.proiectcolectivback.Domain.AppUser;
import testing.proiectcolectivback.Repository.UserRepository;

@Service
public class SpotifyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public SpotifyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String spotifyId) throws UsernameNotFoundException {
        AppUser user = userRepository.findById(spotifyId).orElseThrow(() -> new UsernameNotFoundException("User not found: " + spotifyId));

        return User.withUsername(user.getId())
                .password("")
                .roles("USER")
                .build();
    }

}
