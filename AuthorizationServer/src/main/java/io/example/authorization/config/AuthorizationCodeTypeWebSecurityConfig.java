package io.example.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class AuthorizationCodeTypeWebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Http 요청에 대한 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable() // 토큰 발급 시 요청 Method인 POST 요청 임시 허용 설정 추가
                .authorizeRequests()
                    .antMatchers("/oauth/**", "/client/callback", "/h2-console/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest httpServletRequest
                                , HttpServletResponse httpServletResponse
                                , Authentication authentication) throws IOException, ServletException {
                            httpServletResponse.sendRedirect("/oauth/authorize?client_id=testClientId&response_type=code&redirect_uri=http://localhost:8080/client/callback"); // 로그인 성공 이후 response 객체 접근 가능
                        }
                    })
        ;
    }

    // InMemory환경에서 Token발급 대상인 사용자 정보 설정 추가
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("testUser")
                .password("{noop}password")
                .roles("USER")
        ;
    }
}