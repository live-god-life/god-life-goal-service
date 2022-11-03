package com.godlife.goalservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godlife.goalservice.dto.request.CreateGoalMindsetRequest;
import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.dto.request.CreateGoalTodoRequest;
import com.godlife.goalservice.dto.request.UpdateGoalTodoScheduleRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoFolderRequest;
import static com.godlife.goalservice.utils.SampleTestDataCreator.getCreateGoalTodoTaskRequest;
import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getPostGoalsRequestFieldsSnippet;
import static com.godlife.goalservice.utils.restdoc.DocumentProvider.getSuccessResponseFieldsSnippet;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    todo
    - 리스폰스, 리퀘스트 확정전까진 relaxed
 */

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class GoalControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private static final String USER_ID_HEADER = "x-user";
	private static final Long TEST_USER_ID = 1L;

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
	@DisplayName("모든 목표를 가져온다")
	void getAllGoals() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		ResultActions result = performGetWithAuthorizationByUrlTemplate("/goals");

		//then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0]").exists())
			.andDo(document("get-goals",
				relaxedResponseFields(
					fieldWithPath("status").description("api 응답 상태"),
					fieldWithPath("message").description("api 응답 메시지"),
					fieldWithPath("data").description("api 응답 데이터"),
					fieldWithPath("data[].goalId").description("목표 아이디"),
					fieldWithPath("data[].title").description("목표 제목")
				)))
			.andDo(print());
	}

	@Test
	@DisplayName("미완료인 모든 목표를 가져온다")
	void getAllGoals1() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		ResultActions result = mockMvc.perform(get("/goals")
			.header(USER_ID_HEADER, TEST_USER_ID)
			.queryParam("completionStatus", "false")
			.accept(MediaType.APPLICATION_JSON)
		);

		//then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0]").exists())
			.andDo(document("get-goals",
				relaxedResponseFields(
					fieldWithPath("status").description("api 응답 상태"),
					fieldWithPath("message").description("api 응답 메시지"),
					fieldWithPath("data").description("api 응답 데이터"),

					fieldWithPath("data[].goalId").optional().description("목표 아이디").type(Object.class),
					fieldWithPath("data[].title").optional().description("목표 제목").type(Object.class)
				)))
			.andDo(print());
	}

	//======================================리팩토링 완료======================================

	@Test
	@DisplayName("모든 마인드셋을 가져온다")
	void getAllGoalsWithMindsets() throws Exception {
		//given
		for (int i = 0; i < 1; i++) {
			performPostSampleGoalsWithMindsetsAndTodos();
		}

		//when
		ResultActions result = mockMvc.perform(get("/goals/mindsets")
			.header(USER_ID_HEADER, TEST_USER_ID)
			.queryParam("page", "0")
			.queryParam("size", "5")
				.queryParam("completionStatus","false")
			.accept(MediaType.APPLICATION_JSON));

		//then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0].mindsets").exists())
			.andDo(document("get-goals-with-mindsets",
				relaxedResponseFields(
					fieldWithPath("status").description("api 응답 상태"),
					fieldWithPath("message").description("api 응답 메시지"),
					fieldWithPath("data").description("api 응답 데이터"),

					fieldWithPath("data[].goalId").description("목표 아이디"),
					fieldWithPath("data[].title").description("목표 제목")
				)))
			.andDo(print());
	}

	// @Test
	@DisplayName("MyList/캘린더 특정 년월의 일자별 투두카운트를 조회한다")
	void getDailyTodosCount() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos/count")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "202210")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-goals-with-todos-count", getSuccessResponseFieldsSnippet()))
			.andDo(print());

		//then
	}

	// @Test
	@DisplayName("MyList/캘린더 특정 년월일의 투두리스트를 조회한다_미완료,완료 전체")
	void getDailyGoalsAndLowestDepthTodos1() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "20221003")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	// @Test
	@DisplayName("MyList/캘린더 특정 년월일의 투두리스트를 조회한다_미완료")
	void getDailyGoalsAndLowestDepthTodos2() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "20221003")
				.queryParam("size", "5")
				.queryParam("completionStatus", "false")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	// @Test
	@DisplayName("특정 년월일의 투두리스트에 완료체크를 한다")
	void put() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		UpdateGoalTodoScheduleRequest updateGoalTodoScheduleRequest = UpdateGoalTodoScheduleRequest.builder()
			.completionStatus(true)
			.build();

		//when
		mockMvc.perform(patch("/goals/todoSchedules/{todoScheduleId}", 1)
				.header(USER_ID_HEADER, TEST_USER_ID)
				.content(objectMapper.writeValueAsString(updateGoalTodoScheduleRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			//                .andDo(document("patch-goals-todoSchedule", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	// @Test
	@DisplayName("투두의 상세정보를 조회한다")
	void getTodoDetail() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos/{todoId}", 2)
				.header(USER_ID_HEADER, TEST_USER_ID)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			//                .andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	private ResultActions performPostSampleGoalsWithMindsetsAndTodos() throws Exception {
		CreateGoalMindsetRequest createGoalMindsetRequest1 = new CreateGoalMindsetRequest(
			"사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다111.");

		CreateGoalMindsetRequest createGoalMindsetRequest2 = new CreateGoalMindsetRequest(
			"사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다222.");

		CreateGoalMindsetRequest createGoalMindsetRequest3 = new CreateGoalMindsetRequest(
			"사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다333.");

		CreateGoalTodoRequest todoFolder1 = getCreateGoalTodoFolderRequest(
			"포폴완성",
			1,
			1,
			List.of(
				getCreateGoalTodoTaskRequest(
					"컨셉잡기",
					3,
					0,
					"20221001",
					"20221031",
					"DAY",
					null,
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"스케치",
					3,
					1,
					"20221101",
					"20221131",
					"WEEK",
					List.of("월", "수", "금", "토"),
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"UI 작업",
					3,
					2,
					"20221001",
					"20221231",
					"NONE",
					null,
					"0900"
				)
			)
		);

		CreateGoalTodoRequest todoFolder2 = getCreateGoalTodoFolderRequest(
			"개발프로젝트 해보기",
			1,
			2,
			List.of(
				getCreateGoalTodoTaskRequest(
					"IT 동아리 서류 내기",
					2,
					0,
					"20221001",
					"20221031",
					"NONE",
					null,
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"파이썬 공부",
					2,
					0,
					"20221001",
					"20221031",
					"WEEK",
					List.of("월", "수", "금"),
					"0900"
				)
			)
		);

		CreateGoalTodoRequest todoTask1 = getCreateGoalTodoTaskRequest(
			"외주",
			1,
			3,
			"20221001",
			"20221031",
			"NONE",
			null,
			"0900"
		);

		CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
			.title("이직하기")
			.categoryName("커리어")
			.categoryCode("CAREER")
			.mindsets(List.of(createGoalMindsetRequest1, createGoalMindsetRequest2, createGoalMindsetRequest3))
			.todos(List.of(todoFolder1, todoFolder2, todoTask1))
			.build();

		return mockMvc.perform(
			post("/goals")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.content(objectMapper.writeValueAsString(createGoalRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)

		);
	}

	private ResultActions performGetWithAuthorizationByUrlTemplate(String urlTemplate) throws Exception {
		return mockMvc.perform(get(urlTemplate)
			.header(USER_ID_HEADER, TEST_USER_ID)
			.accept(MediaType.APPLICATION_JSON));
	}
}
