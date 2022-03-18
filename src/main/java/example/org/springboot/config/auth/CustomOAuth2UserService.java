package example.org.springboot.config.auth;


import example.org.springboot.config.auth.dto.OAuthAttributes;
import example.org.springboot.config.auth.dto.SessionUser;
import example.org.springboot.domain.user.User;
import example.org.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //현재 로그인 진행중인 서비스 구분하는 코드
                                                    // 이후 여러 서비스 등록 시 네이버인지 구글인지 등 서비스 구분
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); //userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드 값(=Primary Key)
                                        //이후 네이버 로그인, 구글 로그인 동시 지원할 때 사용됨
                                        // 구글의 경우 기본적인 코드 지원(sub) , 네이버/카카오는 기본 지원X

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
                    //OAuthAttributes: OAuth2UserService 를 통해 가져온 OAuth2User의 attribute를 담을 클래스(다른 소셜 로그인도 이 클래스 사용)

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); //SessionUser: 세션에 사용자 정보를 저장하기 위한 Dto 클래스

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    //구글 사용자 정보 업데이트를 대비한 update 기능
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user); // -> User 엔티티에 반영
    }
}
