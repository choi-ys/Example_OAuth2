package io.example.authorization.service;

import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.entity.partner.PartnerRole;
import io.example.authorization.domain.entity.partner.PartnerStatus;
import io.example.authorization.domain.response.common.ProcessingResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021/02/19 12:20 오후
 * @Content : 사용자 계정 관련 TC
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Partner Service Test")
@ActiveProfiles("test")
class PartnerServiceTest {

    @Autowired PartnerService partnerService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("신규 사용자 생성")
    void savePartner() {
        // given
        String partnerId = "user";
        String partnerPassword = "password";
        String partnerEmail = "project.log.065@gmail.com";
        String partnerCompanyName = "naver";

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        // when
        ProcessingResult processingResult = this.partnerService.savePartner(partnerEntity);
        PartnerEntity savedPartnerEntity = (PartnerEntity) processingResult.getData();

        // then
        assertThat(processingResult.isSuccess()).isTrue();
        assertThat(processingResult.getError()).isNull();
        assertThat(savedPartnerEntity.getPartnerId()).isEqualTo(partnerId);
        assertThat(passwordEncoder.matches(partnerPassword, savedPartnerEntity.getPartnerPassword())).isTrue();
        assertThat(savedPartnerEntity.getPartnerEmail()).isEqualTo(partnerEmail);
        assertThat(savedPartnerEntity.getPartnerCompanyName()).isEqualTo(partnerCompanyName);

        assertThat(savedPartnerEntity.getPartnerRoles()).isEqualTo(Collections.singleton(PartnerRole.FORBIDDEN));
        assertThat(savedPartnerEntity.getPartnerStatus()).isEqualTo(PartnerStatus.API_NOT_AVAILABLE);
    }
}
