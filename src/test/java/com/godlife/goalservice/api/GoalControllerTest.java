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
    - ????????????, ???????????? ??????????????? relaxed
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
	@DisplayName("????????? ????????????")
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
	@DisplayName("?????? ????????? ????????????")
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
					fieldWithPath("status").description("api ?????? ??????"),
					fieldWithPath("message").description("api ?????? ?????????"),
					fieldWithPath("data").description("api ?????? ?????????"),
					fieldWithPath("data[].goalId").description("?????? ?????????"),
					fieldWithPath("data[].title").description("?????? ??????")
				)))
			.andDo(print());
	}

	@Test
	@DisplayName("???????????? ?????? ????????? ????????????")
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
					fieldWithPath("status").description("api ?????? ??????"),
					fieldWithPath("message").description("api ?????? ?????????"),
					fieldWithPath("data").description("api ?????? ?????????"),

					fieldWithPath("data[].goalId").optional().description("?????? ?????????").type(Object.class),
					fieldWithPath("data[].title").optional().description("?????? ??????").type(Object.class)
				)))
			.andDo(print());
	}

	@Test
	@DisplayName("?????? ??????????????? ????????????")
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
					fieldWithPath("status").description("api ?????? ??????"),
					fieldWithPath("message").description("api ?????? ?????????"),
					fieldWithPath("data").description("api ?????? ?????????"),

					fieldWithPath("data[].goalId").description("?????? ?????????"),
					fieldWithPath("data[].title").description("?????? ??????")
				)))
			.andDo(print());
	}

	@Test
	@DisplayName("MyList/????????? ?????? ????????? ????????? ?????????????????? ????????????")
	void getDailyTodosCount() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos/counts")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "202210")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0].date").exists())
			.andDo(document("get-goals-with-todos-count", getSuccessResponseFieldsSnippet()))
			.andDo(print());

		//then
	}

	@Test
	@DisplayName("MyList/????????? ?????? ???????????? ?????????????????? ????????????_?????????,?????? ??????")
	void getDailyGoalsAndLowestDepthTodos1() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "20221031")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	@Test
	@DisplayName("MyList/????????? ?????? ???????????? ?????????????????? ????????????_?????????")
	void getDailyGoalsAndLowestDepthTodos2() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		mockMvc.perform(get("/goals/todos")
				.header(USER_ID_HEADER, TEST_USER_ID)
				.queryParam("date", "20221031")
				.queryParam("size", "1")
				.queryParam("completionStatus", "false")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("get-goals-with-todos", getSuccessResponseFieldsSnippet()))
			.andDo(print());
		//then
	}

	@Test
	@DisplayName("????????? ??????????????? ????????????")
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

	@Test
	@DisplayName("????????? ??????????????? ????????????")
	void getGoalDetail() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		ResultActions result = mockMvc.perform(get("/goals/{goalId}", 1)
			.header(USER_ID_HEADER, TEST_USER_ID)
			.accept(MediaType.APPLICATION_JSON));;

		//then
		result
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.goalId").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("???????????? ?????? ????????? ????????????.")
	void getTodoSchedules() throws Exception {
		//given
		performPostSampleGoalsWithMindsetsAndTodos();

		//when
		ResultActions result = mockMvc.perform(get("/goals/todos/{todoId}/todoSchedules", 2)
			.header(USER_ID_HEADER, TEST_USER_ID)
			.queryParam("page", "1")
			.queryParam("criteria", "before")
			.accept(MediaType.APPLICATION_JSON));

		//then
		result
			.andExpect(status().isOk())
			// .andExpect(jsonPath("$.data.goalId").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("?????? ???????????? ?????????????????? ??????????????? ??????")
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

	private ResultActions performPostSampleGoalsWithMindsetsAndTodos() throws Exception {
		CreateGoalMindsetRequest createGoalMindsetRequest1 = new CreateGoalMindsetRequest(
			"????????? ???????????? ?????? ??????????????? ????????? ?????? ?????? ?????????. ?????? ????????? ????????? ??????111.");

		CreateGoalMindsetRequest createGoalMindsetRequest2 = new CreateGoalMindsetRequest(
			"????????? ???????????? ?????? ??????????????? ????????? ?????? ?????? ?????????. ?????? ????????? ????????? ??????222.");

		CreateGoalMindsetRequest createGoalMindsetRequest3 = new CreateGoalMindsetRequest(
			"????????? ???????????? ?????? ??????????????? ????????? ?????? ?????? ?????????. ?????? ????????? ????????? ??????333.");

		CreateGoalTodoRequest todoFolder1 = getCreateGoalTodoFolderRequest(
			"????????????",
			1,
			1,
			List.of(
				getCreateGoalTodoTaskRequest(
					"????????????",
					2,
					0,
					"20221001",
					"20221031",
					"DAY",
					null,
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"?????????",
					2,
					1,
					"20221101",
					"20221131",
					"WEEK",
					List.of("???", "???", "???", "???"),
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"UI ??????",
					2,
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
			"?????????????????? ?????????",
			1,
			2,
			List.of(
				getCreateGoalTodoTaskRequest(
					"IT ????????? ?????? ??????",
					2,
					0,
					"20221001",
					"20221031",
					"NONE",
					null,
					"0900"
				),
				getCreateGoalTodoTaskRequest(
					"????????? ??????",
					2,
					0,
					"20221001",
					"20221031",
					"WEEK",
					List.of("???", "???", "???"),
					"0900"
				)
			)
		);

		CreateGoalTodoRequest todoTask1 = getCreateGoalTodoTaskRequest(
			"??????",
			1,
			3,
			"20221001",
			"20221031",
			"NONE",
			null,
			"0900"
		);

		CreateGoalRequest createGoalRequest = CreateGoalRequest.builder()
			.title("????????????")
			.categoryName("?????????")
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
