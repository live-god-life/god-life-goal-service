package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Comment;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTaskSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long todoTaskScheduleId;

	@Comment("일정")
	private LocalDate scheduleDate;

	@Comment("완료여부")
	private Boolean completionStatus = false;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "todo_task_id")
	private TodoTask todoTask;

	public TodoTaskSchedule(LocalDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	//===연관관계 편의 메서드===
	public void setTodoTask(TodoTask todoTask) {
		this.todoTask = todoTask;
	}

	//===비즈니스 메서드===
	public void updateCompletionStatus() {
		if (!completionStatus) {
			this.completionStatus = true;
			todoTask.plusCompletedTodoTaskScheduleCount();
		}
	}

	public void updateInCompletionStatus() {
		if (completionStatus) {
			this.completionStatus = false;
			todoTask.minusCompletedTodoTaskScheduleCount();
		}
	}
}
