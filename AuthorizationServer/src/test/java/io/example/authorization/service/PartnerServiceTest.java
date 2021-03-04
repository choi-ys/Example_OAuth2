package io.example.authorization.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.dto.request.partner.IssueClient;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.entity.partner.ClientEntity;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.entity.partner.PartnerRole;
import io.example.authorization.domain.entity.partner.PartnerStatus;
import io.example.authorization.generator.PartnerGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.annotation.Resource;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/02/19 12:20 오후
 * @Content : 사용자 계정 관련 TC
 */
@DisplayName("Partner Service Test")
class PartnerServiceTest extends BaseTest {

    @Resource
    protected  PartnerGenerator partnerGenerator;

    @Autowired PartnerService partnerService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("신규 사용자 생성")
    void savePartner() {
        // given
        CreatePartner createPartner = this.partnerGenerator.createPartner();

        // when
        ProcessingResult processingResult = this.partnerService.savePartner(createPartner);
        PartnerEntity savedPartnerEntity = (PartnerEntity) processingResult.getData();

        // then
        assertThat(processingResult.isSuccess()).isTrue();
        assertThat(processingResult.getError()).isNull();
        assertThat(savedPartnerEntity.getPartnerId()).isEqualTo(createPartner.getPartnerId());
        assertThat(passwordEncoder.matches(createPartner.getPartnerPassword(), savedPartnerEntity.getPartnerPassword())).isTrue();
        assertThat(savedPartnerEntity.getPartnerEmail()).isEqualTo(createPartner.getPartnerEmail());
        assertThat(savedPartnerEntity.getPartnerCompanyName()).isEqualTo(createPartner.getPartnerCompanyName());

        assertThat(savedPartnerEntity.getPartnerRoles()).isEqualTo(Collections.singleton(PartnerRole.FORBIDDEN));
        assertThat(savedPartnerEntity.getPartnerStatus()).isEqualTo(PartnerStatus.API_NOT_AVAILABLE);
    }

    @Test
    @DisplayName("UserDetailSevice의 loadUserByUsername 이용한 User 정보 조회")
    public void loadUserByUsername(){
        // Given
        PartnerEntity savedPartnerEntity = this.partnerGenerator.savedPartnerEntity();
        String savedPartnerId = savedPartnerEntity.getPartnerId();

        // When
        UserDetails userDetails = this.partnerService.loadUserByUsername(savedPartnerId);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(savedPartnerId);
        assertThat(userDetails.getPassword()).isEqualTo(savedPartnerEntity.getPartnerPassword());
    }


    @Test
    @DisplayName("DB에 존재하는 사용자 정보에 Client 정보 생성")
    public void setClientInfoToPartner() {
        // Given
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        ProcessingResult createPartnerProcessingResult = this.partnerService.savePartner(createPartner);
        assertThat(createPartnerProcessingResult.isSuccess()).isTrue();

        // Given
        PartnerEntity savedPartnerEntity = (PartnerEntity) createPartnerProcessingResult.getData();
        String resourceIds = "NAVER";

        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(savedPartnerEntity.getPartnerNo());
        issueClient.setResourceIds(resourceIds);

        // When
        ProcessingResult createClientPartnerProcessingResult = partnerService.saveClient(issueClient);

        // Then
        assertThat(createClientPartnerProcessingResult.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ClientDetailService의 loadClientByClientId 이용한 Client 정보 조회")
    public void loadClientByClientId() throws JsonProcessingException {
        // Given : 클라이언트 정보를 발급할 사용자 정보 생성
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        ProcessingResult createPartnerProcessingResult = this.partnerService.savePartner(createPartner);
        assertThat(createPartnerProcessingResult.isSuccess()).isTrue();

        // Given : 생성된 사용자 정보에 클라이언트 정보 발급 요청
        PartnerEntity savedPartnerEntity = (PartnerEntity) createPartnerProcessingResult.getData();
        String resourceIds = "NAVER";

        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(savedPartnerEntity.getPartnerNo());
        issueClient.setResourceIds(resourceIds);

        ProcessingResult createClientPartnerProcessingResult = partnerService.saveClient(issueClient);
        assertThat(createClientPartnerProcessingResult.isSuccess()).isTrue();

        // Then : Spring Security의 loadClientByClientId를 이용한 ClientDetails 타입의 클라이언트 정보 조회
        ClientEntity savedClientEntity = (ClientEntity) createClientPartnerProcessingResult.getData();
        ClientDetails clientDetails = partnerService.loadClientByClientId(savedClientEntity.getClientId());

        assertThat(clientDetails.getClientId()).isEqualTo(savedClientEntity.getClientId());
        assertThat(clientDetails.getClientSecret()).isEqualTo(savedClientEntity.getClientSecret());
    }
}
