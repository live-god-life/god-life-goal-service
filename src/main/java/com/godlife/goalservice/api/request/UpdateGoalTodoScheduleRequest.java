package com.godlife.goalservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGoalTodoScheduleRequest {
	private Boolean completionStatus;
}
