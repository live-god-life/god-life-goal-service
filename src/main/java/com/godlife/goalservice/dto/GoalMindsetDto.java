package com.godlife.goalservice.dto;

import com.godlife.goalservice.domain.Goal;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GoalMindsetDto {
    private Long goalId;
    private String title;
    private List<MindsetDto> mindsets;

    public static GoalMindsetDto of(Goal goal) {
        return GoalMindsetDto.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .mindsets(goal.getMindsets().stream().map(MindsetDto::of).collect(Collectors.toList()))
                .build();
    }
}
