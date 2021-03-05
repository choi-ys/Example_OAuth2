package io.example.authorization.domain.dto.response.partner;

import io.example.authorization.domain.entity.partner.ClientEntity;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * @author : choi-ys
 * @date : 2021/03/05 9:30 오전
 * @Content : 서비스에서 클라이언트 정보를 표현하는 ClientEntity객체를 Spring Security의 ClientDetails 객체로 변환
 */
public class ClientAdapter extends BaseClientDetails{

    public ClientAdapter(ClientEntity clientEntity) {
        super(
                clientEntity.getClientId()
                , clientEntity.getResourceIds()
                , clientEntity.getScope()
                , clientEntity.getAuthorizedGrantTypes()
                , clientEntity.getAuthorities()
        );
        setClientSecret(clientEntity.getClientSecret());
    }
}