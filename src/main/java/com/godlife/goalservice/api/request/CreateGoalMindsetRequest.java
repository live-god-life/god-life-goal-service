package com.godlife.goalservice.api.request;

import com.godlife.goalservice.service.dto.MindsetServiceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGoalMindsetRequest {
    private String content;

    public MindsetServiceDto toMindServiceDto() {
        return MindsetServiceDto.builder()
                .content(content)
                .build();
    }
}

