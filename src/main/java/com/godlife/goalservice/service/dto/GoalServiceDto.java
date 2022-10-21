package com.godlife.goalservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.utils.mapper.MindsetMapper;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
/*
    todo
    mindsetCount, onProgressCount, completedCount 등은 API를 위해 데이터를 가공하는일인데 어디서 하는게 맞을까...
    일단 서비스 DTO 에서 진행
    음 또 생각해보니 서비스DTO의 역할이 너무 커지는거 같기도하고,,,,,,

    - MapStruct 적용하기
 */

@Data
@Builder
public class GoalServiceDto {
    private Long goalId;
    private String title;
    private Long userId;

    private Category category;

    private int mindsetCount;
    private int onProgressCount;
    private int completedCount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @JsonIgnore
    private List<MindsetServiceDto> mindsets;
    @JsonIgnore
    private List<TodoServiceDto> todos;

    public static GoalServiceDto of(Goal goal) {
        return GoalServiceDto.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .userId(goal.getUserId())
                .mindsetCount(goal.getMindsetTotalCount())
                .onProgressCount(goal.getProgressCount())
                .completedCount(goal.getCompletedCount())
                .mindsets(goal.getMindsets().stream().map(MindsetServiceDto::of).collect(Collectors.toList()))
                .todos(goal.getTodos().stream().map(TodoServiceDto::of).collect(Collectors.toList()))
                .build();
    }

    public Goal toEntity(Long userId) {
        return Goal.builder()
                .goalId(goalId)
                .title(title)
                .category(category)
                .userId(userId)
                .mindsets(mindsets.stream().map(MindsetMapper.INSTANCE::serviceDtoToEntity).collect(Collectors.toList()))
                .todos(todos.stream().map(TodoServiceDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
