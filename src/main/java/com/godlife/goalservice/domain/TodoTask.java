package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.converter.StringListConverter;
import com.godlife.goalservice.domain.enums.RepetitionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
    todo
 */

@Getter
@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoTask extends Todo {
    @Comment("Task 시작일")
    private LocalDate startDate;
    @Comment("Task 종료일")
    private LocalDate endDate;

    @Comment("기간 type")
    private RepetitionType repetitionType;

    @Convert(converter = StringListConverter.class)
    @Comment("기간 파라미터")
    private List<String> repetitionParams;

    @Comment("알림")
    private String notification;

    @OneToMany(mappedBy = "todoTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TodoTaskSchedule> todoTaskSchedules = new ArrayList<>();

    @Comment("모든 스케줄 카운트")
    private Integer totalTodoTaskScheduleCount;
    @Comment("완료된 스케줄 카운트")
    private Integer completedTodoTaskScheduleCount;


    private TodoTask(String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate, RepetitionType repetitionType, List<String> repetitionParams) {
        super(title, depth, orderNumber);
        this.startDate = startDate;
        this.endDate = endDate;
        this.repetitionType = repetitionType;
        this.repetitionParams = repetitionParams;
        createSchedules();
        setScheduleCount();
    }

    private void setScheduleCount() {
        totalTodoTaskScheduleCount = todoTaskSchedules.size();
        completedTodoTaskScheduleCount = 0;
    }

    public static TodoTask createTodoTask(String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate, RepetitionType repetitionType, List<String> repetitionParams) {
        return new TodoTask(title, depth, orderNumber, startDate, endDate, repetitionType, repetitionParams);
    }

    private void createSchedules() {
        switch (repetitionType){
            case DAY:
                createDaySchedule();
                break;
            case WEEK:
                createWeekSchedule();
                break;
            case MONTH:
                createMonthSchedule();
                break;
        }
    }

    //TODO 뭔가 리팩토링이 필요해보인다??
    private void createDaySchedule() {
        for (int i = 0; i <= ChronoUnit.DAYS.between(startDate,endDate); i++) {
            TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(startDate.plusDays(i));
            todoTaskSchedule.setTodoTask(this);
            todoTaskSchedules.add(todoTaskSchedule);
        }
    }
    private void createWeekSchedule() {
        for (int i = 0; i <= ChronoUnit.DAYS.between(startDate,endDate); i++) {
            if (repetitionParams.contains(startDate.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))) {
                TodoTaskSchedule todoTaskSchedule = new TodoTaskSchedule(startDate.plusDays(i));
                todoTaskSchedule.setTodoTask(this);
                todoTaskSchedules.add(todoTaskSchedule);
            }
        }
    }
    private void createMonthSchedule() {
        for (int i = 0; i <= ChronoUnit.DAYS.between(startDate,endDate); i++) {
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
