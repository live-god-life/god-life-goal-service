package com.godlife.goalservice.api.request;

import com.godlife.goalservice.dto.MindsetDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGoalMindsetRequest {
	private String content;

	public MindsetDto toMindServiceDto() {
		return MindsetDto.builder()
			.content(content)
			.build();
	}
}
