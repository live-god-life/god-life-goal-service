package com.godlife.goalservice.api;

import com.godlife.goalservice.api.response.ApiResponse;
import com.godlife.goalservice.service.GoalService;
import com.godlife.goalservice.service.dto.GoalServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalController {
    private final GoalService goalService;

    /**
     * 목표와 마인드셋 조회
     * @param method mindset을 가져오는 방법 ( normal, random )
     * @param count mindset의 갯수
     * @return
     */
    @GetMapping("/goals/mindsets")
    public ResponseEntity<ApiResponse> getGoalsWithMindsets(@RequestParam(value = "method", defaultValue = "normal") String method,
                                                            @RequestParam(value = "count", required = false) Integer count) {
        log.info("method: {}, count: {}", method, count);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createGetSuccessResponse(goalService.getGoalsWithMindsetsByMethodAndCount(method, count)));
    }
}
