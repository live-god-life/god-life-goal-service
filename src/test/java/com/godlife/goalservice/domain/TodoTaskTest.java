package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.enums.RepetitionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTaskTest {
    @Test
    @DisplayName("투두태스크 생성시 스케줄 자동생성 테스트(반복이 매일일때)")
    void createTodoTaskWithSchedules_day() {
        //given
        TodoTask todoTask = TodoTask.createTodoTask("todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.DAY,
                null
        );
        //when
        List<TodoTaskSchedule> result = todoTask.getTodoTaskSchedules();

        //then
        assertThat(result.size()).isEqualTo(31);
    }

    @Test
    @DisplayName("투두태스크 생성시 스케줄 자동생성 테스트(반복이 매주일때)")
    void createTodoTaskWithSchedules_week() {
        //given
        TodoTask todoTask = TodoTask.createTodoTask("todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.WEEK,
                List.of("월", "수", "금"));
        //when
        List<TodoTaskSchedule> result = todoTask.getTodoTaskSchedules();

        //then
        assertThat(result.size()).isEqualTo(13);    //월5 + 수4 + 금4 = 13
    }

    @Test
    @DisplayName("투두태스크 생성시 스케줄 자동생성 테스트(반복이 매달일때)")
    void createTodoTaskWithSchedules_month() {
        //given
        TodoTask todoTask = TodoTask.createTodoTask(
                "todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.MONTH,
                List.of("1", "13", "21"));
        //when
        List<TodoTaskSchedule> result = todoTask.getTodoTaskSchedules();

        //then
        assertThat(result.size()).isEqualTo(3);
    }
}