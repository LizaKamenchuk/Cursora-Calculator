package com.example.ai_userbackend.mapper;

import com.example.ai_userbackend.dto.RegisterRequest;
import com.example.ai_userbackend.dto.UserDto;
import com.example.ai_userbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CompanyMapper.class})
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "company", ignore = true)
    User toEntity(RegisterRequest request);
} 