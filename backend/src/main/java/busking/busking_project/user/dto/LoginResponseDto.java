package busking.busking_project.user.dto;

import busking.busking_project.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto { // 로그인 요청은 단순하기에 @RequestBody Map<String, String>으로 처리했음. 규모가 커질 경우 따로 관리하는 것이 좋음.
    // 로그인 성공 시, 콘솔 창에서 보여지는 토큰 필드임
    private String accessToken;
    private String refreshToken;
    private User user;


}
