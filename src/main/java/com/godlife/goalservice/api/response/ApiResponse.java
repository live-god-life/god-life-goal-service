package com.godlife.goalservice.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    todo
 */

@Data
@NoArgsConstructor
public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    private ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse createGetSuccessResponse(List dtos) {
        return new ApiResponse("success", "ok", dtos);
    }

    public static ApiResponse createPostSuccessResponse() {
        return new ApiResponse("success", "created", null);
    }

    public static ApiResponse createPatchSuccessResponse() {
        return new ApiResponse("success", "modified", null);
    }

    public static ApiResponse createGetSuccessResponse(Object o) {
        return new ApiResponse("success", "ok", o);
    }
}
