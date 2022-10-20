package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTask extends Todo {
    @Comment("Task 시작일")
    private LocalDateTime startDate;
    @Comment("Task 종료일")
    private LocalDateTime endDate;

    //todo 일단 스트링 -> enum
    @Comment("완료 유무")
    private String completionStatus;

    @Builder
    public TodoTask(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos, LocalDateTime startDate, LocalDateTime endDate) {
        super(todoId, title, depth, orderNumber, childTodos);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
