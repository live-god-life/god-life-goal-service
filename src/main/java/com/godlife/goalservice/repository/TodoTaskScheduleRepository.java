package com.godlife.goalservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.TodoTaskSchedule;

public interface TodoTaskScheduleRepository extends JpaRepository<TodoTaskSchedule, Long>, TodoTaskScheduleRepositoryCustom {
	List<TodoTaskSchedule> findAllByTodoTaskTodoId(Long todoId);

	void deleteByTodoTask(TodoTask todoTask);
}
