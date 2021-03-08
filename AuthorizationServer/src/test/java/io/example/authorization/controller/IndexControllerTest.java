package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.constants.MediaTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : choi-ys
 * @date : 2021/03/08 2:44 오후
 * @Content :
 */
@DisplayName("API:Index")
class IndexControllerTest extends BaseTest {

    @Test
    @DisplayName("Index API : public")
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

    @Test
    @DisplayName("Index API : authorized")
    public void indexWithAuthorization() throws Exception {
        // Given
        String urlTemplate = "/api/index";

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links").exists())
        ;
    }
}