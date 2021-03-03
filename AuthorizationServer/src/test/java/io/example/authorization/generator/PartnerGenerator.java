package io.example.authorization.generator;

import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import io.example.authorization.repository.PartnerRepository;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : choi-ys
 * @date : 2021-02-27 오후 11:58
 * @Content : Partner 관련 Test 시 필요한 객체 생성 부
 */
@Disabled
public class PartnerGenerator {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PartnerRepository partnerRepository;

    public CreatePartner createPartner(){
        String partnerId = "naver";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        CreatePartner createPartner = new CreatePartner();
        createPartner.setPartnerId(partnerId);
        createPartner.setPartnerPassword(partnerPassword);
        createPartner.setPartnerEmail(partnerEmail);
        createPartner.setPartnerCompanyName(partnerCompanyName);;

        return createPartner;
    }

    public PartnerEntity buildSignUpPartnerEntity(){
        CreatePartner createPartner = this.createPartner();
        PartnerEntity partnerEntity = this.modelMapper.map(createPartner, PartnerEntity.class);
        partnerEntity.setPartnerPassword(this.passwordEncoder.encode(partnerEntity.getPartnerPassword()));
        partnerEntity.signUp();
        return partnerEntity;
    }

    public PartnerEntity savedPartnerEntity(){
        return partnerRepository.save(this.buildSignUpPartnerEntity());
    }

}