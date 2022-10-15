package com.godlife.goalservice.service.dto;

import com.godlife.goalservice.domain.Goal;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public class GoalServiceDto {
    private Long goalId;
    private String title;
    private Long userId;
    private List<MindsetServiceDto> mindsets;

    public static GoalServiceDto of(Goal goal) {
        return GoalServiceDto.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .userId(goal.getUserId())
                .mindsets(goal.getMindsets().stream().map(mindset -> MindsetServiceDto.of(mindset)).collect(Collectors.toList()))
                .build();
    }
}
