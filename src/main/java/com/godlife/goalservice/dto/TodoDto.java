package com.godlife.goalservice.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TodoDto {
	private Long id;
	private String title;
	private String type;
	private Integer depth;
	private Integer orderNumber;
	private String startDate;
	private String endDate;
	private String repetitionType;
	private List<String> repetitionParams;
	private String notification;
	private Integer totalTodoTaskScheduleCount;
	private Integer completedTodoTaskScheduleCount;

	@JsonIgnore
	private List<TodoDto> todos;

	public static TodoDto of(Todo todo) {
		if (todo instanceof TodoTask) {
			TodoTask todoTask = (TodoTask)todo;
			return TodoDto.builder()
				.id(todoTask.getTodoId())
				.title(todoTask.getTitle())
				.type("TASK")
				.depth(todoTask.getDepth())
				.orderNumber(todoTask.getOrderNumber())
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
			return TodoDto.builder()
				.title(todoFolder.getTitle())
				.depth(todoFolder.getDepth())
				.orderNumber(todoFolder.getOrderNumber())
				.build();
		}
	}
}
