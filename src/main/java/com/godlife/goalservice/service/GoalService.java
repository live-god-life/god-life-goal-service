package com.godlife.goalservice.service;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.repository.GoalRepository;
import com.godlife.goalservice.service.dto.GoalServiceDto;
import com.godlife.goalservice.service.dto.UserDto;
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

    public List<GoalServiceDto> getGoalsWithMindsetsByMethodAndCount(String method, Integer count, String authorization) {
        List<Goal> goals = goalRepository.findByUserId(getUserInfo(authorization).getUserId());
        return goals.stream().map(GoalServiceDto::of).collect(Collectors.toList());
    }

    @Transactional
    public void createGoal(GoalServiceDto goalServiceDto) {
        Goal goal = goalServiceDto.toEntity();
        log.info("goal: {}", goal);
        goalRepository.save(goal);
    }

    public List<GoalServiceDto> getGoals(String authorization) {
        Long userId = getUserInfo(authorization).getUserId();

        List<Goal> goals = goalRepository.findByUserId(userId);

        return goals.stream().map(GoalServiceDto::of).collect(Collectors.toList());
    }

    private static UserDto getUserInfo(String authorization) {
        // todo auth-service 호출하여 jwt payload 담긴 유저정보 받아오기
        UserDto userDto = new UserDto();


        return userDto;
    }
}
