package io.github.zapolyarnydev.authservice.unit;

import io.github.zapolyarnydev.authservice.AuthServiceApplication;
import io.github.zapolyarnydev.authservice.entity.AuthRole;
import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import io.github.zapolyarnydev.authservice.security.jwt.JwtUtil;
import io.github.zapolyarnydev.authservice.security.jwt.validation.JwtValidationStatus;
import io.github.zapolyarnydev.authservice.config.TestsEnvLoader;
import io.github.zapolyarnydev.authservice.unit.config.JwtTestsConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = AuthServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-tests")
@Tag("unit")
@DisplayName("Работа с JWT токенами")
@ExtendWith(MockitoExtension.class)
@Import(JwtTestsConfiguration.class)
public class JwtTests {

    private static Logger logger;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeAll
    public static void loadEnv() {
        TestsEnvLoader.load();
    }

    @BeforeAll
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(JwtTests.class);
    }

    @ParameterizedTest(name = "Сохранение субьекта({index}) - введёный email ({0}) должен корректно сохраниться в refresh токене")
    @DisplayName("Сохранение субьекта данных в refresh токене")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldSaveDataInRefreshTokenAndParseIt(String email){
        var user = new AuthUser();
        user.setEmail(email);
        user.setAuthRole(AuthRole.USER);

        String token = jwtUtil.generateRefreshToken(user);
        var claims = jwtUtil.getJwsClaims(token).getBody();

        assertEquals(email, claims.getSubject(), "Извлечённый email отличается от введённого");
        logger.info("Проверка email '{}' прошла успешно, извлечённые данные: {}", email, claims.getSubject());

        assertEquals("refresh", claims.get("type"), "Токен не имеет тип \"refresh\"");
        logger.info("Проверка типа токена верна! Сгенерированный токен типа '{}' ", claims.get("type"));

        assertEquals("USER", claims.get("role"), "Токен не сохранил роль \"ROLE\"");
        logger.info("Проверка сохранённой роли пройдена! Роль: '{}' ", claims.get("role"));
    }

    @ParameterizedTest(name = "Сохранение субьекта({index}) - введёный email ({0}) должен корректно сохраниться в access токене")
    @DisplayName("Сохранение субьекта данных в access токене")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldSaveDataInAccessTokenAndParseIt(String email){
        var user = new AuthUser();
        user.setEmail(email);
        user.setAuthRole(AuthRole.USER);

        String token = jwtUtil.generateAccessToken(user);
        var claims = jwtUtil.getJwsClaims(token).getBody();

        assertEquals(email, claims.getSubject(), "Извлечённый email отличается от введённого");
        logger.info("Проверка email '{}' прошла успешно, извлечённые данные: {}", email, claims.getSubject());

        assertEquals("access", claims.get("type"), "Токен не имеет тип \"access\"");
        logger.info("Проверка типа токена верна! Сгенерированный токен типа '{}' ", claims.get("type"));

        assertEquals("USER", claims.get("role"), "Токен не сохранил роль \"ROLE\"");
        logger.info("Проверка сохранённой роли пройдена! Роль: '{}' ", claims.get("role"));
    }

    @ParameterizedTest(name = "Работа с некорректным токеном({index}) - введённый токен ({0}) не должен быть принят")
    @DisplayName("Валидация должна вернуть MALFORMED для некорректного токена")
    @ValueSource(strings = {"w", "wrongTOOOKEN", "this.is.not.jwt"})
    public void shouldReturnMalformedForInvalidToken(String badToken) {
        var result = jwtUtil.validateToken(badToken);
        assertEquals(JwtValidationStatus.MALFORMED, result.validationStatus());
        logger.info("Тест успешен. Некорректный токен не был принят");
    }

    @Test
    @DisplayName("Дата создания токена должна быть корректной")
    public void shouldTokenCreatedInCorrectDate() {
        var user = new AuthUser();
        user.setEmail("examplemail");
        user.setAuthRole(AuthRole.USER);

        String token = jwtUtil.generateRefreshToken(user);
        Date issuedAt = jwtUtil.getJwsClaims(token).getBody().getIssuedAt();
        Date now = new Date();

        long deltaMillis = Math.abs(now.getTime() - issuedAt.getTime());

        assertTrue(deltaMillis < 5000, "Дата создания токена отличается от реальной");
    }
}
