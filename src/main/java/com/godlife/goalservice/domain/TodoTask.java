package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.converter.StringListConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
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
    private LocalDate startDate;
    @Comment("Task 종료일")
    private LocalDate endDate;

    @Comment("기간 type")
    private String repetitionType;

    @Convert(converter = StringListConverter.class)
    @Comment("기간 파라미터")
    private List<String> repetitionParams;

    @Comment("알림")
    private String notification;

    @Builder
    public TodoTask(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos, LocalDate startDate, LocalDate endDate, String repetitionType, List<String> repetitionParams) {
        super(todoId, title, depth, orderNumber, childTodos);
        this.startDate = startDate;
        this.endDate = endDate;
        this.repetitionType = repetitionType;
        this.repetitionParams = repetitionParams;
    }
}
