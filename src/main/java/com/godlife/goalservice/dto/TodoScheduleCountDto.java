package com.godlife.goalservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class TodoScheduleCountDto {
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate date;
	private Integer todoCount;
	@JsonProperty("dDayCount")
	private Integer dDayCount;

	private Integer completedCount;

	@QueryProjection
	public TodoScheduleCountDto(LocalDate date, Integer todoCount, Integer dDayCount, Integer completedCount) {
		this.date = date;
		this.todoCount = todoCount;
		this.dDayCount = dDayCount;
		this.completedCount = completedCount;
	}
}
