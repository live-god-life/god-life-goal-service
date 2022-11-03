package com.godlife.goalservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.enums.RepetitionType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class GoalTodoScheduleDto {
	private Long goalId;
	private String title;
	private List<GoalTodoScheduleDto.TodoScheduleDto> todoSchedules = new ArrayList<>();

	@QueryProjection
	public GoalTodoScheduleDto(Long goalId, String title) {
		this.goalId = goalId;
		this.title = title;
	}

	public void addTodoSchedule(List<GoalTodoScheduleDto.TodoScheduleDto> todoScheduleDto) {
		todoSchedules.addAll(todoScheduleDto);
	}

	@Data
	public static class TodoScheduleDto {
		@JsonIgnore
		private Long goalId;
		private Long todoScheduleId;
		private Long todoId;
		private String title;
		private Boolean completionStatue;
		private String taskType;

		@QueryProjection
		public TodoScheduleDto(Long goalId, Long todoScheduleId, Long todoId, String title, Boolean completionStatue, RepetitionType repetitionType) {
			this.goalId = goalId;
			this.todoScheduleId = todoScheduleId;
			this.todoId = todoId;
			this.title = title;
			this.completionStatue = completionStatue;
			if (repetitionType.equals(RepetitionType.NONE)) { 	//디데이 Task
				this.taskType = "D-day";
			} else {											//투두 Task
				this.taskType = "Todo";
			}
		}
	}
}
