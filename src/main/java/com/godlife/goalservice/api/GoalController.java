package com.godlife.goalservice.api;

import com.godlife.goalservice.api.request.CreateGoalRequest;
import com.godlife.goalservice.api.response.ApiResponse;
import com.godlife.goalservice.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalController {
    private final GoalService goalService;

    /**
     * 목표와 마인드셋 조회
     * @param authorization JWT 서비스 토큰
     * @param method mindset을 가져오는 방법 ( normal, random )
     * @param count  mindset의 갯수
     * @return
     */
    @GetMapping("/goals/mindsets")
    public ResponseEntity<ApiResponse> getGoalsWithMindsets(@RequestHeader("Authorization") String authorization,
                                                            @RequestParam(value = "method", defaultValue = "normal") String method,
                                                            @RequestParam(value = "count", required = false) Integer count) {
        log.info("method: {}, count: {}", method, count);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoalsWithMindsetsByMethodAndCount(method, count, authorization)));
    }

    /**
     * 목표 추가
     *
     * @param request
     * @return
     */
    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> createGoal(@RequestBody CreateGoalRequest request) {
        log.info("request: {}", request);
        goalService.createGoal(request.toGoalServiceDto());
        //TODO 목표 추가 후 client 원하는 response 데이터 물어보기
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createPostSuccessResponse());
    }

    /**
     * 목표 조회
     * @param authorization JWT 서비스 토큰
     * @return
     */
    @GetMapping("/goals")
    public ResponseEntity<ApiResponse> getGoals(@RequestHeader(value = "Authorization") String authorization) {
        log.info("Header/Authorization: {}", authorization);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoals(authorization)));
    }
}
