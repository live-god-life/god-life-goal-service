package com.godlife.goalservice.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.TodoTaskSchedule;
import com.godlife.goalservice.domain.Todos;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;
import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.GoalMindsetsTodosDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.dto.request.CreateGoalMindsetRequest;
import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.dto.request.CreateGoalTodoRequest;
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
		Goal goal = Goal.createGoal(
			userId,
			Category.valueOf(goalMindsetsTodosDto.getCategoryCode()),
			goalMindsetsTodosDto.getTitle()
		);
		List<Mindset> mindsets = createMindsets(goalMindsetsTodosDto.getMindsets(), goal);
		Todos todos = createTodos(goalMindsetsTodosDto.getTodos(), goal);

		//시작일, 종료일
		goal.registerStartDate(todos.getFistStartDate());
		goal.registerEndDate(todos.getLastEndDate());
		//마인드셋 카운팅
		goal.registerMindsetCount(mindsets.size());
		//진행중 완료 투두 카운팅
		goal.registerTotalTodoTaskCount(todos.getTotalTodoTaskCount());
		//전체, 완료 스케줄 카운팅
		goal.registerTotalTodoTaskScheduleCount(todos.getTotalTodoTaskScheduleCount());

		mindsetRepository.saveAll(mindsets);
		todoRepository.saveAll(todos.get());

	}

	private List<Mindset> createMindsets(List<CreateGoalMindsetRequest> mindsetDtos, Goal goal) {
		return mindsetDtos.stream()
			.map(mindsetServiceDto -> Mindset.createMindset(mindsetServiceDto.getContent(), goal))
			.collect(Collectors.toList());
	}

	private Todos createTodos(List<CreateGoalTodoRequest> todoDtos, Goal goal) {
		return new Todos(todoDtos.stream()
			.map(todoDto -> createTodo(todoDto, goal))
			.collect(Collectors.toList()));
	}

	//TODO 자식 인스턴스를 만들때 뭔가 깔끔하지 못한데,,,, 다른 방법이 없나?
	private Todo createTodo(CreateGoalTodoRequest todoDto, Goal goal) {
		String type = todoDto.getType();
		if (type.equals("folder")) {
			return TodoFolder.createTodoFolder(
				todoDto.getTitle(),
				todoDto.getDepth(),
				todoDto.getOrderNumber(),
				todoDto.getTodos()
					.stream()
					.map(createGoalTodoRequest -> createTodo(createGoalTodoRequest, goal))
					.collect(Collectors.toList()),
				goal
			);
		} else {
			return TodoTask.createTodoTask(
				todoDto.getTitle(),
				todoDto.getDepth(),
				todoDto.getOrderNumber(),
				LocalDate.parse(todoDto.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				LocalDate.parse(todoDto.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				RepetitionType.valueOf(todoDto.getRepetitionType()),
				todoDto.getRepetitionParams(),
				goal
			);
		}
	}

	public List<TodoScheduleCountDto> getDailyTodosCount(Long userId, YearMonth yearMonth) {
		return goalRepository.findDailyTodosCount(userId, yearMonth);
	}

	public List<GoalMindsetDto> getGoalsWithMindsets(Long userId) {
		return goalRepository.findByUserId(userId).stream().map(GoalMindsetDto::of).collect(Collectors.toList());
	}

	public List<GoalMindsetsTodosDto> getGoals(Long userId) {
		List<Goal> goals = goalRepository.findByUserId(userId);
		return goals.stream().map(GoalMindsetsTodosDto::of).collect(Collectors.toList());
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
