package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;


@DiscriminatorValue("folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoFolder extends Todo {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_todo_id")
    private List<Todo> childTodos;

    private TodoFolder(String title, Integer depth, Integer orderNumber, List<Todo> childTodos) {
        super(title, depth, orderNumber);
        this.childTodos = childTodos;
    }

    public static TodoFolder createTodoFolder(String title, Integer depth, Integer orderNumber, List<Todo> childTodos) {
        return new TodoFolder(title, depth, orderNumber, childTodos);
    }
}

