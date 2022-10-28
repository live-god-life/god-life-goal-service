package com.godlife.goalservice.api;

import com.godlife.goalservice.api.request.CreateGoalRequest;
import com.godlife.goalservice.api.request.UpdateGoalTodoScheduleRequest;
import com.godlife.goalservice.api.response.ApiResponse;
import com.godlife.goalservice.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

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

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> createGoal(@RequestHeader("Authorization") String authorization,
                                                  @RequestBody CreateGoalRequest request) {
        log.info("request: {}", request);
        goalService.createGoal(authorization, request.toGoalServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createPostSuccessResponse());
    }



    @GetMapping("/goals/todos/count")
    public ResponseEntity<ApiResponse> getDailyTodosCount(@RequestHeader("Authorization") String authorization,
                                                          @RequestParam(value = "date") YearMonth date) {

        log.info("date!!: {}", date);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getDailyTodosCount(authorization, date)));
    }

    //===================================================================================================================

    @GetMapping("/goals/todos")
    public ResponseEntity<ApiResponse> getDailyGoalsAndLowestDepthTodos(@RequestHeader("Authorization") String authorization,
                                                                        @RequestParam(value = "date") String date) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getDailyGoalsAndLowestDepthTodos(authorization, date)));
    }

    @GetMapping("/goals/mindsets")
    public ResponseEntity<ApiResponse> getGoalsWithMindsets(@RequestHeader("Authorization") String authorization,
                                                            @RequestParam(value = "method", defaultValue = "normal") String method,
                                                            @RequestParam(value = "count", required = false) Integer count) {
        log.info("method: {}, count: {}", method, count);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoalsWithMindsetsByMethodAndCount(method, count, authorization)));
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> createGoal(@RequestHeader("Authorization") String authorization,
                                                  @RequestBody CreateGoalRequest request) {
        log.info("request: {}", request);
        goalService.createGoal(authorization, request.toGoalServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createPostSuccessResponse());
    }

    @GetMapping("/goals")
    public ResponseEntity<ApiResponse> getGoals(@RequestHeader("Authorization") String authorization) {
        log.info("Header/Authorization: {}", authorization);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoals(authorization)));
    }
}
