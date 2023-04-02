package com.godlife.goalservice.dto;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class TodoDto {
	//===투두 공통===
	private Long todoId;
	private Long parentTodoId;
	private String type;
	private String title;
	private Integer depth;
	private Integer orderNumber;
	@JsonFormat(pattern = "yyyyMMdd")
	private String startDate;
	@JsonFormat(pattern = "yyyyMMdd")
	private String endDate;

	//===투두 타스크===
	private String repetitionType;
	private List<String> repetitionParams;
	private String notification;
	private Integer totalTodoTaskScheduleCount;
	private Integer completedTodoTaskScheduleCount;

	//===투두 폴더===
	private List<TodoDto> childTodos;

	@QueryProjection
	public TodoDto(Long todoId, String type, String title, Integer depth, Integer orderNumber, String startDate, String endDate, String repetitionType, List<String> repetitionParams,
		String notification, Integer totalTodoTaskScheduleCount, Integer completedTodoTaskScheduleCount) {

		this.todoId = todoId;
		this.type = type;
		this.title = title;
		this.depth = depth;
		this.orderNumber = orderNumber;
		this.startDate = StringUtils.delete(startDate, "-");
		this.endDate = StringUtils.delete(endDate, "-");
		this.repetitionType = repetitionType;
		this.repetitionParams = repetitionParams;
		this.notification = notification;
		this.totalTodoTaskScheduleCount = totalTodoTaskScheduleCount;
		this.completedTodoTaskScheduleCount = completedTodoTaskScheduleCount;
	}

	@QueryProjection
	public TodoDto(Long todoId, Long parentTodoId, String type, String title, Integer depth, Integer orderNumber, String startDate, String endDate, String repetitionType,
		List<String> repetitionParams,
		String notification, Integer totalTodoTaskScheduleCount, Integer completedTodoTaskScheduleCount) {
		this.todoId = todoId;
		this.parentTodoId = parentTodoId;
		this.type = type;
		this.title = title;
		this.depth = depth;
		this.orderNumber = orderNumber;
		this.startDate = startDate;
		this.endDate = endDate;
		this.repetitionType = repetitionType;
		this.repetitionParams = repetitionParams;
		this.notification = notification;
		this.totalTodoTaskScheduleCount = totalTodoTaskScheduleCount;
		this.completedTodoTaskScheduleCount = completedTodoTaskScheduleCount;
	}

	public void registerChildTodos(List<TodoDto> todoDtos) {
		this.totalTodoTaskScheduleCount = todoDtos.stream()
			.mapToInt(TodoDto::getTotalTodoTaskScheduleCount)
			.sum();
		this.completedTodoTaskScheduleCount = todoDtos.stream()
			.mapToInt(TodoDto::getCompletedTodoTaskScheduleCount)
			.sum();
		this.childTodos = todoDtos;
	}
}
