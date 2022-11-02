package com.godlife.goalservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class GoalTodoScheduleDto {
	private Long goalId;
	private String goalTitle;
	private List<GoalTodoScheduleDto.TodoScheduleDto> todoScheduleDtos = new ArrayList<>();

	@QueryProjection
	public GoalTodoScheduleDto(Long goalId, String goalTitle) {
		this.goalId = goalId;
		this.goalTitle = goalTitle;
	}

	public void addTodoSchedule(GoalTodoScheduleDto.TodoScheduleDto todoScheduleDto) {
		todoScheduleDtos.add(todoScheduleDto);
	}

	@Data
	public static class TodoScheduleDto {
		private Long goalId;
		private Long todoId;
		private String title;
		private Boolean completionStatue;

		@QueryProjection
		public TodoScheduleDto(Long goalId, Long todoId, String title, Boolean completionStatue) {
			this.goalId = goalId;
			this.todoId = todoId;
			this.title = title;
			this.completionStatue = completionStatue;
		}
	}
}
