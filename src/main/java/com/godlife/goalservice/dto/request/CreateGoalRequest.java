package com.godlife.goalservice.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.godlife.goalservice.domain.Goal;
import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.domain.Todo;
import com.godlife.goalservice.domain.TodoFolder;
import com.godlife.goalservice.domain.TodoTask;
import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.domain.enums.RepetitionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGoalRequest {
	private String title;

	private String categoryName;
	private String categoryCode;

	private List<CreateGoalMindsetRequest> mindsets;
	private List<CreateGoalTodoRequest> todos;

	public Goal createGoalEntity(Long userId) {
		return Goal.createGoal(userId, Category.valueOf(categoryCode), title);
	}

	public List<Mindset> createMindsetsEntity(Goal goal) {
		return mindsets.stream()
			.map(createGoalMindsetRequest -> Mindset.createMindset(createGoalMindsetRequest.getContent(), goal))
			.collect(Collectors.toList());
	}

	public List<Todo> createTodosEntity(Goal goal) {
		return todos.stream()
			.map(todoDto -> createTodo(todoDto, goal))
			.collect(Collectors.toList());
	}

	//TODO 자식 인스턴스를 만들때 뭔가 깔끔하지 못한데,,,, 다른 방법이 없나?
	private Todo createTodo(CreateGoalTodoRequest todoDto, Goal goal) {
		if ("folder".equals(todoDto.getType())) {
			return TodoFolder.createTodoFolder(
				todoDto.getTitle(),
				todoDto.getDepth(),
				todoDto.getOrderNumber(),
				todoDto.getTodos()
					.stream()
					.map(createGoalTodoRequest -> createTodo(createGoalTodoRequest, goal))
					.collect(Collectors.toList()),
				goal);
		} else {
			return TodoTask.createTodoTask(
				todoDto.getTitle(),
				todoDto.getDepth(),
				todoDto.getOrderNumber(),
				LocalDate.parse(todoDto.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				LocalDate.parse(todoDto.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				RepetitionType.valueOf(todoDto.getRepetitionType()),
				todoDto.getRepetitionParams(),
				goal);
		}
	}
}
