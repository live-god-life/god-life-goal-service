package com.godlife.goalservice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {
    @Test
    @DisplayName("")
    void createGoalWithMindsetsAndTodos() {
        //given
        Mindset mindset = Mindset.builder()
                .mindsetId(1L)
                .content("한강뷰 가즈아")
                .build();

        Todo todoFolder_1 = TodoFolder.builder()
                .todoId(1L)
                .title("절약")
                .depth(1)
                .orderNumber(1)
                .build();

        Todo todoTask_1_1 = TodoTask.builder()
                .todoId(2L)
                .title("커피 회사꺼만 마시기")
                .startDate(LocalDateTime.of(2022,10,1,0,0))
                .endDate(LocalDateTime.of(2022,10,30,0,0))
                .depth(2)
                .orderNumber(1)
                .build();


        //when
        
        //then
    }

}