package com.godlife.goalservice.dto;

import com.godlife.goalservice.domain.Mindset;
import com.querydsl.core.annotations.QueryProjection;

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

	@QueryProjection
	public MindsetDto(Long mindsetId, String content) {
		this.mindsetId = mindsetId;
		this.content = content;
	}
}
