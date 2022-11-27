package com.godlife.goalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godlife.goalservice.domain.TodoTaskSchedule;

public interface TodoTaskScheduleRepository extends JpaRepository<TodoTaskSchedule, Long>, TodoTaskScheduleRepositoryCustom {

}
