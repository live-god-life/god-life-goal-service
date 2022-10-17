package com.godlife.goalservice.service.dto;

import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import lombok.Builder;

import java.util.List;

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
//                .type(todo.getType())
                .depth(todo.getDepth())
                .order(todo.getOrderNumber())
                .build();
    }

    public Todo toEntity() {
        if (type.equals("folder")) {
            return TodoFolder.builder()
                    .title(title)
                    .build();
        } else {
            return TodoTask.builder()
                    .title(title)
                    .build();
        }
    }
}
