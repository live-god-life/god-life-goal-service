package com.godlife.goalservice.repository;

import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface GoalRepositoryCustom {
	List<GoalTodoScheduleDto> findDailyGoalsAndTodosByUserIdAndLocalDate(Long userId, LocalDate localDate);

	List<TodoScheduleCountDto> findDailyTodosCount(Long userId, YearMonth yearMonth);

	List<GoalMindsetDto> findGoalsAndMindsetsByUserIdAndCompletionStatus(Pageable page, Long userId, Boolean completionStatus);
}
