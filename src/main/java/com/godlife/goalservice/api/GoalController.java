package com.godlife.goalservice.api;

import com.godlife.goalservice.api.request.CreateGoalRequest;
import com.godlife.goalservice.api.response.ApiResponse;
import com.godlife.goalservice.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
todo
- 목표 추가 후 client 원하는 response 데이터 물어보기
- 생각해보니 모든 API에 사용자 정보가 필요하다. 이런식으로 밖에 안될까?
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalController {
    private final GoalService goalService;

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
