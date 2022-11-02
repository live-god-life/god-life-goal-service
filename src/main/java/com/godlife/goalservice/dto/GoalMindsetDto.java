package com.godlife.goalservice.dto;

import java.util.List;

import com.godlife.goalservice.domain.Goal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalMindsetDto {
	private Long goalId;
	private String title;
	private List<MindsetDto> mindsets;

	public static GoalMindsetDto of(Goal goal) {
		return GoalMindsetDto.builder()
			.goalId(goal.getGoalId())
			.title(goal.getTitle())
			.build();
	}
}
