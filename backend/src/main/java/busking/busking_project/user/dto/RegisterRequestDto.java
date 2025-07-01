package busking.busking_project.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    // 회원가입 시 클라이언트에서 전달되는 데이터를 받기 위한 DTO
    // 입력값에 대해 @Valid 유효성 검사 적용

    @Pattern(
            regexp = "^[a-zA-Z0-9_]{5,20}$",
            message = "아이디는 5~20자, 영문 + 숫자 + 밑줄(_)만 허용됩니다."
    )
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;
    // - 필수 입력 (@NotBlank)
    // - 정규식 검증: 영문자, 숫자, 밑줄(_)만 허용, 5~20자 제한

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 8~20자, 영문 + 숫자 + 특수문자만 허용됩니다."
    )
    private String password;
    // - 정규식 검증: 영문자 1개 이상, 숫자 1개 이상, 특수문자 1개 이상 포함 필수
    // - 총 길이는 8~20자
    // - @NotBlank 생략: null/빈문자열로 들어오면 검증 실패

    @Email(message = "유효한 이메일 형식이 아닙니다. ex) example@google.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;
    // - 이메일 형식(@) 포함 여부 자동 검증
    // - 빈 문자열 방지

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,30}$", message = "닉네임은 2~30자, 한글, 영문, 숫자만 허용됩니다.")
    private String nickname;
    // - 필수 입력
    // - 한글, 영문, 숫자만 허용
    // - 2~30자 제한

    @Builder.Default private boolean social = false;
}
