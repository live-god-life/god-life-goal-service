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

import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getPostGoalsRequestFieldsSnippet;
import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getSuccessResponseFieldsSnippet;
import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoFolderRequest;
import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoTaskRequest;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
todo
인증관련 테스트는 어떻게 진행하는게 좋을까
 */

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class GoalControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("목표를 저장한다")
    void postGoals() throws Exception {
        //given & when
        ResultActions result = performSampleGoalsWithMindsetsAndTodos();

        //then
        result
                .andExpect(status().isCreated())
                .andDo(document("post-goals", getPostGoalsRequestFieldsSnippet(), getSuccessResponseFieldsSnippet()))
                .andDo(print());
    }

    @Test
    @DisplayName("모든 목표를 가져온다")
    void getAllGoals() throws Exception{
        //given
        performSampleGoalsWithMindsetsAndTodos();

        //when
        ResultActions result = performGetWithAuthorizationByUrlTemplate("/goals");

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("get-goals",
                        responseFields(
                                fieldWithPath("status").description("api 응답 상태"),
                                fieldWithPath("message").description("api 응답 메시지"),
                                fieldWithPath("data").description("api 응답 데이터"),

                                fieldWithPath("data[].goalId").description("목표 아이디"),
                                fieldWithPath("data[].title").description("목표 제목"),
                                fieldWithPath("data[].userId").description("사용자 아이디")
                        )))
                .andDo(print());
    }

    @Test
    @DisplayName("normal 방식으로 모든 마인드셋을 가져온다")
    void getAllGoalsWithMindsets() throws Exception {
        //given
        performSampleGoalsWithMindsetsAndTodos();

        //when
        ResultActions result = performGetWithAuthorizationByUrlTemplate("/goals/mindsets");

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets",
                responseFields(
                        fieldWithPath("status").description("api 응답 상태"),
                        fieldWithPath("message").description("api 응답 메시지"),
                        fieldWithPath("data").description("api 응답 데이터"),

                        fieldWithPath("data[].goalId").description("목표 아이디"),
                        fieldWithPath("data[].title").description("목표 제목"),
                        fieldWithPath("data[].userId").description("사용자 아이디")
                )))
                .andDo(print());
    }

    @Test
    @DisplayName("random 방식으로 5개의 마인드셋을 가져온다")
    void getFiveGoalsWithMindsetsByRandom() throws Exception {
        mockMvc.perform(get("/goals/mindsets")
                        .header("Authorization", "Bearer adjilafjisdlf")
                        .queryParam("method", "random")
                        .queryParam("count", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets", getSuccessResponseFieldsSnippet()))
                .andDo(print());
    }

    private ResultActions performSampleGoalsWithMindsetsAndTodos() throws Exception {
        CreateGoalMindsetRequest createGoalMindsetRequest = new CreateGoalMindsetRequest("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.");

        CreateGoalTodoRequest createGoalTodoRequest1 = getCreateGoalTodoFolderRequest(
                "포폴완성",
                List.of(getCreateGoalTodoFolderRequest(
                        "작업1 완료하기",
                        List.of(
                                getCreateGoalTodoTaskRequest("컨셉잡기"),
                                getCreateGoalTodoTaskRequest("스케치")
                        )
                ))
        );

        CreateGoalTodoRequest createGoalTodoRequest7 = getCreateGoalTodoFolderRequest(
                "개발프로젝트 해보기",
                List.of(
                        getCreateGoalTodoTaskRequest("IT 동아리 서류 내기"),
                        getCreateGoalTodoTaskRequest("파이썬 공부")
                )
        );

        CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
                .title("이직하기")
                .categoryName("커리어")
                .categoryCode("001")
                .mindsets(List.of(createGoalMindsetRequest))
                .todos(List.of(createGoalTodoRequest1, createGoalTodoRequest7))
                .build();

        return mockMvc.perform(
                post("/goals")
                        .header("Authorization", "Bearer adjilafjisdlf")
                        .content(objectMapper.writeValueAsString(createGoalRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        );
    }

    private ResultActions performGetWithAuthorizationByUrlTemplate(String urlTemplate) throws Exception {
        return mockMvc.perform(get(urlTemplate)
                .header("Authorization", "Bearer adjilafjisdlf")
                .accept(MediaType.APPLICATION_JSON));
    }
}