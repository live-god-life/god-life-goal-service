package com.godlife.goalservice.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGoalTodoRequest {
	private String title;
	private String type;
	private Integer depth;
	private Integer orderNumber;
	private String startDate;
	private String endDate;
	private String repetitionType;
	private List<String> repetitionParams;
	private String notification;
	private List<CreateGoalTodoRequest> todos;
}
