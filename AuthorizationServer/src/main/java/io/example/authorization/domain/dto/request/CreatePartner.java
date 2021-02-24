package io.example.authorization.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePartner {

    private String partnerId;

    private String partnerPassword;

    private String partnerEmail;

    private String partnerCompanyName;
}