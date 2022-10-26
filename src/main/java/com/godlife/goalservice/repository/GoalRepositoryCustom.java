package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.service.dto.GoalTodoScheduleDto;

import java.time.YearMonth;
import java.util.List;

public interface GoalRepositoryCustom {
    List<Goal> findDailyGoalsAndLowestDepthTodosByUserId(Long userId);

    List<GoalTodoScheduleDto> findDailyTodosCount(Long userId, YearMonth yearMonth);
}
