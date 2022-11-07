package com.godlife.goalservice.repository.impl;

import static com.godlife.goalservice.domain.QTodoTask.*;
import static com.godlife.goalservice.domain.QTodoTaskSchedule.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.godlife.goalservice.dto.QTodoSchedulesDto;
import com.godlife.goalservice.dto.TodoSchedulesDto;
import com.godlife.goalservice.repository.TodoTaskScheduleRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TodoTaskScheduleRepositoryCustomImpl implements TodoTaskScheduleRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private static final String AFTER = "after";

	public TodoTaskScheduleRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<TodoSchedulesDto> findAllByTodoId(Pageable page, Long todoId, String criteria) {
		return queryFactory
			.select(
				new QTodoSchedulesDto(
					todoTaskSchedule.todoTaskScheduleId,
					todoTask.title,
					todoTaskSchedule.scheduleDate,
					todoTaskSchedule.completionStatus
				)
			)
			.from(todoTaskSchedule)
			.leftJoin(todoTaskSchedule.todoTask, todoTask)
			.where(todoTask.todoId.eq(todoId),
				compareScheduleDate(criteria))
			.fetch();
	}

	private BooleanExpression compareScheduleDate(String criteria) {
		LocalDate today = LocalDate.now();
		if (criteria.equals(AFTER)) {
			return todoTaskSchedule.scheduleDate.after(today);
		} else {
			return todoTaskSchedule.scheduleDate.before(today);
		}
	}
}
