package com.godlife.goalservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.dto.request.UpdateGoalTodoScheduleRequest;
import com.godlife.goalservice.dto.response.ApiResponse;
import com.godlife.goalservice.service.GoalCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalCommandController {
	private final GoalCommandService goalCommandService;
	private static final String USER_ID_HEADER = "x-user";

	@PostMapping("/goals")
	public ResponseEntity<ApiResponse> createGoal(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestBody CreateGoalRequest request) {

		goalCommandService.createGoal(userId, request);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(ApiResponse.createPostSuccessResponse());
	}

	@PatchMapping("/goals/todoSchedules/{todoScheduleId}")
	public ResponseEntity<ApiResponse> patchCompletionStatus(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable(value = "todoScheduleId") Long todoScheduleId,
		@RequestBody UpdateGoalTodoScheduleRequest request) {

		goalCommandService.updateTodoScheduleCompletionStatus(userId, todoScheduleId, request.getCompletionStatus());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createPatchSuccessResponse());
	}

	@DeleteMapping("/goals/todos/{todoId}")
	public ResponseEntity<ApiResponse> getTodoDetail(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable(value = "todoId") Long todoId) {
		goalCommandService.deleteTodo(userId, todoId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createDeleteSuccessResponse());
	}
}
