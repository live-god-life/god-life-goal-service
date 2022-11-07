package com.godlife.goalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godlife.goalservice.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
