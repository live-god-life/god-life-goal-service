package com.godlife.goalservice.utils.mapper;

import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.service.dto.MindsetServiceDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MindsetMapperTest {

    @Test
    void entityToDto() {
        //given
        Mindset mindset = Mindset.builder()
                .content("마인드셋컨텐트")
                .build();
        //when
        MindsetServiceDto mindsetServiceDto = MindsetMapper.INSTANCE.entityToDto(mindset);

        //then
        assertThat(mindsetServiceDto.getContent()).isEqualTo(mindset.getContent());
    }

    @Test
    void dtoToEntity() {
        //given
        MindsetServiceDto mindsetServiceDto = MindsetServiceDto.builder()
                .content("마인드셋")
                .build();
        //when
        Mindset mindset = MindsetMapper.INSTANCE.serviceDtoToEntity(mindsetServiceDto);

        //then
        assertThat(mindset.getContent()).isEqualTo(mindsetServiceDto.getContent());
    }
}