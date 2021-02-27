package io.example.authorization.domain.partner.entity;

import io.example.authorization.domain.dto.request.CreatePartner;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain:Partner")
class PartnerEntityTest {

    @Test
    @DisplayName("PartnerSingUp -> PartnerEntity : ModelMapper Mapping")
    public void modelMapperTest(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        CreatePartner createPartner = new CreatePartner();
        createPartner.setPartnerId(partnerId);
        createPartner.setPartnerPassword(partnerPassword);
        createPartner.setPartnerEmail(partnerEmail);
        createPartner.setPartnerCompanyName(partnerCompanyName);

        ModelMapper modelMapper = new ModelMapper();

        //when
        PartnerEntity partnerEntity = modelMapper.map(createPartner, PartnerEntity.class);

        //then
        assertThat(partnerEntity.getPartnerId()).isEqualTo(partnerId);
        assertThat(partnerEntity.getPartnerPassword()).isEqualTo(partnerPassword);
        assertThat(partnerEntity.getPartnerEmail()).isEqualTo(partnerEmail);
        assertThat(partnerEntity.getPartnerCompanyName()).isEqualTo(partnerCompanyName);
    }
}