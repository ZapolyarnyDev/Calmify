package io.github.zapolyarnydev.authservice.service;

import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthUser register(String email, String password) {
        if(userRepository.existsByEmail(email)) throw new EmailAlreadyUsedException(email);

        String hashedPassword = passwordEncoder.encode(password);
        var authUser = new AuthUser(email, hashedPassword);

        userRepository.save(authUser);

        return authUser;
    }

    public Optional<AuthUser> authenticate(String email, String password) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public AuthUser findUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with this email doesn't exist"));
    }
}
