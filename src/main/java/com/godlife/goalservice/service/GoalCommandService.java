package com.godlife.goalservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.TodoTaskSchedule;
import com.godlife.goalservice.domain.Todos;
import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.exception.NoSuchGoalException;
import com.godlife.goalservice.exception.NoSuchTodoException;
import com.godlife.goalservice.exception.NoSuchTodoScheduleException;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.repository.MindsetRepository;
import com.godlife.goalservice.repository.TodoRepository;
import com.godlife.goalservice.repository.TodoTaskScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class GoalCommandService {
	private final GoalRepository goalRepository;
	private final MindsetRepository mindsetRepository;
	private final TodoRepository todoRepository;
	private final TodoTaskScheduleRepository todoTaskScheduleRepository;

	public void createGoal(Long userId, CreateGoalRequest goalMindsetsTodosDto) {
		Goal goal = goalMindsetsTodosDto.createGoalEntity(userId);
		List<Mindset> mindsets = goalMindsetsTodosDto.createMindsetsEntity(goal);
		Todos todos = new Todos(goalMindsetsTodosDto.createTodosEntity(goal));

		goal.registerTodosInfo(todos);

		mindsetRepository.saveAll(mindsets);
		todoRepository.saveAll(todos.get());
	}

	public void updateTodoScheduleCompletionStatus(Long userId, Long todoScheduleId, Boolean completionStatus) {
		TodoTaskSchedule schedule = todoTaskScheduleRepository.findById(todoScheduleId)
			.orElseThrow(NoSuchTodoScheduleException::new);

		if (completionStatus) {
			schedule.updateCompletionStatus();
		} else {
			schedule.updateInCompletionStatus();
		}
	}

	public void deleteTodo(Long userId, Long todoId) {
		Todo todo = todoRepository.findById(todoId).orElseThrow(NoSuchTodoException::new);
		List<TodoTaskSchedule> todoTaskSchedules = todoTaskScheduleRepository.findAllByTodoTaskTodoId(todoId);
		todoTaskScheduleRepository.deleteAll(todoTaskSchedules);
		todoRepository.delete(todo);
	}

	@Transactional
	public void deleteGoal(Long userId, Long goalId) {
		Goal goal = goalRepository.findById(goalId).orElseThrow(NoSuchGoalException::new);
		deleteMindsets(goal);
		deleteTodos(goal);
		goalRepository.delete(goal);
	}

	private void deleteTodos(Goal goal) {
		List<Todo> todos = todoRepository.findAllByGoal(goal);
		todos.stream().filter((todo -> todo instanceof TodoTask)).forEach(todoTask -> todoTaskScheduleRepository.deleteByTodoTask((TodoTask)todoTask));
		todos.forEach(todoRepository::delete);
	}

	private void deleteMindsets(Goal goal) {
		List<Mindset> mindsets = mindsetRepository.findAllByGoal(goal);
		mindsets.forEach(mindsetRepository::delete);
	}

	@Transactional
	public void modifyGoal(Long userId, Long goalId, CreateGoalRequest request) {
		Goal goal = goalRepository.findById(goalId).orElseThrow(NoSuchGoalException::new);
		deleteMindsets(goal);
		deleteTodos(goal);
		List<Mindset> mindsets = request.createMindsetsEntity(goal);
		Todos todos = new Todos(request.createTodosEntity(goal));
		goal.registerTodosInfo(todos);
		mindsetRepository.saveAll(mindsets);
		todoRepository.saveAll(todos.get());
	}
}
