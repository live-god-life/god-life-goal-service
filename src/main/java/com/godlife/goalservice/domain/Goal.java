package com.godlife.goalservice.domain;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.List;

/*
    todo
    - 완료유무
    - 시작일 구하기(todos 시작일중 제일 빠른 날짜)
    - 종료일 구하기(todos 종료일중 제일 느린 날짜)
    - D-Day 구하기(오늘부터 종료일까지)
    - 진행중 투두 카운팅
    - 완료된 투두 카운팅
 */

@EqualsAndHashCode(callSuper=false)
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long goalId;

    @Comment("제목")
    private String title;

    @Comment("사용자아이디")
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

    public int getMindsetTotalCount() {
        return mindsets.size();
    }
}
