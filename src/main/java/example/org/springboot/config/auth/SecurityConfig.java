package example.org.springboot.config.auth;

import example.org.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정들 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당 옵션들을 disable
                .and()
                .authorizeRequests() //URL 별 권한 관리를 설정하는 옵션의 시작점
                                    //authorizeRequests 가 선언되어야만 anMatchers 옵션을 사용할 수 있음
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                                            // 지정된 URL 들은 permitAll 옵션으로 전체 열람 권한
                                            //권한 관리 대상을 지정하는 옵션 / URL, HTTP 메소드 별로 관리가 가능
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //해당 주소를 가진 API는 USER 권한을 가진 사람만 가능
                .anyRequest().authenticated() //anyRequest: 설정된 값들 이외 나머지 URL
                                    // authenticated 를 추가하여 나머지 URL들을 모두 인증된 사용자에게만 허용
                .and()
                .logout().logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점 , 로그아웃 시 /주소로 이동
                .and()
                .oauth2Login() //OAuth2 로그인 기능에 대한 여러 설정 진입점
                .userInfoEndpoint() //OAuth2 로그인 성공 시 싸용자 정보를 가져올 때의 설정들 담당
                .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
                                                //리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고ㅕ 하는 기능 명시할 수 있음
    }
}
