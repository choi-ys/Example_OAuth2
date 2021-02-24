package io.example.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.domain.dto.request.CreatePartner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.example.authorization.constants.MediaTypes.HAL_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Partner API")
class PartnerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("파트너 계정 생성 API")
    public void createPartner() throws Exception {
        // Given
        String partnerId = "project062";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "주식회사 네이버";

        CreatePartner createPartner = new CreatePartner();
        createPartner.setPartnerId(partnerId);
        createPartner.setPartnerPassword(partnerPassword);
        createPartner.setPartnerEmail(partnerEmail);
        createPartner.setPartnerCompanyName(partnerCompanyName);

        // When
        String urlTemplate = "/api/partner";
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsBytes(createPartner))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
        ;
    }
}