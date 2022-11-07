package com.godlife.goalservice.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    todo
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

	@Column(insertable = false, updatable = false)
	private String type;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Integer depth;

	@Column(nullable = false)
	private Integer orderNumber;

	@Comment("Task 시작일")
	private LocalDate startDate;
	@Comment("Task 종료일")
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "goal_id")
	private Goal goal;

	protected Todo(String title, Integer depth, Integer orderNumber, Goal goal) {
		this.title = title;
		this.depth = depth;
		this.orderNumber = orderNumber;
		this.goal = goal;
	}

	public Todo(String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate, Goal goal) {
		this.title = title;
		this.depth = depth;
		this.orderNumber = orderNumber;
		this.startDate = startDate;
		this.endDate = endDate;
		this.goal = goal;
	}
}
