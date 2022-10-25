package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.converter.StringListConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

/*
    todo

 */


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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "todo_id")
    private List<TodoTaskSchedule> todoTaskSchedules = new ArrayList<>();

    @Builder
    public TodoTask(Long todoId, String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate, RepetitionType repetitionType, List<String> repetitionParams) {
        super(todoId, title, depth, orderNumber);
        this.startDate = startDate;
        this.endDate = endDate;
        this.repetitionType = repetitionType;
        this.repetitionParams = repetitionParams;
        createSchedules();
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
        for (int i = 0; i <= Period.between(startDate,endDate).getDays(); i++) {
            todoTaskSchedules.add(new TodoTaskSchedule(startDate.plusDays(i)));
        }
    }
    private void createWeekSchedule() {
        for (int i = 0; i <= Period.between(startDate,endDate).getDays(); i++) {
            if (repetitionParams.contains(startDate.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))) {
                todoTaskSchedules.add(new TodoTaskSchedule(startDate.plusDays(i)));
            }
        }
    }
    private void createMonthSchedule() {
        for (int i = 0; i <= Period.between(startDate,endDate).getDays(); i++) {
            if (repetitionParams.contains(String.valueOf(startDate.plusDays(i).getDayOfMonth()))) {
                todoTaskSchedules.add(new TodoTaskSchedule(startDate.plusDays(i)));
            }
        }
    }
}
