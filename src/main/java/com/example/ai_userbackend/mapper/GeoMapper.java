package com.example.ai_userbackend.mapper;

import com.example.ai_userbackend.dto.GeoDto;
import com.example.ai_userbackend.entity.Geo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeoMapper {
    @Mapping(target = "id", ignore = true)
    Geo toEntity(GeoDto dto);

    GeoDto toDto(Geo entity);
} 