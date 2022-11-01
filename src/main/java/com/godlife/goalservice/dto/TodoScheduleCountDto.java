package com.godlife.goalservice.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoScheduleCountDto {
	private LocalDate date;
	private Integer todoCount;

	@QueryProjection
	public TodoScheduleCountDto(LocalDate date, Integer todoCount) {
		this.date = date;
		this.todoCount = todoCount;
	}
}
