package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
    todo
    - 완료유무
    - 시작일 구하기(todos 시작일중 제일 빠른 날짜)
    - 종료일 구하기(todos 종료일중 제일 느린 날짜)
    - D-Day 구하기(오늘부터 종료일까지)
    - 진행중 투두 카운팅(최상위 뎁스의 완료 유무로 확인)
    - 완료된 투두 카운팅(최상위 뎁스의 완료 유무로 확인)

    - 진행중, 완료 투두 카운팅은 완료체크 개발 후 개발
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Comment("사용자아이디")
    @Column(nullable = false)
    private Long userId;

    @Comment("카테고리")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Comment("제목")
    @Column(nullable = false)
    private String title;

    @Comment("완료유무")
    private Boolean completedStatus;

    @Comment("HEX 색상코드")
    private String hexColorCode;

    @Comment("정렬순서")
    private Integer orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private List<Mindset> mindsets;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    //===목표 시작일 종료일===
    private LocalDate startDate;
    private LocalDate endDate;

    //===카운팅 통계 데이터===
    @Comment("마인드셋 카운트")
    private int totalMindsetCount;
    @Comment("진행중 투두 카운트")
    private int onProgressTodoCount;
    @Comment("완료됨 카운트")
    private int completedTodoCount;
    private int totalTodoCount;

    private Goal(Long userId, Category category, String title, Integer orderNumber, List<Mindset> mindsets) {
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.orderNumber = orderNumber;
        this.mindsets = mindsets;
    }

    public static Goal createGoal(Long userId, Category category, String title, Integer orderNumber, List<Mindset> mindsets, List<Todo> todos) {
        Goal goal = new Goal(userId, category, title, orderNumber, mindsets);
        goal.setTodos(todos);
        goal.setMindsetTotalCount(mindsets.size());
        return goal;
    }

    private void setTodos(List<Todo> todos) {
        for (Todo todo : todos) {
            addTodo(todo);
            if (todo.getDepth() == 1) {
                totalTodoCount++;
            }
        }
    }

    private void setMindsetTotalCount(int totalMindsetCount) {
        this.totalMindsetCount = totalMindsetCount;
    }

    private void addTodo(Todo todo) {
        todo.setGoal(this);
        if (todo instanceof TodoFolder) {
            if (Objects.nonNull(((TodoFolder) todo).getChildTodos())) {
                ((TodoFolder) todo).getChildTodos().forEach(todo1 -> todo1.setGoal(this));
            }
        }
        this.todos.add(todo);
    }

    public int getMindsetTotalCount() {
        return mindsets.size();
    }

    public int getProgressCount() {
        return 0;
    }

    public int getCompletedCount() {
        return 0;
    }
}
