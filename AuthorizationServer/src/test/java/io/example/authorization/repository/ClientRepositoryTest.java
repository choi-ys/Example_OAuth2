package io.example.authorization.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.entity.partner.ClientEntity;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.entity.partner.PartnerRole;
import io.example.authorization.generator.PartnerGenerator;
import io.example.authorization.service.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/03/02 5:03 오후
 * @Content : ClientRepository를 이용한 ClientEntity TEST
 */
@Slf4j
class ClientRepositoryTest extends BaseTest {

    @Resource PartnerGenerator partnerGenerator;
    @Autowired PartnerService partnerService;
    @Resource ClientRepository clientRepository;

    @Test
    @DisplayName("[Create]특정 사용자의 클라이언트 정보 생성")
    @Transactional
    public void createClientEntity() throws JsonProcessingException {
        // Given : 클라이언트 정보를 발급할 PartnerEntity 생성
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        ProcessingResult savePartnerProcessingResult = this.partnerService.savePartner(createPartner);
        assertThat(savePartnerProcessingResult.isSuccess()).isTrue();
        PartnerEntity savedPartnerEntity = (PartnerEntity) savePartnerProcessingResult.getData();

        // Given : 특정회원에 클라이언트 정보 발급
        ClientEntity clientEntity = this.buildClientEntity();
        clientEntity.setPublishedClientInfo(savedPartnerEntity);

        // When : 발급된 클라이언트 정보 저장
        ClientEntity savedClientEntity = this.clientRepository.save(clientEntity);

        // Then : 입력값과 초기값 설정이 필요한 항목의 초기화 여부 확인
        assertThat(savedClientEntity.getClientId()).isEqualTo(clientEntity.getClientId());
        assertThat(savedClientEntity.getClientSecret()).isEqualTo(clientEntity.getClientSecret());
        assertThat(savedClientEntity.getResourceIds()).isEqualTo(clientEntity.getResourceIds());
        assertThat(savedClientEntity.getScope()).isEqualTo(clientEntity.getScope());
        assertThat(savedClientEntity.getAuthorizedGrantTypes()).isEqualTo(clientEntity.getAuthorizedGrantTypes());
        assertThat(savedClientEntity.getAuthorities()).isEqualTo(clientEntity.getAuthorities());
        assertThat(savedClientEntity.getAccessTokenValidity()).isEqualTo(clientEntity.getAccessTokenValidity());
        assertThat(savedClientEntity.getRefreshTokenValidity()).isEqualTo(clientEntity.getRefreshTokenValidity());
        assertThat(savedClientEntity.getPartnerEntity()).isEqualTo(savedPartnerEntity);
    }

    @Test
    @DisplayName("[Create]특정 사용자 정보가 없는 클라리언트 정보 생성")
    public void createClientEntity_NoExistsPartnerRequest(){
        // Given
        ClientEntity clientEntity = this.buildClientEntity();

        // When
        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> clientRepository.save(clientEntity));

        System.out.println("Exception : " + exception.getClass().getName());
        System.out.println("getMessage() : " + exception.getMessage());
        exception.printStackTrace();
    }

    @Disabled
    private ClientEntity buildClientEntity(){
        String clientId = UUID.randomUUID().toString();
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String clientSecret = passwordEncoder.encode(clientId);
        String resourceIds = "naver";

        String scope = "read";
        String authorizedGrantTypes = "password,refresh_token";
        String authorities = PartnerRole.STARTER.name();
        int accessTokenValidity = 43200; // 12시간
        int refreshTokenValidity = 86400; // 24시간

        ClientEntity clientEntity = ClientEntity.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resourceIds(resourceIds)
                .scope(scope)
                .authorizedGrantTypes(authorizedGrantTypes)
                .authorities(authorities)
                .accessTokenValidity(accessTokenValidity)
                .refreshTokenValidity(refreshTokenValidity)
                .build();

        return clientEntity;
    }
}