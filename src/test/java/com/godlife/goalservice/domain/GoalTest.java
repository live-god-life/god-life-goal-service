package com.godlife.goalservice.domain;

import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GoalTest {
	@Test
	@DisplayName("마인드셋의 카운트를 조회한다")
	void getMindsetCount() {
		//given
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			"이직하기"
		);

		//when
		// int result = goal.getMindsetTotalCount();

		//then
		// assertThat(result).isEqualTo(2);
	}

	@Test
	@DisplayName("진행중 투두의 카운트를 조회한다")
	void getTotalTodoCount() {
		//given
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			"이직하기"
		);
		Todo todo1 = TodoTask.createTodoTask(
			"title",
			1,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);
		Todo todo2 = TodoTask.createTodoTask(
			"title",
			1,
			2,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);
		Todo todo3_1 = TodoTask.createTodoTask(
			"title",
			2,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);

		Todo todo3 = TodoFolder.createTodoFolder(
			"title",
			1,
			3,
			List.of(todo3_1),
			goal);
		//when
		int result = goal.getTotalTodoCount();

		//then
		assertThat(result).isEqualTo(3);
	}

	@Test
	@DisplayName("진행중 투두의 카운트를 조회한다")
	void getOnProgressTodoCount() {
		//given
		Goal goal = Goal.createGoal(
			1L,
			Category.CAREER,
			"이직하기"
		);
		Todo todo1 = TodoTask.createTodoTask(
			"title",
			1,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);
		Todo todo2 = TodoTask.createTodoTask(
			"title",
			1,
			2,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);
		Todo todo3_1 = TodoTask.createTodoTask(
			"title",
			2,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			goal);

		Todo todo3 = TodoFolder.createTodoFolder(
			"title",
			1,
			3,
			List.of(todo3_1),
			goal);


		//when
		int result = goal.getTotalTodoCount();

		//then
		assertThat(result).isEqualTo(3);
	}

	@Test
	@DisplayName("완료된 투두의 카운트를 조회한다")
	void getCompletedTodoCount() {
		//given

		//when

		//then
	}
}
