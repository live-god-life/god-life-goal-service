package com.godlife.goalservice.dto.request;

import java.util.List;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.enums.Category;

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

	public Goal createGoalEntity(Long userId) {
		return Goal.createGoal(userId, Category.valueOf(categoryCode), title);
	}
}
