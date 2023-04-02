package com.godlife.goalservice.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Comment;

import com.godlife.goalservice.domain.enums.Category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    todo
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal extends BaseEntity {
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
	private Boolean completionStatus;

	@Comment("HEX 색상코드")
	private String hexColorCode;

	//===목표 시작일 종료일===
	private LocalDate startDate;
	private LocalDate endDate;

	//===카운팅 통계 데이터===
	@Comment("마인드셋 카운트")
	private Integer totalMindsetCount;
	@Comment("전체 투두 카운트")
	private Integer totalTodoCount;
	@Comment("완료된 투두 카운트")
	private Integer completedTodoCount;
	@Comment("전체 투두 일정 카운트")
	private Integer totalTodoTaskScheduleCount;
	@Comment("완료된 투두 일정 카운트")
	private Integer completedTodoTaskScheduleCount;

	private Goal(Long userId, Category category, String title) {
		this.userId = userId;
		this.category = category;
		this.title = title;
		this.completionStatus = Boolean.FALSE;
		this.totalMindsetCount = 0;
		this.totalTodoCount = 0;
		this.completedTodoCount = 0;
		this.totalTodoTaskScheduleCount = 0;
		this.completedTodoTaskScheduleCount = 0;
	}

	public static Goal createGoal(Long userId, Category category, String title) {
		return new Goal(userId, category, title);
	}

	public void addMindsetCount() {
		this.totalMindsetCount++;
	}

	public void registerTodosInfo(Todos todos) {
		registerStartDate(todos.getFistStartDate());
		registerEndDate(todos.getLastEndDate());
		registerTotalTodoTaskCount(todos.getTotalTodoTaskCount());
		registerTotalTodoTaskScheduleCount(todos.getTotalTodoTaskScheduleCount());
	}

	private void registerStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	private void registerEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	private void registerTotalTodoTaskCount(int totalTodoCount) {
		this.totalTodoCount = totalTodoCount;
	}

	private void registerTotalTodoTaskScheduleCount(int totalTodoTaskScheduleCount) {
		this.totalTodoTaskScheduleCount = totalTodoTaskScheduleCount;
	}

	public void plusCompletedTodoTaskScheduleCount() {
		this.completedTodoTaskScheduleCount++;
	}

	public void minusCompletedTodoTaskScheduleCount() {
		this.completedTodoTaskScheduleCount--;
	}

	public void changeTitle(String title) {
		this.title = title;
	}

	public void changeCategory(Category category) {
		this.category = category;
	}

	public void initMindSetCount() {
		this.totalMindsetCount = 0;
	}
}
