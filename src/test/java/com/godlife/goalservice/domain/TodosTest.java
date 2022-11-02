package com.godlife.goalservice.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.godlife.goalservice.domain.enums.RepetitionType;

class TodosTest {

	@Test
	@DisplayName("투두 중 시작날짜중 제일 빠른날짜, 종료날짜중 제일 느린날짜를 가져온다.")
	void getFistStartDateAndEndDate() {
		//given
		Todo todo1 = TodoTask.createTodoTask(
			"제목",
			1,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 3),
			RepetitionType.DAY,
			List.of(),
			null
		);
		Todo todo2 = TodoTask.createTodoTask(
			"제목",
			1,
			1,
			LocalDate.of(2022, 10, 7),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			List.of(),
			null
		);

		Todos todos = new Todos(List.of(todo1, todo2));
		//when
		LocalDate fistStartDate = todos.getFistStartDate();
		LocalDate lastEndDate = todos.getLastEndDate();

		//then
		assertThat(fistStartDate).isEqualTo(LocalDate.of(2022, 10, 1));
		assertThat(lastEndDate).isEqualTo(LocalDate.of(2022, 10, 31));
	}

	@Test
	@DisplayName("전체 투두태스크 카운팅을 가져온다.")
	void getTotalTodoTaskCount() {
		//given
		Todo todo1 = TodoTask.createTodoTask(
			"제목",
			1,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 3),
			RepetitionType.DAY,
			List.of(),
			null
		);
		Todo todo2 = TodoTask.createTodoTask(
			"제목",
			1,
			2,
			LocalDate.of(2022, 10, 7),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			List.of(),
			null
		);
		Todo todo3_1 = TodoTask.createTodoTask(
			"제목",
			2,
			1,
			LocalDate.of(2022, 10, 7),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			List.of(),
			null
		);
		Todo todo3_2 = TodoTask.createTodoTask(
			"제목",
			2,
			2,
			LocalDate.of(2022, 10, 7),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			List.of(),
			null
		);
		Todo todo3 = TodoFolder.createTodoFolder("투두폴더", 1, 3, List.of(todo3_1, todo3_2), null);
		//when
		Todos todos = new Todos(List.of(todo1, todo2, todo3));
		int result = todos.getTotalTodoTaskCount();
		//then
		assertThat(result).isEqualTo(4);
	}

	@Test
	void getTotalTodoTaskScheduleCount() {
	    //given
		TodoTask todo1 = TodoTask.createTodoTask(
			"제목",
			1,
			1,
			LocalDate.of(2022, 10, 1),
			LocalDate.of(2022, 10, 3),
			RepetitionType.DAY,
			null,
			null
		);
		TodoTask todo2 = TodoTask.createTodoTask(
			"제목",
			1,
			1,
			LocalDate.of(2022, 10, 7),
			LocalDate.of(2022, 10, 31),
			RepetitionType.DAY,
			null,
			null
		);
	    //when
		Todos todos = new Todos(List.of(todo1, todo2));
		int result = todos.getTotalTodoTaskScheduleCount();

		//then
		assertThat(result).isEqualTo(todo1.getTotalTodoTaskScheduleCount() + todo2.getTotalTodoTaskScheduleCount()); //todo1 3일치, todo2 25일치
	}
}
