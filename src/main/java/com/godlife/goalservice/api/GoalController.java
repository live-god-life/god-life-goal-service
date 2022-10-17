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
     *
     * @param method mindset을 가져오는 방법 ( normal, random )
     * @param count  mindset의 갯수
     * @return
     */
    @GetMapping("/goals/mindsets")
    public ResponseEntity<ApiResponse> getGoalsWithMindsets(@RequestParam(value = "method", defaultValue = "normal") String method,
                                                            @RequestParam(value = "count", required = false) Integer count) {
        log.info("method: {}, count: {}", method, count);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoalsWithMindsetsByMethodAndCount(method, count)));
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> createGoals(@RequestBody CreateGoalRequest request) {
        log.info("request: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createPostSuccessResponse());
    }
}
