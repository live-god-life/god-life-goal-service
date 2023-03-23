package com.godlife.goalservice.dto.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

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

	public static ApiResponse createDeleteSuccessResponse() {
		return new ApiResponse("success", "deleted", null);
	}

	public static ApiResponse createGetSuccessResponse(Object o) {
		return new ApiResponse("success", "ok", o);
	}

	public static ApiResponse createErrorResponse(String errorMessage) {
		return new ApiResponse("error", errorMessage, null);
	}

	public static ApiResponse createPutSuccessResponse() {
		return new ApiResponse("success", "modified", null);
	}
}
