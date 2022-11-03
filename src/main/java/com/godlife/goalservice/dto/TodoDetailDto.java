package com.godlife.goalservice.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoDetailDto {
	private Long todoId;
	private String title;
	private String type;
	private String startDate;
	private String endDate;
	private String repetitionType;
	private List<String> repetitionParams;
	private String notification;
	private Integer totalTodoTaskScheduleCount;
	private Integer completedTodoTaskScheduleCount;

	public static TodoDetailDto of(Todo todo) {
		if (todo instanceof TodoTask) {
			TodoTask todoTask = (TodoTask)todo;
			return TodoDetailDto.builder()
				.todoId(todoTask.getTodoId())
				.title(todoTask.getTitle())
				.type("TASK")
				.startDate(todoTask.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.endDate(todoTask.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.repetitionType(todoTask.getRepetitionType().toString())
				.repetitionParams(todoTask.getRepetitionParams())
				.notification(todoTask.getNotification())
				.totalTodoTaskScheduleCount(todoTask.getTotalTodoTaskScheduleCount())
				.completedTodoTaskScheduleCount(todoTask.getCompletedTodoTaskScheduleCount())
				.build();
		} else {
			TodoFolder todoFolder = (TodoFolder)todo;
			return TodoDetailDto.builder()
				.title(todoFolder.getTitle())
				.build();
		}
	}
}
