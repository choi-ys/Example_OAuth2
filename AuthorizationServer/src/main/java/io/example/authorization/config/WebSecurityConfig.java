package io.example.authorization.config;

import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Http 요청에 대한 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable() // 토큰 발급 시 요청 Method인 POST 요청 임시 허용 설정 추가
                .authorizeRequests()
                    .antMatchers("/oauth/**", "/client/callback", "/h2-console/*", "/api/partner/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    private final PartnerService partnerService;
    private final PasswordEncoder passwordEncoder;

    // DB의 사용자 정보를 이용한 인증 정보 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(partnerService)
                .passwordEncoder(passwordEncoder)
        ;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}