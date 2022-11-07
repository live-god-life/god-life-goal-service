package com.godlife.goalservice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.godlife.goalservice.domain.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>, GoalRepositoryCustom {
	List<Goal> findByUserId(Long userId);

	List<Goal> findAllByUserId(Pageable page, Long userId);

	List<Goal> findAllByUserIdAndCompletionStatus(Pageable page, Long userId, Boolean completionStatus);
}
