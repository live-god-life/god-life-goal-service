package com.godlife.goalservice.api.request;

import com.godlife.goalservice.service.dto.TodoServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGoalTodoRequest {
    private String title;
    private String type;
    private Integer depth;
    private Integer order;
    private String startDate;
    private String endDate;
    private String repetitionType;
    private List<String> repetitionParams;
    private String notification;
    private List<CreateGoalTodoRequest> todos;

    public TodoServiceDto toTodoServiceDto() {
        return TodoServiceDto.builder()
                .title(title)
                .type(type)
                .depth(depth)
                .order(order)
                .todos(Optional.ofNullable(todos).orElseGet(Collections::emptyList).stream().map(CreateGoalTodoRequest::toTodoServiceDto).collect(Collectors.toList()))
                .build();
    }
}
