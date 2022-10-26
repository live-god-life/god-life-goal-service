package com.godlife.goalservice.repository.impl;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.repository.GoalRepositoryCustom;
import com.godlife.goalservice.service.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.service.dto.QGoalTodoScheduleDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.YearMonth;
import java.util.List;

import static com.godlife.goalservice.domain.QGoal.goal;
import static com.godlife.goalservice.domain.QTodoTask.todoTask;
import static com.godlife.goalservice.domain.QTodoTaskSchedule.todoTaskSchedule;

public class GoalRepositoryCustomImpl implements GoalRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public GoalRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<GoalTodoScheduleDto> findDailyTodosCount(Long userId, YearMonth yearMonth) {
        return queryFactory
                .select(
                        new QGoalTodoScheduleDto(todoTaskSchedule.scheduleDate,
                                todoTaskSchedule.id.count().intValue().as("todoCount"))
                )
                .from(goal)
                .leftJoin(goal.todos, todoTask._super)
                .leftJoin(todoTask.todoTaskSchedules, todoTaskSchedule)
                .where(goal.userId.eq(userId),
                        todoTaskSchedule.scheduleDate.yearMonth().eq(yearMonth.getYear() * 100 + yearMonth.getMonthValue()))
                .groupBy(todoTaskSchedule.scheduleDate)
                .fetch();
    }

    @Override
    public List<Goal> findDailyGoalsAndLowestDepthTodosByUserId(Long userId) {
        return queryFactory
                .select(goal)
                .from(goal)
                .where(goal.userId.eq(userId))
                .fetch();
    }
}
