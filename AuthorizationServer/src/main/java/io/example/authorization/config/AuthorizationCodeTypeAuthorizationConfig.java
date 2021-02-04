package io.example.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationCodeTypeAuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * Request URI : /oauth/authorize?response_type=code&redirect_uri={client_server_redirect_uri}&client_id={client_id}
     * Example URI : http://localhost:8080/oauth/authorize?client_id=testClientId&response_type=code&redirect_uri=http://localhost:8080/oauth/callback
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("testClientId")
                .secret("{noop}testClientSecret")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris("http://localhost:8080/client/callback")
                .scopes("read")
                .accessTokenValiditySeconds(60 * 10)
                .additionalInformation("additionalInformation")
        ;
    }
}