package com.godlife.goalservice.utils.mapper;

import com.godlife.goalservice.domain.Mindset;
import com.godlife.goalservice.service.dto.MindsetServiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MindsetMapper {
    MindsetMapper INSTANCE = Mappers.getMapper(MindsetMapper.class);
    Mindset serviceDtoToEntity(MindsetServiceDto mindsetServiceDto);
    MindsetServiceDto entityToDto(Mindset mindset);
}
