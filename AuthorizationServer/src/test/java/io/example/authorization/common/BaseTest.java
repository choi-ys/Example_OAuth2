package io.example.authorization.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.generator.PartnerGenerator;
import io.example.authorization.repository.PartnerRepository;
import io.example.authorization.service.PartnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static io.example.authorization.constants.TestActiveProfilesConstants.TEST;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author : choi-ys
 * @date : 2021-02-27 오후 11:59
 * @Content : 각 Controller Test Case에서 중복으로 사용되는 항목 모듈화
 */
@Disabled
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(TEST)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Import({PartnerGenerator.class})
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PartnerRepository partnerRepository;

    @Autowired
    protected PartnerService partnerService;

    @Resource
    protected  PartnerGenerator partnerGenerator;

    @BeforeEach
    public void setup(){
        partnerRepository.deleteAll();
    }
}