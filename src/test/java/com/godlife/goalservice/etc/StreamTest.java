package com.godlife.goalservice.etc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class StreamTest {
	@Test
	void 스케줄이없는목표는_제외() {
		TestGoal testGoal1 = new TestGoal(1L);
		TestGoal testGoal2 = new TestGoal(2L);

		TestSchedule testSchedule1 = new TestSchedule(1L,1L);
		TestSchedule testSchedule2 = new TestSchedule(2L,1L);

		List<TestGoal> goals = new ArrayList<>();
		goals.add(testGoal1);
		goals.add(testGoal2);

		List<TestSchedule> schedules = new ArrayList<>();
		schedules.add(testSchedule1);
		schedules.add(testSchedule2);

		List<TestGoal> result = goals.stream().filter(
			testGoal -> {
				List<TestSchedule> collect = schedules.stream().filter(testSchedule -> testSchedule.goalId.equals(testGoal.id)).collect(Collectors.toList());
				testGoal.addSchedules(collect);
				return collect.size() > 0;
			}
		).collect(Collectors.toList());

		Assertions.assertThat(result.size()).isEqualTo(1);
	}

	static class TestGoal {
		private Long id;
		private List<TestSchedule> schedules = new ArrayList<>();

		public TestGoal(Long id) {
			this.id = id;
		}

		public void addSchedules(List<TestSchedule> schedules) {
			this.schedules.addAll(schedules);
		}
	}

	static class TestSchedule {
		private Long id;

		private Long goalId;

		public TestSchedule(Long id, Long goalId) {
			this.id = id;
			this.goalId = goalId;
		}
	}
}
