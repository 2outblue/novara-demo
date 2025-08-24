package com.novara.novara_demo.init;

import com.novara.novara_demo.model.entity.User;
import com.novara.novara_demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder1;
    }

    @Override
    public void run(String... args) throws Exception {
        initUsers();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            userRepository.saveAll(getInitUsers());
        }
    }

    private List<User> getInitUsers() {
        return Arrays.asList(
                createUser(
                        UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                        "elon.vacation@example.com",
                        "Elon",
                        "Musk",
                        "SunnyMarsTrip42!"
                ).setRoles(new HashSet<>(List.of("ADMIN"))),
                createUser(
                        UUID.fromString("987fcdeb-51a2-43c7-8b9e-7a3b2c1d0e9f"),
                        "emma.smoothie@example.com",
                        "Emma",
                        "Watson",
                        "BerryBliss22#"
                ),
                createUser(
                        UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                        "leo.adventure@example.com",
                        "Leo",
                        "DiCaprio",
                        "BeachVibes99@"
                ),
                createUser(
                        UUID.fromString("a1b2c3d4-e5f6-7890-abcd-1234567890ab"),
                        "sophia.baker@example.com",
                        "Sophia",
                        "Cupcake",
                        "SweetTreats47$"
                ),
                createUser(
                        UUID.fromString("b2c3d4e5-f678-9012-cdef-34567890abcd"),
                        "max.traveler@example.com",
                        "Max",
                        null,
                        "RoadTripFun55%"
                ),
                createUser(
                        UUID.fromString("c3d4e5f6-7890-abcd-1234-567890abcdef"),
                        "luna.stargazer@example.com",
                        null,
                        "Stargazer",
                        "MoonlitNights88^"
                ),
                createUser(
                        UUID.fromString("d4e5f6a7-8901-2345-cdef-67890abcdef1"),
                        "oliver.pizzafan@example.com",
                        "Oliver",
                        "Slice",
                        "PepperoniPal22!"
                )
        );
    }

    private User createUser(UUID id, String email, String firstName, String lastName, String password) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }
}
