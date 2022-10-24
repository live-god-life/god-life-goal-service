package com.godlife.goalservice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GoalTest {
    @Test
    @DisplayName("마인드셋의 카운트를 조회한다")
    void getMindsetCount() {
        //given
        Goal goal = Goal.builder()
                .mindsets(
                        List.of(
                                Mindset.builder()
                                        .content("마인드셋 샘플1")
                                        .build(),
                                Mindset.builder()
                                        .content("마인드셋 샘플2")
                                        .build()))
                .build();
        //when
        int result = goal.getMindsetTotalCount();

        //then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("진행중 투두의 카운트를 조회한다")
    void getOnProgressCount() {
        //given

        //when

        //then
    }
    
    @Test
    @DisplayName("완료된 투두의 카운트를 조회한다")
    void getCompletedCount() {
        //given
        
        //when
        
        //then
    }
}