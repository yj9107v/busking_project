package busking.busking_project;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "ID는 영문, 숫자, 언더스코어 포함 5~20자")
    private String username;


    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자"
    )
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,30}$", message = "닉네임은 2~30자, 한글/영문/숫자만 허용")
    private String nickname;
}

