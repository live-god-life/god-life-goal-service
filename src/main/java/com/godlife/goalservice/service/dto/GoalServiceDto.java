package com.godlife.goalservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Goal;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GoalServiceDto {
    private Long goalId;
    private String title;
    private Long userId;

    @JsonIgnore
    private List<MindsetServiceDto> mindsets;
    @JsonIgnore
    private List<TodoServiceDto> todos;

    public static GoalServiceDto of(Goal goal) {
        return GoalServiceDto.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .userId(goal.getUserId())
                .mindsets(goal.getMindsets().stream().map(MindsetServiceDto::of).collect(Collectors.toList()))
                .todos(goal.getTodos().stream().map(TodoServiceDto::of).collect(Collectors.toList()))
                .build();
    }

    public Goal toEntity(Long userId) {
        return Goal.builder()
                .goalId(goalId)
                .title(title)
                .userId(userId)
                .mindsets(mindsets.stream().map(MindsetServiceDto::toEntity).collect(Collectors.toList()))
                .todos(todos.stream().map(TodoServiceDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
