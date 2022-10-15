package com.godlife.goalservice.api.response;

import com.godlife.goalservice.service.dto.GoalServiceDto;
import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    private ApiResponse(String status, String message, Object data) {

    }

    public static ApiResponse createGetSuccessResponse(List<GoalServiceDto> goalServiceDtos) {
        return new ApiResponse("success", "ok", goalServiceDtos);
    }
}
