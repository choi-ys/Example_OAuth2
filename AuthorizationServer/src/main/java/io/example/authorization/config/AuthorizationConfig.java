package io.example.authorization.config;

import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    // WebSecurityConfig.java의 AuthenticationManagerBuilder를 통해 AuthenticationManager에 등록된 인증된 사용자 정보
    private final AuthenticationManager authenticationManager;

    // application.yml에 등록된 database 설정 및 접속 정보
    private final DataSource dataSource;

    private final PartnerService partnerService;

    // 인증 Token 발급/갱신에 필요한 Clinet정보를 코드로 설정
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(partnerService);
    }

    /**
     * 인증 토큰 발급/갱신 요청 처리 부
     *  - 인증 토큰 발급 시 필요한 인증된 사용자 정보 등록
     *  - 발급 토큰 정보 관리하는 TokenStore에 Datasource 정보 등록
     *  endpoint URL : AuthorizationServerSecurityConfiguration
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // Account 인증 정보를 소유한 Bean
                .tokenStore(new JdbcTokenStore(dataSource)) // application에 명시된 dataSource를 tokenStore로 설정
                .userDetailsService(partnerService) // Refresh Token을 이용한 Access Token갱신 시 인증된 사용자 여부 검사를 위한 UserDetailsService 구현체 설정
                .reuseRefreshTokens(false) //  Refresh Token을 이용한 Access Token갱신 시 Refresh Token 만료 처리 설정
        ;
    }
}