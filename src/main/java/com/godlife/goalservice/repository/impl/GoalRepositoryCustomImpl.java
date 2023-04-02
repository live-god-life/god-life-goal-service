package com.godlife.goalservice.repository.impl;

import static com.godlife.goalservice.domain.QGoal.*;
import static com.godlife.goalservice.domain.QMindset.*;
import static com.godlife.goalservice.domain.QTodo.*;
import static com.godlife.goalservice.domain.QTodoFolder.*;
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
import com.godlife.goalservice.dto.GoalMindsetsTodosDto;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.MindsetDto;
import com.godlife.goalservice.dto.QGoalMindsetDto;
import com.godlife.goalservice.dto.QGoalMindsetsTodosDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto;
import com.godlife.goalservice.dto.QGoalTodoScheduleDto_TodoScheduleDto;
import com.godlife.goalservice.dto.QMindsetDto;
import com.godlife.goalservice.dto.QTodoDto;
import com.godlife.goalservice.dto.QTodoScheduleCountDto;
import com.godlife.goalservice.dto.TodoDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;
import com.godlife.goalservice.exception.NoSuchGoalException;
import com.godlife.goalservice.repository.GoalRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class GoalRepositoryCustomImpl implements GoalRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private static final int FIRST_DEPTH = 1;
	private static final int SECOND_DEPTH = 2;

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
						.count().intValue().as("dDayCount"),
					new CaseBuilder()
						.when(todoTaskSchedule.completionStatus.eq(true))
						.then(1)
						.otherwise((Integer)null)
						.count().intValue().as("completedCount")))
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
			.where(goal.userId.eq(userId))
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
	public GoalMindsetsTodosDto findGoalWithMindsetsAndTodosByUserIdAndGoalId(Long userId, Long goalId) {
		GoalMindsetsTodosDto goalMindsetsTodosDto = findGoalMindsetsTodosDto(goalId);

		if (goalMindsetsTodosDto == null) {
			throw new NoSuchGoalException();
		}

		List<MindsetDto> mindsetDtos = findMindsetDtos(goalId);
		goalMindsetsTodosDto.registerMindsetDtos(mindsetDtos);

		List<TodoDto> parentTodoDtos = findParentTodoDtos(goalId);
		List<TodoDto> childTodoDtos = findChildTodoDtos(goalId);
		setChildTodoDtosInParentTodoDtos(parentTodoDtos, childTodoDtos);
		goalMindsetsTodosDto.registerTodoDtos(parentTodoDtos);

		return goalMindsetsTodosDto;
	}

	private void setChildTodoDtosInParentTodoDtos(List<TodoDto> parentTodoDtos, List<TodoDto> childTodoDtos) {
		parentTodoDtos.stream()
			.filter(todoDto -> todoDto.getType().equals("folder"))
			.forEach(todoDto ->
				todoDto.registerChildTodos(
					childTodoDtos.stream()
						.filter(childTodos -> childTodos.getParentTodoId().equals(todoDto.getTodoId()))
						.collect(Collectors.toList())
				));
	}

	private List<TodoDto> findChildTodoDtos(Long goalId) {
		return queryFactory
			.select(
				new QTodoDto(
					todo.todoId,
					todoTask.parent_todo_id,
					todo.type,
					todo.title,
					todo.depth,
					todo.orderNumber,
					todo.startDate.stringValue(),
					todo.endDate.stringValue(),
					todoTask.repetitionType.stringValue(),
					todoTask.repetitionParams,
					todoTask.notification,
					todoTask.totalTodoTaskScheduleCount,
					todoTask.completedTodoTaskScheduleCount))
			.from(todo)
			.leftJoin(todoTask).on(todoTask.eq(todo))
			.leftJoin(todoFolder).on(todoFolder.eq(todo))
			.where(todo.goal.goalId.eq(goalId),
				todo.depth.eq(SECOND_DEPTH))
			.fetch();
	}

	private List<TodoDto> findParentTodoDtos(Long goalId) {
		return queryFactory
			.select(
				new QTodoDto(
					todo.todoId,
					todo.type,
					todo.title,
					todo.depth,
					todo.orderNumber,
					todo.startDate.stringValue(),
					todo.endDate.stringValue(),
					todoTask.repetitionType.stringValue(),
					todoTask.repetitionParams,
					todoTask.notification,
					todoTask.totalTodoTaskScheduleCount,
					todoTask.completedTodoTaskScheduleCount))
			.from(todo)
			.leftJoin(todoTask).on(todoTask.eq(todo))
			.leftJoin(todoFolder).on(todoFolder.eq(todo))
			.where(todo.goal.goalId.eq(goalId),
				todo.depth.eq(FIRST_DEPTH))
			.fetch();
	}

	private List<MindsetDto> findMindsetDtos(Long goalId) {
		return queryFactory
			.select(
				new QMindsetDto(
					mindset.mindsetId,
					mindset.content))
			.from(mindset)
			.where(mindset.goal.goalId.eq(goalId))
			.fetch();
	}

	private GoalMindsetsTodosDto findGoalMindsetsTodosDto(Long goalId) {
		return queryFactory
			.select(
				new QGoalMindsetsTodosDto(
					goal.goalId,
					goal.category,
					goal.title,
					goal.startDate,
					goal.endDate,
					goal.totalMindsetCount,
					goal.totalTodoCount,
					goal.completedTodoCount,
					goal.totalTodoTaskScheduleCount,
					goal.completedTodoTaskScheduleCount))
			.from(goal)
			.where(goal.goalId.eq(goalId))
			.fetchOne();
	}

	@Override
	public List<GoalTodoScheduleDto> findDailyGoalsAndTodosByUserIdAndLocalDate(Pageable page, Long userId, LocalDate searchedDate, Boolean completionStatus) {
		List<GoalTodoScheduleDto> goals = findGoalTodoScheduleDtos(page, userId, searchedDate);

		List<GoalTodoScheduleDto.TodoScheduleDto> todoSchedules = findTodoScheduleDtos(userId, searchedDate, completionStatus);
		return makeTodoScheduleDtoInGoalTodoScheduleDto(goals, todoSchedules);
	}

	private List<GoalTodoScheduleDto> makeTodoScheduleDtoInGoalTodoScheduleDto(List<GoalTodoScheduleDto> goals, List<GoalTodoScheduleDto.TodoScheduleDto> todoSchedules) {
		return goals.stream().filter(goal -> {
			List<GoalTodoScheduleDto.TodoScheduleDto> todoScheduleDtos = todoSchedules.stream()
				.filter(todoSchedule -> todoSchedule.getGoalId().equals(goal.getGoalId()))
				.collect(Collectors.toList());
			goal.addTodoSchedule(todoScheduleDtos);
			return todoScheduleDtos.size() > 0;
		}).collect(Collectors.toList());

	}

	private List<GoalTodoScheduleDto.TodoScheduleDto> findTodoScheduleDtos(Long userId, LocalDate searchedDate, Boolean completionStatus) {
		return queryFactory
			.select(
				new QGoalTodoScheduleDto_TodoScheduleDto(
					goal.goalId,
					todoTaskSchedule.todoTaskScheduleId,
					todoTask.todoId,
					todoTask.title,
					todoTaskSchedule.completionStatus,
					todoTask.repetitionType,
					todoTask.repetitionParams,
					todoTask.totalTodoTaskScheduleCount,
					todoTask.completedTodoTaskScheduleCount,
					todoTask.endDate))
			.from(todoTask)
			.leftJoin(todoTask.goal, goal)
			.leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
			.where(goal.userId.eq(userId),
				todoTaskSchedule.scheduleDate.eq(searchedDate),
				eqTodoTaskScheduleCompletionStatus(completionStatus))
			.fetch();
	}

	private List<GoalTodoScheduleDto> findGoalTodoScheduleDtos(Pageable page, Long userId, LocalDate searchedDate) {
		return queryFactory
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
	}

	private BooleanExpression eqTodoTaskScheduleCompletionStatus(Boolean completionStatus) {
		if (Objects.isNull(completionStatus)) {
			return null;
		}
		return todoTaskSchedule.completionStatus.eq(completionStatus);
	}
}
