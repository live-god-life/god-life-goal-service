package com.godlife.goalservice.dto;

import java.util.List;

import com.godlife.goalservice.domain.Goal;
import com.querydsl.core.annotations.QueryProjection;

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

	@QueryProjection
	public GoalMindsetDto(Long goalId, String title, List<MindsetDto> mindsets) {
		this.goalId = goalId;
		this.title = title;
		this.mindsets = mindsets;
	}
}
