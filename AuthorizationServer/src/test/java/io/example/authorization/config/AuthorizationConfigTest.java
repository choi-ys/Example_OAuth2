package io.example.authorization.config;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.dto.request.partner.CreatePartner;
import io.example.authorization.domain.dto.request.partner.IssueClient;
import io.example.authorization.domain.dto.response.common.ProcessingResult;
import io.example.authorization.domain.entity.partner.ClientEntity;
import io.example.authorization.domain.entity.partner.PartnerEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Token API")
@Slf4j
class AuthorizationConfigTest extends BaseTest{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증 토큰 발급 : InMemory환경의 사용자 정보와 클라이언트 정보를 이용한 Password Type의 인증 토큰 발급")
    @Disabled
    public void issuedAccessTokenToInMemoryUser() throws Exception {
        //given
        String urlTemplate = "/oauth/token";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String username = "user";
        String password = "password";

        //when
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("expires_in").exists())
                .andExpect(jsonPath("scope").exists())
        ;
    }

    @Test
    @DisplayName("인증 토큰 발급 : DB의 인증된 사용자 정보와 InMemory의 클라이언트 정보를 이용한 Token 발급")
    @Disabled
    public void issuedAccessTokenToDatabaseUser() throws Exception {
        //given
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        PartnerEntity partnerEntity = this.modelMapper.map(createPartner, PartnerEntity.class);
        partnerEntity.setPartnerPassword(this.passwordEncoder.encode(partnerEntity.getPartnerPassword()));
        partnerEntity.signUp();
        PartnerEntity savedPartnerEntity = this.partnerRepository.save(partnerEntity);

        System.out.println(savedPartnerEntity.getPartnerPassword());

        String urlTemplate = "/oauth/token";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String username = createPartner.getPartnerId();
        String password = createPartner.getPartnerPassword();

        //when
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("expires_in").exists())
                .andExpect(jsonPath("scope").exists())
        ;
    }

    @Test
    @DisplayName("인증 토큰 발급 : DB의 인증된 사용자 정보와 클라이언트 정보를 이용한 Token 발급")
    public void issuedAccessTokenToDatabaseUserWithDatabaseClient() throws Exception {
        // Given : 토큰 발급 대상 사용자 정보 생성
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        ProcessingResult createPartnerProcessingResult = this.partnerService.savePartner(createPartner);
        assertThat(createPartnerProcessingResult.isSuccess()).isTrue();

        // Given : 생성된 사용자에 클라이언트 정보 발급
        PartnerEntity savedPartnerEntity = (PartnerEntity) createPartnerProcessingResult.getData();
        String resourceIds = "NAVER";
        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(savedPartnerEntity.getPartnerNo());
        issueClient.setResourceIds(resourceIds);

        ProcessingResult issueClientProcessingResult = this.partnerService.saveClient(issueClient);
        assertThat(issueClientProcessingResult.isSuccess()).isTrue();

        // Given : 토큰 발급 요청에 필요한 사용자 정보와 클라이언트 정보 설정
        ClientEntity clientEntity = (ClientEntity) issueClientProcessingResult.getData();

        String urlTemplate = "/oauth/token";
        String clientId = clientEntity.getClientId();
        String clientSecret = clientEntity.getClientId();
        String username = createPartner.getPartnerId();
        String password = createPartner.getPartnerPassword();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("expires_in").exists())
                .andExpect(jsonPath("scope").exists())
        ;

        // Given : 발급된 인증토큰 정보에서 access_token 추출
        String authTokenInfo = resultActions.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String accessToken = jackson2JsonParser.parseMap(authTokenInfo).get("access_token").toString();

        // When : 발급된 인증 토큰 검증 요청
        String checkTokenUrlTemplate = "/oauth/check_token";
        ResultActions checkTokenResultAction = this.mockMvc.perform(post(checkTokenUrlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("token", accessToken)
        );

        // Then : 발급된 인증 토큰 검증 여부 확인
        checkTokenResultAction.andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("Refresh Token을 이용한 Access Token 갱신")
    public void refreshAccessToken() throws Exception {
        // Given : 토큰 발급 대상 사용자 정보 생성
        CreatePartner createPartner = this.partnerGenerator.createPartner();
        ProcessingResult createPartnerProcessingResult = this.partnerService.savePartner(createPartner);
        assertThat(createPartnerProcessingResult.isSuccess()).isTrue();

        // Given : 생성된 사용자에 클라이언트 정보 발급
        PartnerEntity savedPartnerEntity = (PartnerEntity) createPartnerProcessingResult.getData();
        String resourceIds = "NAVER";
        IssueClient issueClient = new IssueClient();
        issueClient.setPartnerNo(savedPartnerEntity.getPartnerNo());
        issueClient.setResourceIds(resourceIds);

        ProcessingResult issueClientProcessingResult = this.partnerService.saveClient(issueClient);
        assertThat(issueClientProcessingResult.isSuccess()).isTrue();

        // Given : 토큰 발급 요청에 필요한 사용자 정보와 클라이언트 정보 설정
        ClientEntity clientEntity = (ClientEntity) issueClientProcessingResult.getData();

        String urlTemplate = "/oauth/token";
        String clientId = clientEntity.getClientId();
        String clientSecret = clientEntity.getClientId();
        String username = createPartner.getPartnerId();
        String password = createPartner.getPartnerPassword();

        // When : 인증 토큰 발급 요청
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        );

        // Then : 인증 토큰 발급 여부 확인
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("expires_in").exists())
                .andExpect(jsonPath("scope").exists())
        ;

        // Then : 발급된 인증토큰 정보에서 access_token, refresh_toekn 추출
        String beforeAuthTokenInfo = resultActions.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        String beforeAccessToken = jackson2JsonParser.parseMap(beforeAuthTokenInfo).get("access_token").toString();
        String beforeRefreshToken = jackson2JsonParser.parseMap(beforeAuthTokenInfo).get("refresh_token").toString();

        // When : 인증 토큰 갱신 요청
        String refreshTokenUrlTemplate = "/oauth/token";
        ResultActions refreshTokenResultAction = this.mockMvc.perform(post(refreshTokenUrlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(httpBasic(clientId, clientSecret))
                .param("grant_type", "refresh_token")
                .param("refresh_token", beforeRefreshToken)
        );

        // Then : 인증 토큰 갱신 여부 확인
        refreshTokenResultAction.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("expires_in").exists())
                .andExpect(jsonPath("scope").exists());
        String afterAuthTokenInfo = refreshTokenResultAction.andReturn().getResponse().getContentAsString();
        String afterAccessToken = jackson2JsonParser.parseMap(afterAuthTokenInfo).get("access_token").toString();
        String afterRefreshToken = jackson2JsonParser.parseMap(afterAuthTokenInfo).get("refresh_token").toString();

        assertThat(beforeAccessToken).isNotEqualTo(afterAccessToken);
        assertThat(beforeRefreshToken).isNotEqualTo(afterRefreshToken);
    }
}