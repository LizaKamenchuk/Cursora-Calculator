package com.example.ai_userbackend.mapper;

import com.example.ai_userbackend.dto.AddressDto;
import com.example.ai_userbackend.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GeoMapper.class})
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressDto dto);

    AddressDto toDto(Address entity);
} 