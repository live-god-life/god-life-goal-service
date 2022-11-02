package com.godlife.goalservice.dto;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

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
