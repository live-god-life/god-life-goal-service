package com.godlife.goalservice.dto;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class TodoSchedulesDto {
	private Long todoScheduleId;
	private String title;
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate scheduleDate;
	private String dayOfWeek;
	private Boolean completionStatus;

	@QueryProjection
	public TodoSchedulesDto(Long todoScheduleId, String title, LocalDate scheduleDate, Boolean completionStatus) {
		this.todoScheduleId = todoScheduleId;
		this.title = title;
		this.scheduleDate = scheduleDate;
		this.dayOfWeek = scheduleDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA);
		this.completionStatus = completionStatus;
	}
}
