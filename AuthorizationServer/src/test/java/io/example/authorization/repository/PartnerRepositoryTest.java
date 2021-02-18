package io.example.authorization.repository;

import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.domain.entity.partner.PartnerRole;
import io.example.authorization.domain.entity.partner.PartnerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@DataJpaTest
//@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Partner Repository")
class PartnerRepositoryTest {

    @Autowired
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("파트너 엔티티 생성")
    public void createPartnerEntity(){
        // given
        String partnerId = "user";
        String partnerPassword = "password";
        String partnerEmail = "project.log.063@gmail.com";
        String partnerCompanyName = "naver";
        Set<PartnerRole> partnerRoles = Collections.singleton(PartnerRole.FORBIDDEN);
        PartnerStatus partnerStatus = PartnerStatus.DRAFT;

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .partnerRoles(partnerRoles)
                .partnerStatus(partnerStatus)
                .build();

        // when
        PartnerEntity savedPartnerEntity = this.partnerRepository.save(partnerEntity);

        // then
        assertThat(savedPartnerEntity).isNotNull();
        assertThat(savedPartnerEntity.getPartnerId()).isEqualTo(partnerId);
        assertThat(savedPartnerEntity.getPartnerPassword()).isEqualTo(partnerPassword);
        assertThat(savedPartnerEntity.getPartnerEmail()).isEqualTo(partnerEmail);
        assertThat(savedPartnerEntity.getPartnerCompanyName()).isEqualTo(partnerCompanyName);
        assertThat(savedPartnerEntity.getPartnerRoles()).isEqualTo(partnerRoles);
        assertThat(savedPartnerEntity.getPartnerStatus()).isEqualTo(partnerStatus);

//        try {
//            PartnerEntity savedPartnerEntity2 = this.partnerRepository.save(partnerEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Test
    @DisplayName("파트너 엔티티 조회")
    public void readPartnerEntity(){
        // given
        long partnerNo = 1L;

        // when
        Optional<PartnerEntity> optionalPartnerEntity = this.partnerRepository.findById(partnerNo);

        // then
        if(!optionalPartnerEntity.isEmpty()){
            PartnerEntity partnerEntity = optionalPartnerEntity.get();
            assertThat(partnerEntity.getPartnerNo()).isEqualTo(partnerNo);
            assertThat(partnerEntity.getPartnerId()).isNotNull();
            assertThat(partnerEntity.getPartnerPassword()).isNotNull();
            assertThat(partnerEntity.getPartnerEmail()).isNotNull();
            assertThat(partnerEntity.getPartnerRoles()).isNotNull();
            assertThat(partnerEntity.getPartnerStatus()).isNotNull();
        }else {
            new RuntimeException("not found");
        }
    }
}