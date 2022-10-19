package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GoalRepositoryTest {
    @Autowired
    GoalRepository goalRepository;

    private Goal goal;
    private String expectedTitle;

    @BeforeEach
    void beforeEach() {
        //given
        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        expectedTitle = "목표제목이다";
        goal = Goal.builder()
                .title(expectedTitle)
                .mindsets(List.of(mindset1, mindset2))
                .build();
    }

    @Test
    void save() {
        //when
        Goal savedGoal = goalRepository.save(goal);

        //then
        assertThat(savedGoal.getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    void findById() {
        //when
        Goal savedGoal = goalRepository.save(goal);
        Goal foundGoal = goalRepository.findById(savedGoal.getGoalId()).orElseThrow(() -> new NoSuchElementException());

        //then
        assertThat(foundGoal.getTitle()).isEqualTo(expectedTitle);
        assertThat(foundGoal.getMindsets().size()).isEqualTo(2);
    }

    private static Mindset createMindsetByContent(String content) {
        return Mindset.builder()
                .content(content)
                .build();
    }
}