package com.godlife.goalservice.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@Getter
//TODO 엔티티 - 테이블 상속관계 전략을 SINGLE_TABLE로 설정, 추후 변경가능하지만 코드의 복잡성을 생각하면 SINGLE_TABLE이 좋아보임, 보류
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
