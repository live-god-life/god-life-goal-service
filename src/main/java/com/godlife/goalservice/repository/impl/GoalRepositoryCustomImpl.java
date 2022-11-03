package com.godlife.goalservice.repository.impl;

import static com.godlife.goalservice.domain.QGoal.*;
import static com.godlife.goalservice.domain.QMindset.*;
import static com.godlife.goalservice.domain.QTodoTask.*;
import static com.godlife.goalservice.domain.QTodoTaskSchedule.*;
import static com.querydsl.core.group.GroupBy.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.godlife.goalservice.domain.enums.RepetitionType;
import com.godlife.goalservice.dto.GoalMindsetDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.QGoalMindsetDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto_TodoScheduleDto;
import com.godlife.goalservice.dto.QMindsetDto;
import com.godlife.goalservice.dto.QTodoScheduleCountDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.repository.GoalRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
					new CaseBuilder()
						.when(todoTask.repetitionType.ne(RepetitionType.NONE))
						.then(1)
						.otherwise((Integer)null)
						.count().intValue().as("todoCount"),
					new CaseBuilder()
						.when(todoTask.repetitionType.eq(RepetitionType.NONE))
						.then(1)
						.otherwise((Integer)null)
						.count().intValue().as("dDayCount")))
			.from(todoTask)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(todoTask.goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.yearMonth().eq(yearMonth.getYear() * 100 + yearMonth.getMonthValue()))
			.groupBy(todoTaskSchedule.scheduleDate)
			.fetch();
	}

	@Override
	public List<GoalMindsetDto> findGoalsAndMindsetsByUserIdAndCompletionStatus(Pageable page, Long userId, Boolean completionStatus) {
		Map<Long, GoalMindsetDto> resultMap = queryFactory
			.from(goal)
			.join(mindset).on(goal.eq(mindset.goal))
			.where(eqGoalCompletionStatus(completionStatus))
			.offset(page.getOffset())
			.limit(page.getPageSize())
			.transform(
				groupBy(goal.goalId).as(
					new QGoalMindsetDto(
						goal.goalId,
						goal.title,
						list(new QMindsetDto(mindset.mindsetId, mindset.content)))));

		return resultMap.keySet().stream()
			.map(resultMap::get)
			.collect(Collectors.toList());
	}

	private BooleanExpression eqGoalCompletionStatus(Boolean completionStatus) {
		if (Objects.isNull(completionStatus)) {
			return null;
		}
		return goal.completionStatus.eq(completionStatus);
	}

	@Override
	public List<GoalTodoScheduleDto> findDailyGoalsAndTodosByUserIdAndLocalDate(Pageable page, Long userId, LocalDate searchedDate, Boolean completionStatus) {
		List<GoalTodoScheduleDto> goals = queryFactory
			.select(
				new QGoalTodoScheduleDto(
					goal.goalId,
					goal.title))
			.from(todoTask)
			.rightJoin(todoTask.goal, goal)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.eq(searchedDate))
			.groupBy(goal.goalId, goal.title)
			.offset(page.getOffset())
			.limit(page.getPageSize())
			.fetch();

		List<GoalTodoScheduleDto.TodoScheduleDto> todoSchedules = queryFactory
			.select(
				new QGoalTodoScheduleDto_TodoScheduleDto(
					goal.goalId,
					todoTaskSchedule.todoTaskScheduleId,
					todoTask.todoId,
					todoTask.title,
					todoTaskSchedule.completionStatus,
					todoTask.repetitionType))
			.from(todoTask)
			.leftJoin(todoTask.goal, goal)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.eq(searchedDate),
				eqTodoTaskScheduleCompletionStatus(completionStatus))
			.fetch();

		goals.forEach(goal -> goal.addTodoSchedule(todoSchedules.stream()
			.filter(todoSchedule -> todoSchedule.getGoalId().equals(goal.getGoalId()))
			.collect(Collectors.toList())));
		return goals;
	}

	private BooleanExpression eqTodoTaskScheduleCompletionStatus(Boolean completionStatus) {
		if (Objects.isNull(completionStatus)) {
			return null;
		}
		return todoTaskSchedule.completionStatus.eq(completionStatus);
	}
}
