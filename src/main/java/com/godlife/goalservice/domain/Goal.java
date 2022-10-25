package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.enums.Category;
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
    - 진행중 투두 카운팅(최상위 뎁스의 완료 유무로 확인)
    - 완료된 투두 카운팅(최상위 뎁스의 완료 유무로 확인)

    진행중, 완료 투두 카운팅은 완료체크 개발 후 개발
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;
    @Comment("사용자아이디")
    private Long userId;

    @Comment("카테고리")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Comment("제목")
    private String title;

    @Comment("완료유무")
    private String completedStatus;

    @Comment("HEX 색상코드")
    private String hexColorCode;

    @Comment("정렬순서")
    private Integer orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private List<Mindset> mindsets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private List<Todo> todos;

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
