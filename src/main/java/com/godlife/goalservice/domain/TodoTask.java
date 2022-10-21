package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;
/*
    todo
    기간 어떤식으로 풀지?
    알림은 0900 String 형태가 좋을까?
 */
@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTask extends Todo {
    @Comment("Task 시작일")
    private LocalDateTime startDate;
    @Comment("Task 종료일")
    private LocalDateTime endDate;

    @Comment("기간 type")
    private String repetitionType;
    @Comment("기간 파라미터")
    private String repetitionParams;

    @Comment("알림")
    private String notification;

    @Builder
    public TodoTask(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos, LocalDateTime startDate, LocalDateTime endDate) {
        super(todoId, title, depth, orderNumber, childTodos);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
