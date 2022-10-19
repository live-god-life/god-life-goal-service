package com.godlife.goalservice.service.dto;

import com.godlife.goalservice.domain.Mindset;
import lombok.Builder;

@Builder
public class MindsetServiceDto {
    private Long mindsetId;
    private String content;

    public static MindsetServiceDto of(Mindset mindset) {

        return MindsetServiceDto.builder()
                .mindsetId(mindset.getMindsetId())
                .content(mindset.getContent())
                .build();
    }

    public Mindset toEntity() {
        return Mindset.builder()
                .content(content)
                .build();
    }
}
