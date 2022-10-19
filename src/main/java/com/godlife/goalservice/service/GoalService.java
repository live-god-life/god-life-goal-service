package com.godlife.goalservice.service;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.service.dto.GoalServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;

    public List<GoalServiceDto> getGoalsWithMindsetsByMethodAndCount(String method, Integer count) {
        /* todo token 풀기 */
        Long userId = 1L;
        /* =============== */

        List<Goal> goals = goalRepository.findByUserId(userId);

        return goals.stream().map(goal -> GoalServiceDto.of(goal)).collect(Collectors.toList());
    }

    @Transactional
    public void createGoal(GoalServiceDto goalServiceDto) {
        Goal goal = goalServiceDto.toEntity();
        log.info("goal: {}", goal);
        goalRepository.save(goal);
    }
}
