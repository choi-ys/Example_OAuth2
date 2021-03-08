package io.example.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.dto.request.partner.IssueClient;
import io.example.authorization.domain.entity.partner.PartnerStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.example.authorization.constants.MediaTypes.HAL_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Partner API")
class PartnerControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("파트너 계정 생성 API")
    public void createPartner() throws Exception {
        // Given
        CreatePartner createPartner = partnerGenerator.createPartner();

        // When
        String urlTemplate = "/api/partner";
        ResultActions createPartnerResultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsBytes(createPartner))
        );

        // Then
        createPartnerResultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().stringValues(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.partnerId").value(createPartner.getPartnerId()))
                .andExpect(jsonPath("data.partnerEmail").value(createPartner.getPartnerEmail()))
                .andExpect(jsonPath("data.partnerStatus").value(PartnerStatus.API_NOT_AVAILABLE.name()))
        ;
    }

    @Test
    @DisplayName("파트너 계정 생성 API : 값이 없는 요청")
    public void createPartnerAPI_EmptyParamRequest() throws Exception {
        //given
        CreatePartner createPartner = new CreatePartner();

        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createPartner))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createPartner"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").doesNotExist())
        ;
    }

    @Test
    @DisplayName("파트너 계정 생성 API : 유효하지 못한 값의 요청")
    public void createPartnerAPI_WrongParamRequest() throws Exception {
        //given
        String partnerId = "a";
        String partnerPassword = "b";
        String partnerEmail = "c";
        String partnerCompanyName = "d";

        CreatePartner createPartner = new CreatePartner();
        createPartner.setPartnerId(partnerId);
        createPartner.setPartnerPassword(partnerPassword);
        createPartner.setPartnerEmail(partnerEmail);
        createPartner.setPartnerCompanyName(partnerCompanyName);

        //when
        String urlTemplate = "/api/partner";
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createPartner))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createPartner"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
        ;
    }

    @Test
    @DisplayName("생성된 파트너 계정에 클라이언트 발급 API")
    public void issueClient() throws Exception {
        // Given
        CreatePartner createPartner = partnerGenerator.createPartner();

        // When
        String urlTemplate = "/api/partner";
        ResultActions createPartnerResultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsBytes(createPartner))
        );

        // Then
        createPartnerResultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().stringValues(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.partnerId").value(createPartner.getPartnerId()))
                .andExpect(jsonPath("data.partnerEmail").value(createPartner.getPartnerEmail()))
                .andExpect(jsonPath("data.partnerStatus").value(PartnerStatus.API_NOT_AVAILABLE.name()))
        ;

        // Given : 생성된 파트너 계정 정보에서 partnerNo 추출
        String createdPartnerResponse = createPartnerResultActions.andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(createdPartnerResponse);
        JSONObject createdPartner = (JSONObject) jsonObject.get("data");
        long createdPartnerNo = Long.parseLong(createdPartner.get("partnerNo").toString());

        String resourceIds = "CloudM";

        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(createdPartnerNo);
        issueClient.setResourceIds(resourceIds);

        String issueClientUrlTemplate = "/api/partner/client";

        // When : 생성된 파트너 계정에 클라이언트 발급 요청
        ResultActions issueClientResultActions = this.mockMvc.perform(post(issueClientUrlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsBytes(issueClient))
        );

        // Then : 클라이언트 발급 여부 확인
        issueClientResultActions.andDo(print())
                .andExpect(status().isCreated())
        ;
    }
    
    @Test
    @DisplayName("존재하지 않는 파트너 계정에 클라이언트 발급 API")
    public void issueClient_NotExistsPartner() throws Exception {
        // Given
        long partnerNo = 1L;
        String resourceIds = "CloudM";

        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(partnerNo);
        issueClient.setResourceIds(resourceIds);

        String urlTemplate = "/api/partner/client";

        // When
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsBytes(issueClient))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is5xxServerError())
        ;

    }
}