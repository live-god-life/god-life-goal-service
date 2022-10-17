package com.godlife.goalservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godlife.goalservice.api.request.CreateGoalMindsetRequest;
import com.godlife.goalservice.api.request.CreateGoalRequest;
import com.godlife.goalservice.api.request.CreateGoalTodoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class GoalControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void postGoals() throws Exception {
        //given
        //sample mindset
        CreateGoalMindsetRequest createGoalMindsetRequest = new CreateGoalMindsetRequest("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.");

        //sample todo-task
        CreateGoalTodoRequest createGoalTodoRequest2 = new CreateGoalTodoRequest("작업1완료하기", "folder");
        CreateGoalTodoRequest createGoalTodoRequest3 = new CreateGoalTodoRequest("컨셉잡기", "task");
        CreateGoalTodoRequest createGoalTodoRequest4 = new CreateGoalTodoRequest("스케치", "task");
        //sample todo-folder
        CreateGoalTodoRequest createGoalTodoRequest1 = new CreateGoalTodoRequest("포폴완성", "folder", List.of(createGoalTodoRequest2, createGoalTodoRequest3, createGoalTodoRequest4));

        //sample goal
        CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
                .title("1년안에 5000만원 모으기")
                .categoryName("돈관리")
                .categoryCode("001")
                .mindsets(List.of(createGoalMindsetRequest))
                .todos(List.of(createGoalTodoRequest1))
                .build();

        //when
        ResultActions result = mockMvc.perform(
                post("/goals")
                        .content(objectMapper.writeValueAsString(createGoalRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        );

        //then
        result.andExpect(status().isCreated());

        //rest-doc
        result.andDo(document("post-goals",
                requestFields(
                        fieldWithPath("title").description("목표 제목"),
                        fieldWithPath("categoryName").description("목표 카테고리명"),
                        fieldWithPath("categoryCode").description("목표 카테고리코드"),

                        fieldWithPath("mindsets[].content").description("마인드셋 내용"),

                        fieldWithPath("todos[].title").description("목표 제목"),
                        fieldWithPath("todos[].type").description("목표 제목"),
                        fieldWithPath("todos[].depth").description("목표 제목"),
                        fieldWithPath("todos[].order").description("목표 제목"),
                        fieldWithPath("todos[].todos").description("목표 제목"),

                        fieldWithPath("todos[].todos[].title").description("목표 제목"),
                        fieldWithPath("todos[].todos[].type").description("목표 제목"),
                        fieldWithPath("todos[].todos[].depth").description("목표 제목"),
                        fieldWithPath("todos[].todos[].order").description("목표 제목"),
                        fieldWithPath("todos[].todos[].todos").description("목표 제목")
                ),
                responseFields(
                        fieldWithPath("status").description(""),
                        fieldWithPath("message").description(""),
                        fieldWithPath("data").description("")
                )));

    }
    
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