package busking.busking_project.user.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
class RegisterRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // ✨ 1) 성공 케이스
    @Test
    void validDto_shouldPass() {
        RegisterRequestDto dto = valid();      // 헬퍼 메서드
        assertThat(validator.validate(dto)).isEmpty();
    }

    // ✨ 2) username 실패 케이스 (ParameterizedTest)
    @ParameterizedTest
    @ValueSource(strings = {"ab", "가나다라", "long_username_over_20_chars"})
    void invalidUsername_shouldFail(String username) {
        RegisterRequestDto dto = valid();
        dto.setUsername(username);

        Set<ConstraintViolation<RegisterRequestDto>> v = validator.validate(dto);

        assertThat(v)
                .extracting(ConstraintViolation::getPropertyPath)
                .anyMatch(path -> path.toString().equals("username"));
    }

    // ✨ 3) password, email, nickname 각각도 동일 패턴으로 작성 …

    // --- 헬퍼: 항상 통과하는 기본 DTO ---
    private RegisterRequestDto valid() {
        return new RegisterRequestDto(
                "user01",
                "Pa$$word1!",
                "user01@example.com",
                "닉네임"
        );
    }
}
