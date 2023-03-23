package com.godlife.goalservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;

public interface MindsetRepository extends JpaRepository<Mindset, Long> {
	List<Mindset> findAllByGoal(Goal goal);
}
