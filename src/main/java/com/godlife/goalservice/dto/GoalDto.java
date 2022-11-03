package com.godlife.goalservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.enums.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalDto {
	private Long goalId;
	private Category category;
	private String title;
	private Boolean completionStatus;

	//==목표 시작일 종료일===
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate endDate;

	//===카운팅 통계 데이터===
	private Integer totalMindsetCount;
	private Integer totalTodoCount;
	private Integer completedTodoCount;
	private Integer totalTodoTaskScheduleCount;
	private Integer completedTodoTaskScheduleCount;

	public static GoalDto of(Goal goal) {
		return GoalDto.builder()
			.goalId(goal.getGoalId())
			.category(goal.getCategory())
			.title(goal.getTitle())
			.completionStatus(goal.getCompletionStatus())
			.category(goal.getCategory())
			.startDate(goal.getStartDate())
			.endDate(goal.getEndDate())
			.totalMindsetCount(goal.getTotalMindsetCount())
			.totalTodoCount(goal.getTotalTodoCount())
			.completedTodoCount(goal.getCompletedTodoCount())
			.totalTodoTaskScheduleCount(goal.getTotalTodoTaskScheduleCount())
			.completedTodoTaskScheduleCount(goal.getCompletedTodoTaskScheduleCount())
			.build();
	}
}
