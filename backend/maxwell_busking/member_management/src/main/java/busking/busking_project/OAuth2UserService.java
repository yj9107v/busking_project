package busking.busking_project;

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

    private final userRepository userRepository;

    public OAuth2UserService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email;
        String socialId;
        AuthProvider provider;
        String nickname;

        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            socialId = (String) attributes.get("sub");
            nickname = "구글_" + socialId;
            provider = AuthProvider.GOOGLE;
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            socialId = String.valueOf(attributes.get("id"));
            nickname = "카카오_" + socialId;
            provider = AuthProvider.KAKAO;
        } else {
            socialId = null;
            provider = null;
            nickname = null;
            email = null;
        }

        if (email == null || socialId == null)
            throw new RuntimeException("이메일 또는 소셜 ID를 가져올 수 없습니다.");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            return userRepository.save(User.builder()
                    .username(email)
                    .email(email)
                    .password(null)
                    .nickname(nickname)
                    .provider(provider)
                    .socialId(socialId)
                    .role(Role.USER)
                    .isDeleted(false)
                    .build());
        });

        Map<String, Object> customAttr = new HashMap<>(attributes);
        customAttr.put("email", user.getEmail());
        customAttr.put("nickname", user.getNickname());
        customAttr.put("provider", user.getProvider().toString());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttr,
                "email"
        );
    }
}
