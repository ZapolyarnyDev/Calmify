package io.github.zapolyarnydev.unit;

import io.github.zapolyarnydev.authservice.AuthServiceApplication;
import io.github.zapolyarnydev.authservice.security.jwt.JwtUtil;
import io.github.zapolyarnydev.authservice.security.jwt.JwtValidationStatus;
import io.github.zapolyarnydev.config.TestsEnvLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthServiceApplication.class)
@ActiveProfiles("unit-tests")
@Tag("unit")
@DisplayName("Работа с JWT токенами")
public class JwtTests {

    private static Logger logger;

    @BeforeAll
    public static void loadEnv() {
        TestsEnvLoader.load();
    }

    @BeforeAll
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(JwtTests.class);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @ParameterizedTest(name = "Сохранение субьекта({index}) - введёный email ({0}) должен корректно сохраниться в refresh токене")
    @DisplayName("Сохранение субьекта данных в refresh токене")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldSaveSubjectInRefreshTokenAndParseIt(String email){
        String token = jwtUtil.generateRefreshToken(email);
        var claims = jwtUtil.getClaims(token).getBody();

        assertEquals(email, claims.getSubject(), "Извлечённый email отличается от введённого");
        logger.info("Проверка email '{}' прошла успешно, извлечённые данные: {}", email, claims.getSubject());

        assertEquals("refresh", claims.get("type"), "Токен не имеет тип \"refresh\"");
        logger.info("Проверка типа токена верна! Сгенерированный токен типа '{}' ", claims.get("type"));
    }

    @ParameterizedTest(name = "Сохранение субьекта({index}) - введёный email ({0}) должен корректно сохраниться в access токене")
    @DisplayName("Сохранение субьекта данных в access токене")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldSaveSubjectInAccessTokenAndParseIt(String email){
        String token = jwtUtil.generateAccessToken(email);
        var claims = jwtUtil.getClaims(token).getBody();

        assertEquals(email, claims.getSubject(), "Извлечённый email отличается от введённого");
        logger.info("Проверка email '{}' прошла успешно, извлечённые данные: {}", email, claims.getSubject());

        assertEquals("access", claims.get("type"), "Токен не имеет тип \"access\"");
        logger.info("Проверка типа токена верна! Сгенерированный токен типа '{}' ", claims.get("type"));
    }

    @ParameterizedTest(name = "Восстановление access токена({index}) - введёный email ({0}) должен корректно сохраниться при полчении access токена через восстановление")
    @DisplayName("Восстановление access токена")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldRefreshAccessTokenFromRefreshToken(String email){
        String refreshToken = jwtUtil.generateRefreshToken(email);

        String accessToken = jwtUtil.refreshToken(refreshToken);
        var claims = jwtUtil.getClaims(accessToken).getBody();

        assertEquals(email, claims.getSubject(), "Извлечённый email отличается от введённого");
        logger.info("Проверка email '{}' прошла успешно, извлечённые данные: {}", email, claims.getSubject());

        assertEquals("access", claims.get("type"), "Токен не имеет тип \"access\"");
        logger.info("Проверка типа токена верна! Сгенерированный токен типа '{}' ", claims.get("type"));
    }

    @ParameterizedTest(name = "Работа с некорректным токеном({index}) - введённый токен ({0}) не должен быть принят")
    @DisplayName("Валидация должна вернуть MALFORMED для некорректного токена")
    @ValueSource(strings = {"w", "wrongTOOOKEN", "this.is.not.jwt"})
    public void shouldReturnMalformedForInvalidToken(String badToken) {
        var result = jwtUtil.validateToken(badToken);
        assertEquals(JwtValidationStatus.MALFORMED, result.validationStatus());
        logger.info("Тест успешен. Некорректный токен не был принят");
    }

    @ParameterizedTest(name = "Восстановление access токена({index}) - использование токена с неверным типом должно выбросить исключение")
    @DisplayName("Использование токена неверного типа для восстановления")
    @ValueSource(strings = {"example@email.com", "supermail@mail.net", "newemail@mail.com", "s@gl.com"})
    public void shouldThrowExceptionWhenRefreshAccessToken(String email){
        String accessToken = jwtUtil.generateAccessToken(email);

        assertThrows(IllegalArgumentException.class, () -> jwtUtil.refreshToken(accessToken));
        logger.info("Исключение на попытку восстановить токен используя неверный тип токена выброшено!");
    }

    @Test
    @DisplayName("Дата создания токена должна быть корректной")
    public void shouldTokenCreatedInCorrectDate() {
        String token = jwtUtil.generateRefreshToken("examplemail");
        Date issuedAt = jwtUtil.getClaims(token).getBody().getIssuedAt();
        Date now = new Date();

        long deltaMillis = Math.abs(now.getTime() - issuedAt.getTime());

        assertTrue(deltaMillis < 5000, "Дата создания токена отличается от реальной");
    }
}
