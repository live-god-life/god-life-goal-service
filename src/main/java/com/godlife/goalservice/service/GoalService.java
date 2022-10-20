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
/*  todo
    getGoalsWithMindsetsByMethodAndCount 와 로직이 동일해졌다..
    목표를 respository에서 가지고는 오는데, 해당 목표를 가공해야한다.
    가공은 어디서 하면 좋을까, 결국 각 API 마다 다른 가공을 원하는 것이다.
    API 스팩의 변경이 도메인, 서비스 레이어에 영향을 안주는게 가장 좋은 방법일거같은데
    음.....................................................
    일단 생각나는 대로 짜보고, 중복된 코드, 변경에 유연하지 못한 코드를 리팩토링해나가자, 지금 생각하기에는 지식이 부족하다

    todo
    auth-service 호출하여 jwt payload 담긴 유저정보 받아오기
 */
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
    public void createGoal(String authorization, GoalServiceDto goalServiceDto) {
        Long userId = getUserInfo(authorization).getUserId();
        Goal goal = goalServiceDto.toEntity(userId);
        goalRepository.save(goal);
    }

    public List<GoalServiceDto> getGoals(String authorization) {
        Long userId = getUserInfo(authorization).getUserId();
        List<Goal> goals = goalRepository.findByUserId(userId);
        log.info("goals: {}",goals);
        return goals.stream().map(GoalServiceDto::of).collect(Collectors.toList());
    }

    private static UserDto getUserInfo(String authorization) {
        UserDto userDto = new UserDto(1L);
        return userDto;
    }
}
