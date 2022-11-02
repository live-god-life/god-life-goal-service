package com.godlife.goalservice.repository.impl;

import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto_TodoScheduleDto;
import com.godlife.goalservice.dto.QTodoScheduleCountDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.repository.GoalRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;

import static com.godlife.goalservice.domain.QGoal.goal;
import static com.godlife.goalservice.domain.QTodoTask.todoTask;
import static com.godlife.goalservice.domain.QTodoTaskSchedule.todoTaskSchedule;

public class GoalRepositoryCustomImpl implements GoalRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public GoalRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<TodoScheduleCountDto> findDailyTodosCount(Long userId, YearMonth yearMonth) {
		return queryFactory
			.select(
				new QTodoScheduleCountDto(todoTaskSchedule.scheduleDate,
					todoTaskSchedule.todoTaskScheduleId.count().intValue().as("todoCount"))
			)
			.from(todoTask)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(todoTask.goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.yearMonth().eq(yearMonth.getYear() * 100 + yearMonth.getMonthValue()))
			.groupBy(todoTaskSchedule.scheduleDate)
			.fetch();
	}

	//TODO 코드가 깔끔하지못해 리팩토링 예정
	@Override
	public List<GoalTodoScheduleDto> findDailyGoalsAndTodosByUserIdAndLocalDate(Long userId, LocalDate localDate) {
		List<GoalTodoScheduleDto> goals = queryFactory
			.select(
				new QGoalTodoScheduleDto(
					goal.goalId,
					goal.title
				)
			)
			.from(todoTask)
			.rightJoin(todoTask.goal, goal)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.eq(localDate))
			.groupBy(goal.goalId, goal.title)
			.fetch();

		List<GoalTodoScheduleDto.TodoScheduleDto> todoSchedules = queryFactory
			.select(
				new QGoalTodoScheduleDto_TodoScheduleDto(
					goal.goalId,
					todoTaskSchedule.todoTaskScheduleId,
					todoTask.title,
					todoTaskSchedule.completionStatus
				)
			)
			.from(todoTask)
			.leftJoin(todoTask.goal, goal)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.eq(localDate))
			.fetch();

		todoSchedules.forEach(todoScheduleDto -> {
			GoalTodoScheduleDto goalTodoScheduleDto1 = goals.stream()
				.filter(goalTodoScheduleDto -> goalTodoScheduleDto.getGoalId().equals(todoScheduleDto.getGoalId()))
				.findAny()
				.orElseThrow(NoSuchElementException::new);
			goalTodoScheduleDto1.addTodoSchedule(todoScheduleDto);
		});

		return goals;
	}
}
