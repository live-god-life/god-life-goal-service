package com.godlife.goalservice.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGoalRequest {
	private String title;

	private String categoryName;
	private String categoryCode;

	private List<CreateGoalMindsetRequest> mindsets;
	private List<CreateGoalTodoRequest> todos;
}
