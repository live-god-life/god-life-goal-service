package com.godlife.goalservice.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.TodoTaskSchedule;
import com.godlife.goalservice.domain.Todos;
import com.godlife.goalservice.dto.GoalDto;
import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.GoalMindsetsTodosDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoDetailDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.exception.NoSuchTodoException;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.repository.MindsetRepository;
import com.godlife.goalservice.repository.TodoRepository;
import com.godlife.goalservice.repository.TodoTaskScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
	todo
	- 목표:마인드셋,목표:투두 관계를 다대일단방향으로 수정하였다.
	  목표쪽 로직은 단순해진거같지만 서비스에 엔티티를 생성하는 로직이 너무 복잡해보인다.
	  기존 일대다 단방향의 경우 한번에 목표를 레파지토리에 넣으면 되었지만
	  현재는 연관관계의 주인을 레파지토리에 넣어야하므로 복잡해진거같다.
	  다대일 양방향이 좋을까???
	  -> dto에 entity생성 메서드 추가 해봄
 */
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalService {
	private final GoalRepository goalRepository;
	private final TodoTaskScheduleRepository todoTaskScheduleRepository;
	private final TodoRepository todoRepository;
	private final MindsetRepository mindsetRepository;

	@Transactional
	public void createGoal(Long userId, CreateGoalRequest goalMindsetsTodosDto) {
		Goal goal = goalMindsetsTodosDto.createGoalEntity(userId);
		List<Mindset> mindsets = goalMindsetsTodosDto.createMindsetsEntity(goal);
		Todos todos = new Todos(goalMindsetsTodosDto.createTodosEntity(goal));

		goal.registerTodosInfo(todos);

		mindsetRepository.saveAll(mindsets);
		todoRepository.saveAll(todos.get());
	}

	public List<GoalDto> getGoals(Pageable page, Long userId, Boolean completionStatus) {
		return Objects.isNull(completionStatus) ?
			goalRepository.findAllByUserId(page, userId).stream().map(GoalDto::of).collect(Collectors.toList()) :
			goalRepository.findAllByUserIdAndCompletionStatus(page, userId, completionStatus).stream().map(GoalDto::of).collect(Collectors.toList());
	}

	public List<GoalMindsetDto> getGoalsWithMindsets(Pageable page, Long userId, Boolean completionStatus) {
		return goalRepository.findGoalsAndMindsetsByUserIdAndCompletionStatus(page, userId, completionStatus);
	}

	public List<TodoScheduleCountDto> getDailyTodosCount(Long userId, YearMonth yearMonth) {
		return goalRepository.findDailyTodosCount(userId, yearMonth);
	}

	public List<GoalTodoScheduleDto> getDailyGoalsAndTodos(Pageable page, Long userId, LocalDate searchedDate, Boolean completionStatus) {

		return goalRepository.findDailyGoalsAndTodosByUserIdAndLocalDate(page, userId, searchedDate, completionStatus);
	}

	public TodoDetailDto getTodoDetail(Long userId, Long todoId) {
		return TodoDetailDto.of(todoRepository.findById(todoId).orElseThrow(NoSuchTodoException::new));
	}

	public GoalMindsetsTodosDto getGoalDetail(Long userId, Long goalId) {
		return goalRepository.findGoalWithMindsetsAndTodosByUserIdAndGoalId(userId, goalId);
	}

	//======================================리팩토링 완료======================================

	@Transactional
	public void updateTodoScheduleCompletionStatus(Long userId, Long todoScheduleId, Boolean completionStatus) {
		TodoTaskSchedule schedule = todoTaskScheduleRepository.findById(todoScheduleId)
			.orElseThrow(NoSuchElementException::new);
		if (completionStatus) {
			schedule.updateCompletionStatus();
		} else {
			schedule.updateInCompletionStatus();
		}
	}
}
