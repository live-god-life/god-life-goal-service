package com.godlife.goalservice.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoalTodoScheduleDto {
    private LocalDate date;
    private Integer todoCount;

    @QueryProjection
    public GoalTodoScheduleDto(LocalDate date, Integer todoCount) {
        this.date = date;
        this.todoCount = todoCount;
    }

    @QueryProjection
    public GoalTodoScheduleDto(LocalDate date) {
        this.date = date;
    }
}
