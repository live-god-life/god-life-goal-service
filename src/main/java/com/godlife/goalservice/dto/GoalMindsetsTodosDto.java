package com.godlife.goalservice.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.godlife.goalservice.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class GoalMindsetsTodosDto {
	//===목표 정보===
	private Long goalId;
	private Category category;
	private String title;
	//===목표 시작일 종료일===
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate endDate;
	//===목표 카운팅 통계 데이터===
	private Integer totalMindsetCount;
	private Integer totalTodoCount;
	private Integer completedTodoCount;
	private Integer totalTodoTaskScheduleCount;
	private Integer completedTodoTaskScheduleCount;

	//===마인드셋 정보===
	private List<MindsetDto> mindsets;

	//===투두 정보===
	private List<TodoDto> todos;

	@QueryProjection
	public GoalMindsetsTodosDto(Long goalId, Category category, String title, LocalDate startDate, LocalDate endDate, Integer totalMindsetCount, Integer totalTodoCount, Integer completedTodoCount,
		Integer totalTodoTaskScheduleCount, Integer completedTodoTaskScheduleCount) {
		this.goalId = goalId;
		this.category = category;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalMindsetCount = totalMindsetCount;
		this.totalTodoCount = totalTodoCount;
		this.completedTodoCount = completedTodoCount;
		this.totalTodoTaskScheduleCount = totalTodoTaskScheduleCount;
		this.completedTodoTaskScheduleCount = completedTodoTaskScheduleCount;
	}

	public void registerMindsetDtos(List<MindsetDto> mindsetDtos) {
		this.mindsets = mindsetDtos;
	}

	public void registerTodoDtos(List<TodoDto> todoDtos) {
		this.todos = todoDtos;
	}
}
