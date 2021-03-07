package com.example.resource.controller;

import com.example.resource.constants.MediaTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("API : Index")
class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("API : API 목록 조회")
    public void index() throws Exception {
        // Given
        String urlTemplate = "/api/index";

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links").exists())
        ;
    }
}