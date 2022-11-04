package com.godlife.goalservice.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.GoalMindsetsTodosDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;

public interface GoalRepositoryCustom {
	List<GoalTodoScheduleDto> findDailyGoalsAndTodosByUserIdAndLocalDate(Pageable page, Long userId, LocalDate searchedDate, Boolean completionStatus);

	List<TodoScheduleCountDto> findDailyTodosCount(Long userId, YearMonth yearMonth);

	List<GoalMindsetDto> findGoalsAndMindsetsByUserIdAndCompletionStatus(Pageable page, Long userId, Boolean completionStatus);

	GoalMindsetsTodosDto findGoalWithMindsetsAndTodosByUserIdAndGoalId(Long userId, Long goalId);
}
