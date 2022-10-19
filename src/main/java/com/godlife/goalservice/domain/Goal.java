package com.godlife.goalservice.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private String title;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private List<Mindset> mindsets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private List<Todo> todos;

    @Builder
    public Goal(Long goalId, String title, Long userId, List<Mindset> mindsets, List<Todo> todos) {
        this.goalId = goalId;
        this.title = title;
        this.userId = userId;
        this.mindsets = mindsets;
        this.todos = todos;
    }
}
