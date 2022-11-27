package com.godlife.goalservice.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
		private Boolean completionStatus;
		private String taskType;
		private String repetitionType;
		private List<String> repetitionParams;
		private Integer totalTodoTaskScheduleCount;
		private Integer completedTodoTaskScheduleCount;
		private Long todoDay;

		@QueryProjection
		public TodoScheduleDto(Long goalId, Long todoScheduleId, Long todoId, String title, Boolean completionStatus, RepetitionType repetitionType, List<String> repetitionParams,
			Integer totalTodoTaskScheduleCount, Integer completedTodoTaskScheduleCount, LocalDate endDate) {

			this.goalId = goalId;
			this.todoScheduleId = todoScheduleId;
			this.todoId = todoId;
			this.title = title;
			this.completionStatus = completionStatus;
			if (repetitionType.equals(RepetitionType.NONE)) {    //디데이 Task
				this.taskType = "D-day";
			} else {                                            //투두 Task
				this.taskType = "Todo";
			}
			this.repetitionType = repetitionType.toString();
			this.repetitionParams = repetitionParams;
			this.totalTodoTaskScheduleCount = totalTodoTaskScheduleCount;
			this.completedTodoTaskScheduleCount = completedTodoTaskScheduleCount;
			this.todoDay = ChronoUnit.DAYS.between(endDate, LocalDate.now());
		}
	}
}
