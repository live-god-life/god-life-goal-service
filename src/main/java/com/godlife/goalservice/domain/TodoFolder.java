package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;


@DiscriminatorValue("folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoFolder extends Todo {
    @Builder
    public TodoFolder(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos) {
        super(todoId, title, depth, orderNumber, childTodos);
    }
}

