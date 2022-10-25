package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@DiscriminatorValue("folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoFolder extends Todo {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_todo_id")
    private List<Todo> childTodos;

    @Builder
    public TodoFolder(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos) {
        super(todoId, title, depth, orderNumber);
        this.childTodos = childTodos;
    }
}

