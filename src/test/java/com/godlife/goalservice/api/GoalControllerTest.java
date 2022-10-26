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

import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoFolderRequest;
import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoTaskRequest;
import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getPostGoalsRequestFieldsSnippet;
import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getSuccessResponseFieldsSnippet;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    todo
    - 인증관련 테스트는 어떻게 진행하는게 좋을까
    - 리스폰스, 리퀘스트 확정전까진 relaxed
 */

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class GoalControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String JWT_TOKEN = "Bearer token";

    @Test
    @DisplayName("목표를 저장한다")
    void postGoals() throws Exception {
        //given & when
        ResultActions result = performPostSampleGoalsWithMindsetsAndTodos();

        //then
        result
                .andExpect(status().isCreated())
                .andDo(document("post-goals", getPostGoalsRequestFieldsSnippet(), getSuccessResponseFieldsSnippet()))
                .andDo(print());
    }
    
    @Test
    @DisplayName("MyList/캘린더 특정 년월의 일자별 투두카운트를 조회한다")
    void getDailyTodosCount() throws Exception {
        //given
        performPostSampleGoalsWithMindsetsAndTodos();

        //when
        mockMvc.perform(get("/goals/todos/count")
                        .header("Authorization", JWT_TOKEN)
                        .queryParam("date", "202210")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-todos-count", getSuccessResponseFieldsSnippet()))
                .andDo(print());

        //then
    }
    
    @Test
    @DisplayName("MyList/캘린더 특정 년월일의 투두리스트를 조회한다")
    void getDailyGoalsAndLowestDepthTodos() throws Exception{
        //given
        performPostSampleGoalsWithMindsetsAndTodos();

        //when
        mockMvc.perform(get("/goals/todos")
                        .header("Authorization", JWT_TOKEN)
                        .queryParam("date", "20221001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
                .andDo(print());
        
        //then
    }

    @Test
    @DisplayName("MyList/캘린더 특정 년월일의 투두리스트에 완료체크를 한다")
    void put() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("모든 목표를 가져온다")
    void getAllGoals() throws Exception{
        //given
        performPostSampleGoalsWithMindsetsAndTodos();

        //when
        ResultActions result = performGetWithAuthorizationByUrlTemplate("/goals");

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("get-goals",
                        relaxedResponseFields(
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
        performPostSampleGoalsWithMindsetsAndTodos();

        //when
        ResultActions result = performGetWithAuthorizationByUrlTemplate("/goals/mindsets");

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets",
                        relaxedResponseFields(
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
                        .header("Authorization", JWT_TOKEN)
                        .queryParam("method", "random")
                        .queryParam("count", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-goals-with-mindsets", getSuccessResponseFieldsSnippet()))
                .andDo(print());
    }

    private ResultActions performPostSampleGoalsWithMindsetsAndTodos() throws Exception {
        CreateGoalMindsetRequest createGoalMindsetRequest = new CreateGoalMindsetRequest("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.");

        CreateGoalTodoRequest createGoalTodoRequest1 = getCreateGoalTodoFolderRequest(
                "포폴완성",
                List.of(getCreateGoalTodoFolderRequest(
                        "작업1 완료하기",
                        List.of(
                                getCreateGoalTodoTaskRequest(
                                        "컨셉잡기",
                                        "20221001",
                                        "20221031",
                                        "DAY",
                                        null,
                                        "0900"
                                ),
                                getCreateGoalTodoTaskRequest(
                                        "스케치",
                                        "20221101",
                                        "20221131",
                                        "WEEK",
                                        List.of("월", "수", "금", "토"),
                                        "0900"
                                ),
                                getCreateGoalTodoTaskRequest(
                                        "UI 작업",
                                        "20221001",
                                        "20221231",
                                        "NONE",
                                        null,
                                        "0900"
                                )
                        )
                ))
        );

        CreateGoalTodoRequest createGoalTodoRequest7 = getCreateGoalTodoFolderRequest(
                "개발프로젝트 해보기",
                List.of(
                        getCreateGoalTodoTaskRequest(
                                "IT 동아리 서류 내기",
                                "20221001",
                                "20221031",
                                "NONE",
                                null,
                                "0900"
                        ),
                        getCreateGoalTodoTaskRequest(
                                "파이썬 공부",
                                "20221001",
                                "20221031",
                                "WEEK",
                                List.of("월","수","금"),
                                "0900"
                        )
                )
        );

        CreateGoalTodoRequest createGoalTodoTaskRequest = getCreateGoalTodoTaskRequest(
                "최상위 태스크",
                "20221001",
                "20221031",
                "DAY",
                null,
                "0900"
        );

        CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
                .title("이직하기")
                .categoryName("커리어")
                .categoryCode("CAREER")
                .mindsets(List.of(createGoalMindsetRequest))
                .todos(List.of(createGoalTodoRequest1, createGoalTodoRequest7, createGoalTodoTaskRequest))
                .build();

        return mockMvc.perform(
                post("/goals")
                        .header("Authorization", JWT_TOKEN)
                        .content(objectMapper.writeValueAsString(createGoalRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        );
    }

    private ResultActions performGetWithAuthorizationByUrlTemplate(String urlTemplate) throws Exception {
        return mockMvc.perform(get(urlTemplate)
                .header("Authorization", JWT_TOKEN)
                .accept(MediaType.APPLICATION_JSON));
    }
}