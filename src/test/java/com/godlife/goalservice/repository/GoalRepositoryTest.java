package com.godlife.goalservice.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;
import com.godlife.goalservice.dto.GoalTodoScheduleDto;
import com.godlife.goalservice.dto.TodoScheduleCountDto;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GoalRepositoryTest {
	@Autowired
	GoalRepository goalRepository;
	@Autowired
	EntityManager em;

	@Test
	void save() {
		String expectedTitle = "목표제목이다";
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			expectedTitle
		);

		Mindset mindset1 = createMindsetByContent("마인드셋1", goal);
		Mindset mindset2 = createMindsetByContent("마인드셋2", goal);

		TodoTask todoTask = TodoTask.createTodoTask(
			"todo",
			1,
			0,
			LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
			LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
			RepetitionType.MONTH,
			List.of("1", "13", "21"),
			"0900",
			goal);

		//when
		Goal savedGoal = goalRepository.save(goal);

		//then
		assertThat(savedGoal.getTitle()).isEqualTo(expectedTitle);
	}

	@Test
	void findById() {
		//given
		String expectedTitle = "목표제목이다";

		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			expectedTitle
		);

		Mindset mindset1 = createMindsetByContent("마인드셋1", goal);
		Mindset mindset2 = createMindsetByContent("마인드셋2", goal);

		TodoTask todoTask = TodoTask.createTodoTask(
			"todo",
			1,
			0,
			LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
			LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
			RepetitionType.MONTH,
			List.of("1", "13", "21"),
			"0900",
			goal);

		//when
		Goal savedGoal = goalRepository.save(goal);

		em.flush();
		em.clear();

		Goal foundGoal = goalRepository.findById(savedGoal.getGoalId()).orElseThrow(NoSuchElementException::new);

		//then
		assertThat(foundGoal.getTitle()).isEqualTo(expectedTitle);
		// assertThat(foundGoal.getMindsets().size()).isEqualTo(2);
	}

	private static Mindset createMindsetByContent(String content, Goal goal) {
		return Mindset.createMindset(content, goal);
	}

	// @Test
	@DisplayName("MyList/캘린더 일별 투두 카운팅 조회")
	void findDailyTodosCount() {
		//given
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			"이직하기"
		);

		Mindset mindset1 = createMindsetByContent("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.", goal);

		TodoTask todoTask = TodoTask.createTodoTask("todo",
			1,
			0,
			LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
			LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
			RepetitionType.DAY,
			Collections.emptyList(),
			"0900",
			goal);

		goalRepository.save(goal);

		//when
		List<TodoScheduleCountDto> result = goalRepository.findDailyTodosCount(1L, YearMonth.of(2022, 10));

		//then
		assertThat(result.size()).isEqualTo(31);
	}

	// @Test
	@DisplayName("특정일의 최하위 투두리스트 조회하기")
	void getDailyGoalsAndTodos() {
		//given
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			"이직하기"
		);

		Mindset mindset1 = createMindsetByContent("사는건 레벨업이 아닌 스펙트럼을 넓히는 거란 얘길 들었다. 어떤 말보다 용기가 된다.", goal);

		TodoTask todoTask = TodoTask.createTodoTask("todo",
			1,
			0,
			LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd")),
			LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd")),
			RepetitionType.DAY,
			Collections.emptyList(),
			"0900",
			goal);

		goalRepository.save(goal);

		//when
		List<GoalTodoScheduleDto> result = goalRepository.findDailyGoalsAndTodosByUserIdAndLocalDate(
			Pageable.ofSize(25),
			1L,
			LocalDate.of(2022, 10, 1),
			false);

		//then
		assertThat(result.size()).isEqualTo(1);
	}
}
