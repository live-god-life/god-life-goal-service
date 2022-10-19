package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTask extends Todo {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public TodoTask(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos, LocalDateTime startDate, LocalDateTime endDate) {
        super(todoId, title, depth, orderNumber, childTodos);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
