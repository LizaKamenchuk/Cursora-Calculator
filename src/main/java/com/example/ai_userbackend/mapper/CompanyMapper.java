package com.example.ai_userbackend.mapper;

import com.example.ai_userbackend.dto.CompanyDto;
import com.example.ai_userbackend.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Mapping(target = "id", ignore = true)
    Company toEntity(CompanyDto dto);

    CompanyDto toDto(Company entity);
} 