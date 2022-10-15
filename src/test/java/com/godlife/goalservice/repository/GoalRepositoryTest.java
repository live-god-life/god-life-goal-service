package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GoalRepositoryTest {
    @Autowired GoalRepository goalRepository;

    @Test
    void save() {
        //given
        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        String expectedTitle = "목표제목이다";
        Goal goal = Goal.builder()
                .title(expectedTitle)
                .mindsets(List.of(mindset1, mindset2))
                .build();

        //when
        Goal savedGoal = goalRepository.save(goal);

        //then
        assertThat(savedGoal.getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    void findById() {
        //given
        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        String expectedTitle = "목표제목이다";
        Goal goal = Goal.builder()
                .title(expectedTitle)
                .mindsets(List.of(mindset1, mindset2))
                .build();

        //when
        Goal savedGoal = goalRepository.save(goal);

        Goal foundGoal = goalRepository.findById(savedGoal.getGoalId()).orElseThrow(() -> new NoSuchElementException());
        //then
        assertThat(foundGoal.getTitle()).isEqualTo(expectedTitle);
        assertThat(foundGoal.getMindsets().get(0).getContent()).isEqualTo("마인드셋1");
    }

    private static Mindset createMindsetByContent(String 마인드셋1) {
        return Mindset.builder()
                .content(마인드셋1)
                .build();
    }
}