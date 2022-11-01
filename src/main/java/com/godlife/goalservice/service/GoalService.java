package com.godlife.goalservice.service;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoTaskSchedule;
import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.MindsetDto;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.dto.GoalDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.dto.TodoDto;
import com.godlife.goalservice.repository.TodoRepository;
import com.godlife.goalservice.repository.TodoTaskScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*  todo
    getGoalsWithMindsetsByMethodAndCount 와 로직이 동일해졌다..
    목표를 respository에서 가지고는 오는데, 해당 목표를 가공해야한다.
    가공은 어디서 하면 좋을까, 결국 각 API 마다 다른 가공을 원하는 것이다.
    API 스팩의 변경이 도메인, 서비스 레이어에 영향을 안주는게 가장 좋은 방법일거같은데
    음.....................................................
    일단 생각나는 대로 짜보고, 중복된 코드, 변경에 유연하지 못한 코드를 리팩토링해나가자, 지금 생각하기에는 지식이 부족하다
 */
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalService {
	private final GoalRepository goalRepository;
	private final TodoTaskScheduleRepository todoTaskScheduleRepository;
	private final TodoRepository todoRepository;

	@Transactional
	public void createGoal(Long userId, GoalDto goalDto) {
		Goal goal = Goal.createGoal(userId,
			goalDto.getCategory(),
			goalDto.getTitle(),
			goalDto.getOrderNumber(),
			createMindsets(goalDto.getMindsets()),
			createTodos(goalDto.getTodos()));
		goalRepository.save(goal);
	}

	private List<Todo> createTodos(List<TodoDto> todoDtos) {
		return todoDtos.stream()
			.map(TodoDto::toEntity)
			.collect(Collectors.toList());
	}

	private List<Mindset> createMindsets(List<MindsetDto> mindsetDtos) {
		return mindsetDtos.stream()
			.map(mindsetServiceDto -> Mindset.createMindset(mindsetServiceDto.getContent()))
			.collect(Collectors.toList());
	}

	public List<TodoScheduleCountDto> getDailyTodosCount(Long userId, YearMonth yearMonth) {
		return goalRepository.findDailyTodosCount(userId, yearMonth);
	}

	public List<GoalMindsetDto> getGoalsWithMindsets(Long userId) {
		return goalRepository.findByUserId(userId).stream().map(GoalMindsetDto::of).collect(Collectors.toList());
	}

	public List<GoalDto> getGoals(Long userId) {
		List<Goal> goals = goalRepository.findByUserId(userId);
		return goals.stream().map(GoalDto::of).collect(Collectors.toList());
	}

	public List<GoalTodoScheduleDto> getDailyGoalsAndTodos(Long userId, LocalDate date, Boolean completionStatus,
		Pageable page) {
		return goalRepository.findDailyGoalsAndTodosByUserIdAndLocalDate(userId, date);
	}

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

	public TodoDto getTodoDetail(Long userId, Long todoId) {
		return TodoDto.of(todoRepository.findById(todoId)
			.orElseThrow(NoSuchElementException::new));
	}
}
