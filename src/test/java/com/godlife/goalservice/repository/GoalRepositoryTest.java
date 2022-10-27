package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;
import com.godlife.goalservice.service.dto.TodoScheduleCountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GoalRepositoryTest {
    @Autowired
    GoalRepository goalRepository;
    @Autowired
    EntityManager em;

//    @Test
    void save() {
        Goal goal;
        String expectedTitle;
        expectedTitle = "목표제목이다";

        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        TodoTask todoTask = TodoTask.createTodoTask(
                "todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.MONTH,
                List.of("1", "13", "21"));

        goal = Goal.createGoal(
                1L,
                Category.CAREER,
                expectedTitle,
                1,
                List.of(mindset1, mindset2),
                Collections.emptyList()
        );
        //when
        Goal savedGoal = goalRepository.save(goal);

        //then
        assertThat(savedGoal.getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    void findById() {
        //given
        String expectedTitle = "목표제목이다";

        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        TodoTask todoTask = TodoTask.createTodoTask(
                "todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.MONTH,
                List.of("1", "13", "21"));

        Goal goal = Goal.createGoal(
                1L,
                Category.CAREER,
                expectedTitle,
                1,
                List.of(mindset1, mindset2),
                List.of(todoTask)
        );

        //when
        Goal savedGoal = goalRepository.save(goal);

        em.flush();
        em.clear();

        Goal foundGoal = goalRepository.findById(savedGoal.getGoalId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(foundGoal.getTitle()).isEqualTo(expectedTitle);
        assertThat(foundGoal.getMindsets().size()).isEqualTo(2);
    }

    private static Mindset createMindsetByContent(String content) {
        return Mindset.createMindset(content);
    }
    
    @Test
    @DisplayName("MyList/캘린더 일별 투두 카운팅 조회")
    void findDailyTodosCount() {
        //given
        Mindset mindset1 = createMindsetByContent("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.");

        TodoTask todoTask = TodoTask.createTodoTask("todo",
                1,
                0,
                LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
                RepetitionType.DAY,
                Collections.emptyList()
        );

        Goal goal = Goal.createGoal(
                1L,
                Category.CAREER,
                "이직하기",
                1,
                List.of(mindset1),
                List.of(todoTask)
        );

        Goal savedGoal = goalRepository.save(goal);

        //when
        List<TodoScheduleCountDto> result = goalRepository.findDailyTodosCount(1L, YearMonth.of(2022, 10));

        //then
        assertThat(result.size()).isEqualTo(31);
    }

//    @Test
    @DisplayName("특정일의 최하위 투두리스트 조회하기")
    void findDailyGoalsAndLowestDepthTodosByUserId() {


        Goal savedGoal = goalRepository.save(null);
        //when
        List<Goal> foundGoals = goalRepository.findDailyGoalsAndLowestDepthTodosByUserId(savedGoal.getUserId());
        //then
        assertThat(foundGoals.size()).isEqualTo(1);
    }
}