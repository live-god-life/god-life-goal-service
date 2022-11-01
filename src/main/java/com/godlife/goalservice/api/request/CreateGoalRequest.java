package com.godlife.goalservice.api.request;

import com.godlife.goalservice.domain.enums.Category;
import com.godlife.goalservice.dto.GoalDto;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class CreateGoalRequest {
	private String title;

	private String categoryName;
	private String categoryCode;

	private List<CreateGoalMindsetRequest> mindsets;
	private List<CreateGoalTodoRequest> todos;

	public GoalDto toGoalServiceDto() {

		return GoalDto.builder()
			.title(title)
			.category(Category.valueOf(categoryCode))
			.mindsets(mindsets.stream().map(CreateGoalMindsetRequest::toMindServiceDto).collect(Collectors.toList()))
			.todos(Optional.ofNullable(todos)
				.orElseGet(Collections::emptyList)
				.stream()
				.map(CreateGoalTodoRequest::toTodoServiceDto)
				.collect(Collectors.toList()))
			.build();
	}
}
