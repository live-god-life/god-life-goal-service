package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*
    todo
    엔티티 - 테이블 상속관계 전략을 SINGLE_TABLE로 설정, 추후 변경가능하지만 코드의 복잡성을 생각하면 SINGLE_TABLE이 좋아보임, 보류
    childTodos: 양방향 단방향에 대해서는 고민좀 하고 결정, 우선 일대다 단방향으로 설정

    완료체크할때 날짜별, todo의 상태는 어떻게 저장할까?
 */

@EqualsAndHashCode(callSuper = false)
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public abstract class Todo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long todoId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Integer depth;

	@Column(nullable = false)
	private Integer orderNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goal_id")
	private Goal goal;

	//===연관관계 편의 메서드===
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	protected Todo(String title, Integer depth, Integer orderNumber) {
		this.title = title;
		this.depth = depth;
		this.orderNumber = orderNumber;
	}
}
