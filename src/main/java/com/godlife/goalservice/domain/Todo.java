package com.godlife.goalservice.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public abstract class Todo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;
    private String title;
    private Integer depth;
    private Integer orderNumber;

    //TODO 양방향 단방향에 대해서는 고민좀 하고 결정, 우선 일대다 단방향으로 설정
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_todo_id")
    private List<Todo> childTodos;

    protected Todo(Long todoId, String title, Integer depth, Integer orderNumber, List<Todo> childTodos) {
        this.todoId = todoId;
        this.title = title;
        this.depth = depth;
        this.orderNumber = orderNumber;
        this.childTodos = childTodos;
    }
}
