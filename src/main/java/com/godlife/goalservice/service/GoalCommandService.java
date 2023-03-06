package com.godlife.goalservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoTaskSchedule;
import com.godlife.goalservice.domain.Todos;
import com.godlife.goalservice.dto.request.CreateGoalRequest;
import com.godlife.goalservice.exception.NoSuchTodoException;
import com.godlife.goalservice.exception.NoSuchTodoScheduleException;
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
}
