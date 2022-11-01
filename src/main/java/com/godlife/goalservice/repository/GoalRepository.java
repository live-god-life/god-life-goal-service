package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Goal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, GoalRepositoryCustom {
	List<Goal> findByUserId(Long userId);
}
