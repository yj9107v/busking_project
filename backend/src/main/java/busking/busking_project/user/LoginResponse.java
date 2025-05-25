package busking.busking_project.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    // 로그인 성공 시, 콘솔 창에서 보여지는 토큰 필드임
    private String accessToken;
    private String refreshToken;
    private User user;


}
