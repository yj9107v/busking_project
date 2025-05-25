package busking.busking_project.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Spring Security 기본 구현을 통해 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google" 또는 "kakao"

        String email;
        String socialId;
        AuthProvider provider;
        String nickname;

        // ✅ 구글 로그인 처리
        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");        // 이메일 추출
            socialId = (String) attributes.get("sub");       // 구글의 고유 사용자 ID
            nickname = "구글_" + socialId;                   // 자동 닉네임 생성
            provider = AuthProvider.GOOGLE;
        }
        // ✅ 카카오 로그인 처리
        else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");      // 이메일 추출
            socialId = String.valueOf(attributes.get("id")); // 카카오 고유 ID
            nickname = "카카오_" + socialId;
            provider = AuthProvider.KAKAO;
        }
        // ✅ 지원하지 않는 로그인 공급자
        else {
            throw new RuntimeException("지원하지 않는 소셜 로그인입니다.");
        }

        // 필수 정보가 없으면 오류 발생
        if (email == null || socialId == null) {
            throw new RuntimeException("이메일 또는 소셜 ID를 가져올 수 없습니다.");
        }

        // ✅ 기존 사용자 찾기 또는 새 사용자 저장
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            return userRepository.save(User.builder()
                    .username(email)
                    .email(email)
                    .password(null)          // 비밀번호 없음 (소셜 로그인)
                    .nickname(nickname)
                    .provider(provider)
                    .socialId(socialId)
                    .role(Role.USER)
                    .isDeleted(false)
                    .build());
        });

        // ✅ 사용자 ID와 기타 정보를 속성에 추가
        Map<String, Object> customAttr = new HashMap<>(attributes);
        customAttr.put("userId", user.getId().toString());
        customAttr.put("email", user.getEmail());
        customAttr.put("nickname", user.getNickname());
        customAttr.put("provider", user.getProvider().toString());

        // ✅ 사용자 정보를 포함한 OAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                customAttr,   // 사용자 정보 (속성)
                "userId"      // principal 속성 키 (고유 식별자 역할)
        );
    }
}

