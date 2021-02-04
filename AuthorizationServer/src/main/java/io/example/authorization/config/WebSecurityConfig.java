package io.example.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
                .httpBasic();
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

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}