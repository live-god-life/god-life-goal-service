package com.godlife.goalservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

	List<Todo> findAllByGoal(Goal goal);
}
