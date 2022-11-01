package com.godlife.goalservice.dto;

import com.godlife.goalservice.domain.Mindset;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MindsetDto {
	private Long mindsetId;
	private String content;

	public static MindsetDto of(Mindset mindset) {
		return MindsetDto.builder()
			.mindsetId(mindset.getMindsetId())
			.content(mindset.getContent())
			.build();
	}
}
