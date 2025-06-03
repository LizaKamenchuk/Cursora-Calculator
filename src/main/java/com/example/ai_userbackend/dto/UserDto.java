package com.example.ai_userbackend.dto;

import com.example.ai_userbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private AddressDto address;
    private CompanyDto company;

    public static UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setWebsite(user.getWebsite());
        
        if (user.getAddress() != null) {
            dto.setAddress(AddressDto.fromAddress(user.getAddress()));
        }
        
        if (user.getCompany() != null) {
            dto.setCompany(CompanyDto.fromCompany(user.getCompany()));
        }
        
        return dto;
    }
} 