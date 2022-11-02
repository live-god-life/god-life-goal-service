package com.godlife.goalservice.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.enums.Category;

import lombok.Builder;
import lombok.Data;
/*
    todo
 */

@Data
@Builder
public class GoalMindsetsTodosDto {
	private Long goalId;
	private String title;
	private Long userId;

	private Category category;

	private int mindsetCount;
	private int onProgressCount;
	private int completedCount;

	private LocalDate startDate;
	private LocalDate endDate;

	private String repetitionType;
	private String repetitionPrams;

	private Integer OrderNumber;

	@JsonIgnore
	private List<MindsetDto> mindsets;
	@JsonIgnore
	private List<TodoDto> todos;

	public static GoalMindsetsTodosDto of(Goal goal) {
		return GoalMindsetsTodosDto.builder()
			.goalId(goal.getGoalId())
			.title(goal.getTitle())
			.userId(goal.getUserId())
			.category(goal.getCategory())
			.completedCount(goal.getCompletedTodoCount())
			.build();
	}
}
