package io.github.zapolyarnydev.authservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.commons.api.ApiStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-tests")
@Testcontainers
@DirtiesContext
@AutoConfigureMockMvc
@Tag("integration")
@DisplayName("Auth: user registration")
public class RegistrationTest {

    @Container
    private static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.8.0")
            .asCompatibleSubstituteFor("apache/kafka")
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder encoder;

    @DynamicPropertySource
    private static void kafkaProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @AfterEach
    public void cleanDatabase(){
        jdbcTemplate.execute("DELETE FROM auth_users");
    }

    @ParameterizedTest(name = "{index} User registration. Email: {0}, Password: {1}")
    @DisplayName("User registration with the correct details")
    @MethodSource("correctRegisterDetailsArguments")
    public void shouldRegisterUserWithCorrectDetails(String email, String password) throws Exception {
        String request = createRegistrationRequest(email, password);

        MvcResult result = mockMvc.perform(post("/v0/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(ApiStatus.SUCCESS);
        assertThat(response.data()).isNotEmpty();

        assertThat(authUserRepository.existsByEmail(email)).isTrue();

        AuthUser user = authUserRepository.findByEmail(email).get();

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(encoder.matches(password, user.getPassword())).isTrue();
    }

    private static Stream<Arguments> correctRegisterDetailsArguments() {
        return Stream.of(
                Arguments.of("mail1@mail.com", "qwerty12345"),
                Arguments.of("m@mail.com", "qwerty12"),
                Arguments.of("mail2@mail.com", "cdasdasfasfasdasdasasdsa")
        );
    }

    private String createRegistrationRequest(String email, String password) {
        return String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password);
    }
}
