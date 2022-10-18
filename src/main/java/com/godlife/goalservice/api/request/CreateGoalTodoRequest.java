package com.godlife.goalservice.api.request;

import com.godlife.goalservice.service.dto.TodoServiceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CreateGoalTodoRequest {
    private String title;
    private String type;
    private Integer depth;
    private Integer order;
    private List<CreateGoalTodoRequest> todos;

    public CreateGoalTodoRequest(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public CreateGoalTodoRequest(String title, String type, List<CreateGoalTodoRequest> todos) {
        this.title = title;
        this.type = type;
        this.todos = todos;
    }

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
