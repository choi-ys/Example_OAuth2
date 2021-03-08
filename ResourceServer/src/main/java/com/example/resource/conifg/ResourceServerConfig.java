package com.example.resource.conifg;

//import com.example.resource.service.CustomClientDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/api/index", "/api/resource/any").permitAll()
                .anyRequest().access("#oauth2.hasScope('read') and hasRole('ROLE_STARTER')")
//                .anyRequest().access("#oauth2.hasScope('read') and hasRole('ROLE_ALLIANCET')")
                ;
    }

//    private final TokenStore tokenStore;
//    private final CustomClientDetailsService customClientDetailsService;

//    @Bean
//    @Qualifier
//    public ResourceServerTokenServices tokenService(){
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setClientDetailsService(customClientDetailsService);
//        return defaultTokenServices;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
//        authenticationManager.setTokenServices(tokenService());
//        return authenticationManager;
//    }

//    @Bean
//    public ResourceServerTokenServices resourceServerTokenServices(){
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        TokenStore tokenStore = new JdbcTokenStore(dataSource);
//        defaultTokenServices.setTokenStore(tokenStore);
//        return defaultTokenServices;
//    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//        TokenStore tokenStore = new JdbcTokenStore(dataSource);
//        DefaultTokenServices tokenServices = new DefaultTokenServices();

//        resources.resourceId("CloudM").tokenStore(tokenStore()).tokenServices(resourceServerTokenServices());

//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setClientDetailsService(customClientDetailsService);
//        resources.resourceId("CloudM").tokenServices(defaultTokenServices);

        resources.resourceId("CloudM").stateless(false);

//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setClientDetailsService(customClientDetailsService);
//
//        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
//        authenticationManager.setTokenServices(defaultTokenServices);
//
//        resources.resourceId("resource-server-rest-api")
//                .authenticationManager(authenticationManager)
////                .tokenExtractor(tokenExtractor);
//        ;
    }
}