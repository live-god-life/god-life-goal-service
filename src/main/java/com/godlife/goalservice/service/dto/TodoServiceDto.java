package com.godlife.goalservice.service.dto;

import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.RepetitionType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class TodoServiceDto {
    private String title;
    private String type;
    private Integer depth;
    private Integer order;
    private List<TodoServiceDto> todos;
    private String startDate;
    private String endDate;

    private String repetitionType;
    private List<String> repetitionParams;

    public static TodoServiceDto of(Todo todo) {
        return TodoServiceDto.builder()
                .title(todo.getTitle())
                .depth(todo.getDepth())
                .order(todo.getOrderNumber())
                .build();
    }

    public Todo toEntity() {
        if (type.equals("folder")) {
            return TodoFolder.createTodoFolder(title, depth, orderNumber, todos.stream().map(TodoServiceDto::toEntity).collect(Collectors.toList()));
        } else {
            return TodoTask.createTodoTask(title,
                            depth,
                            orderNumber,
                            (LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd"))),
                            LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
                            RepetitionType.valueOf(repetitionType),
                            repetitionParams
                    );
        }
    }
}
