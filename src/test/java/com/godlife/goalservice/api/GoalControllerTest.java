package com.godlife.goalservice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class GoalControllerTest {
    @Autowired private MockMvc mockMvc;
    
    @Test
    @DisplayName("random 방식으로 5개의 마인드셋을 가져온다")
    void getFiveGoalsWithMindsetsByRandom() throws Exception {
        mockMvc.perform(get("/goals/mindsets")
                        .queryParam("method", "random")
                        .queryParam("count","5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets",
                        responseFields(
                                fieldWithPath("status").description("api 응답 상태"),
                                fieldWithPath("message").description("api 응답 메시지"),
                                fieldWithPath("data").description("api 응답 데이터")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("normal 방식으로 모든 마인드셋을 가져온다")
    void getAllGoalsWithMindsets() throws Exception {
        mockMvc.perform(get("/goals/mindsets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets",
                        responseFields(
                                fieldWithPath("status").description("api 응답 상태"),
                                fieldWithPath("message").description("api 응답 메시지"),
                                fieldWithPath("data").description("api 응답 데이터")
                        )))
                .andDo(print());
    }
}