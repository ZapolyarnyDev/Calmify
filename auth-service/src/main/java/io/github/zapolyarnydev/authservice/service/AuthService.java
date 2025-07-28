package io.github.zapolyarnydev.authservice.service;

import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.authservice.exception.EmailNotFoundException;
import io.github.zapolyarnydev.authservice.exception.UserNotFoundException;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import io.github.zapolyarnydev.authservice.exception.InvalidCredentialsException;
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

    public AuthUser authenticate(String email, String password) throws InvalidCredentialsException {
        var authUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        if(!passwordEncoder.matches(password, authUser.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return authUser;
    }

    public AuthUser findUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
