package com.godlife.goalservice.service;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.service.dto.GoalServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
}
