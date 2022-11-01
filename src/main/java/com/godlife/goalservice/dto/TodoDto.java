package com.godlife.goalservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.RepetitionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TodoDto {
    private Long id;
    private String title;
    private String type;
    private Integer depth;
    private Integer orderNumber;
    private String startDate;
    private String endDate;
    private String repetitionType;
    private List<String> repetitionParams;
    private String notification;
    private Integer totalTodoTaskScheduleCount;
    private Integer completedTodoTaskScheduleCount;

    @JsonIgnore
    private List<TodoDto> todos;

    public static TodoDto of(Todo todo) {
        if (todo instanceof TodoTask) {
            TodoTask todoTask = (TodoTask) todo;
            return TodoDto.builder()
                    .id(todoTask.getTodoId())
                    .title(todoTask.getTitle())
                    .type("TASK")
                    .depth(todoTask.getDepth())
                    .orderNumber(todoTask.getOrderNumber())
                    .startDate(todoTask.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .endDate(todoTask.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .repetitionType(todoTask.getRepetitionType().toString())
                    .repetitionParams(todoTask.getRepetitionParams())
                    .notification(todoTask.getNotification())
                    .totalTodoTaskScheduleCount(todoTask.getTotalTodoTaskScheduleCount())
                    .completedTodoTaskScheduleCount(todoTask.getCompletedTodoTaskScheduleCount())
                    .build();
        } else {
            TodoFolder todoFolder = (TodoFolder) todo;
            return TodoDto.builder()
                    .title(todoFolder.getTitle())
                    .depth(todoFolder.getDepth())
                    .orderNumber(todoFolder.getOrderNumber())
                    .build();
        }
    }

    public Todo toEntity() {
        if (type.equals("folder")) {
            return TodoFolder.createTodoFolder(title, depth, orderNumber, todos.stream().map(TodoDto::toEntity).collect(Collectors.toList()));
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
