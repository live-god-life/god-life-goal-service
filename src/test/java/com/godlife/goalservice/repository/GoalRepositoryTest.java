package com.godlife.goalservice.repository;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;
import com.godlife.goalservice.service.dto.GoalTodoScheduleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
@DataJpaTest
class GoalRepositoryTest {
    @Autowired
    GoalRepository goalRepository;



    @BeforeEach
    void beforeEach() {

    }

//    @Test
    void save() {
        Goal goal;
        String expectedTitle;
        expectedTitle = "목표제목이다";

        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        Todo todo1 = TodoTask.builder()
                .title("투두태스크1")
                .build();

        goal = Goal.builder()
                .title(expectedTitle)
                .userId(1L)
                .mindsets(List.of(mindset1, mindset2))
                .build();
        //when
        Goal savedGoal = goalRepository.save(goal);

        //then
        assertThat(savedGoal.getTitle()).isEqualTo(expectedTitle);
    }

//    @Test
    void findById() {
        Goal goal;
        String expectedTitle;
        expectedTitle = "목표제목이다";

        Mindset mindset1 = createMindsetByContent("마인드셋1");
        Mindset mindset2 = createMindsetByContent("마인드셋2");

        Todo todo1 = TodoTask.builder()
                .title("투두태스크1")
                .build();

        goal = Goal.builder()
                .title(expectedTitle)
                .userId(1L)
                .mindsets(List.of(mindset1, mindset2))
                .build();
        //when
        Goal savedGoal = goalRepository.save(goal);
        Goal foundGoal = goalRepository.findById(savedGoal.getGoalId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(foundGoal.getTitle()).isEqualTo(expectedTitle);
        assertThat(foundGoal.getMindsets().size()).isEqualTo(2);
    }

    private static Mindset createMindsetByContent(String content) {
        return Mindset.builder()
                .content(content)
                .build();
    }
    
    @Test
    @DisplayName("MyList/캘린더 일별 투두 카운팅 조회")
    void findDailyTodosCount() {
        //given
        Mindset mindset1 = createMindsetByContent("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.");

        Todo todo1 = TodoTask.builder()
                .title("포트폴리오 완성")
                .startDate(LocalDate.parse("20220923", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .endDate(LocalDate.parse("20221123", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .repetitionType(RepetitionType.DAY)
                .orderNumber(1)
                .depth(1)
                .build();

        Goal goal = Goal.builder()
                .title("이직하기")
                .category(Category.CAREER)
                .userId(1L)
                .mindsets(List.of(mindset1))
                .todos(List.of(todo1))
                .build();

        Goal savedGoal = goalRepository.save(goal);

        //when
        List<GoalTodoScheduleDto> result = goalRepository.findDailyTodosCount(1L, YearMonth.of(2022, 10));

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