package com.godlife.goalservice.domain;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Comment;

import com.godlife.goalservice.domain.converter.StringListConverter;
import com.godlife.goalservice.domain.enums.RepetitionType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    todo
 */

@Getter
@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTask extends Todo {
	private Long parent_todo_id;

	@Enumerated(EnumType.STRING)
	@Comment("기간 type")
	private RepetitionType repetitionType;

	@Convert(converter = StringListConverter.class)
	@Comment("기간 파라미터")
	private List<String> repetitionParams;

	@Comment("알림")
	private String notification;

	@Comment("모든 스케줄 카운트")
	private Integer totalTodoTaskScheduleCount;
	@Comment("완료된 스케줄 카운트")
	private Integer completedTodoTaskScheduleCount;

	@OneToMany(mappedBy = "todoTask", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<TodoTaskSchedule> todoTaskSchedules = new ArrayList<>();

	private TodoTask(String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate,
		RepetitionType repetitionType, List<String> repetitionParams, String notification, Goal goal) {

		super(title, depth, orderNumber, startDate, endDate, goal);
		this.repetitionType = repetitionType;
		this.repetitionParams = repetitionParams;
		this.notification = notification;
		createSchedules(startDate, endDate);
		setScheduleCount();
	}

	private void setScheduleCount() {
		totalTodoTaskScheduleCount = todoTaskSchedules.size();
		completedTodoTaskScheduleCount = 0;
	}

	public static TodoTask createTodoTask(String title, Integer depth, Integer orderNumber, LocalDate startDate,
		LocalDate endDate, RepetitionType repetitionType, List<String> repetitionParams, String notification, Goal goal) {

		return new TodoTask(title, depth, orderNumber, startDate, endDate, repetitionType, repetitionParams, notification, goal);
	}

	private void createSchedules(LocalDate startDate, LocalDate endDate) {
		switch (repetitionType) {
			case DAY:
				createDaySchedule(startDate, endDate);
				break;
			case WEEK:
				createWeekSchedule(startDate, endDate);
				break;
			case MONTH:
				createMonthSchedule(startDate, endDate);
				break;
			case NONE:
				createNoneSchedule(endDate);
				break;
		}
	}

	private void createNoneSchedule(LocalDate endDate) {
		TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(endDate);
		todoTaskSchedule.setTodoTask(this);
		todoTaskSchedules.add(todoTaskSchedule);
	}

	//TODO 뭔가 리팩토링이 필요해보인다??
	private void createDaySchedule(LocalDate startDate, LocalDate endDate) {
		for (int i = 0; i <= ChronoUnit.DAYS.between(startDate, endDate); i++) {
			TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(startDate.plusDays(i));
			todoTaskSchedule.setTodoTask(this);
			todoTaskSchedules.add(todoTaskSchedule);
		}
	}

	private void createWeekSchedule(LocalDate startDate, LocalDate endDate) {
		for (int i = 0; i <= ChronoUnit.DAYS.between(startDate, endDate); i++) {
			if (repetitionParams.contains(
				startDate.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))) {
				TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(startDate.plusDays(i));
				todoTaskSchedule.setTodoTask(this);
				todoTaskSchedules.add(todoTaskSchedule);
			}
		}
	}

	private void createMonthSchedule(LocalDate startDate, LocalDate endDate) {
		for (int i = 0; i <= ChronoUnit.DAYS.between(startDate, endDate); i++) {
			if (repetitionParams.contains(String.valueOf(startDate.plusDays(i).getDayOfMonth()))) {
				TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(startDate.plusDays(i));
				todoTaskSchedule.setTodoTask(this);
				todoTaskSchedules.add(todoTaskSchedule);
			}
		}
	}

	public void plusCompletedTodoTaskScheduleCount() {
		completedTodoTaskScheduleCount++;
	}

	public void minusCompletedTodoTaskScheduleCount() {
		completedTodoTaskScheduleCount--;
	}
}
