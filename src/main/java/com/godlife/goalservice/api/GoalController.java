package com.godlife.goalservice.api;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.dto.request.UpdateGoalTodoScheduleRequest;
import com.godlife.goalservice.dto.response.ApiResponse;
import com.godlife.goalservice.exception.NoSuchTodosInTodoEntityException;
import com.godlife.goalservice.service.GoalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
    todo
    - 목표 추가 후 client 원하는 response 데이터 물어보기
    - date : yyyyMM 날짜형태의 좋은 변수명이 뭘까
    - validation check 추가
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalController {
	private final GoalService goalService;
	private static final String USER_ID_HEADER = "x-user";
	private static final int DEFAULT_PAGE = 25;

	@PostMapping("/goals")
	public ResponseEntity<ApiResponse> createGoal(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestBody CreateGoalRequest request) {

		goalService.createGoal(userId, request);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(ApiResponse.createPostSuccessResponse());
	}

	@GetMapping("/goals")
	public ResponseEntity<ApiResponse> getGoals(
		@PageableDefault(size = DEFAULT_PAGE) Pageable page,
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestParam(required = false) Boolean completionStatus) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiResponse.createGetSuccessResponse(goalService.getGoals(page, userId, completionStatus)));
	}

	@GetMapping("/goals/mindsets")
	public ResponseEntity<ApiResponse> getGoalsWithMindsets(
		@PageableDefault(size = DEFAULT_PAGE) Pageable page,
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestParam(required = false) Boolean completionStatus) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.createGetSuccessResponse(goalService.getGoalsWithMindsets(page, userId, completionStatus)));
	}

	@GetMapping("/goals/todos/count")
	public ResponseEntity<ApiResponse> getDailyTodosCount(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestParam(value = "date") YearMonth date) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.createGetSuccessResponse(goalService.getDailyTodosCount(userId, date)));
	}

	//======================================리팩토링 완료======================================

	@GetMapping("/goals/todos")
	public ResponseEntity<ApiResponse> getDailyGoalsAndTodos(@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestParam(value = "date") LocalDate date,
		@RequestParam(value = "completionStatus", required = false) Boolean completionStatus,
		Pageable page) {
		log.info("userId: {}, date: {}, completionStatus: {}, page: {}", userId, date, completionStatus, page);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.createGetSuccessResponse(
				goalService.getDailyGoalsAndTodos(userId, date, completionStatus, page)));
	}

	@GetMapping("/goals/todos/{todoId}")
	public ResponseEntity<ApiResponse> getTodoDetail(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable(value = "todoId") Long todoId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.createGetSuccessResponse(goalService.getTodoDetail(userId, todoId)));
	}

	@PatchMapping("/goals/todoSchedules/{todoScheduleId}")
	public ResponseEntity<ApiResponse> patchCompletionStatus(@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable(value = "todoScheduleId") Long todoScheduleId,
		@RequestBody UpdateGoalTodoScheduleRequest request) {
		goalService.updateTodoScheduleCompletionStatus(userId, todoScheduleId, request.getCompletionStatus());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createPatchSuccessResponse());
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse> noSuchTodosInTodoException(NoSuchTodosInTodoEntityException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.createErrorResponse(e.getMessage()));
	}
}
