package com.godlife.goalservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.enums.Category;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
/*
    todo
 */

@Data
@Builder
public class GoalDto {
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

	public static GoalDto of(Goal goal) {
		return GoalDto.builder()
			.goalId(goal.getGoalId())
			.title(goal.getTitle())
			.userId(goal.getUserId())
			.category(goal.getCategory())
			.mindsetCount(goal.getMindsetTotalCount())
			.onProgressCount(goal.getProgressCount())
			.completedCount(goal.getCompletedCount())
			.mindsets(goal.getMindsets().stream().map(MindsetDto::of).collect(Collectors.toList()))
			.todos(goal.getTodos().stream().map(TodoDto::of).collect(Collectors.toList()))
			.build();
	}
}
