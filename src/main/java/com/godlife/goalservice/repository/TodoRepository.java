package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
