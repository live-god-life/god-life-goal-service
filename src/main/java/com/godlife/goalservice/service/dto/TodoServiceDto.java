package com.godlife.goalservice.service.dto;

import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public class TodoServiceDto {
    private String title;
    private String type;
    private Integer depth;
    private Integer order;
    private List<TodoServiceDto> todos;

    public static TodoServiceDto of(Todo todo) {
        return TodoServiceDto.builder()
                .title(todo.getTitle())
                .depth(todo.getDepth())
                .order(todo.getOrderNumber())
                .build();
    }

    public Todo toEntity() {
        if (type.equals("folder")) {
            return TodoFolder.builder()
                    .title(title)
                    .childTodos(todos.stream().map(TodoServiceDto::toEntity).collect(Collectors.toList()))
                    .build();
        } else {
            return TodoTask.builder()
                    .title(title)
                    .build();
        }
    }
}
