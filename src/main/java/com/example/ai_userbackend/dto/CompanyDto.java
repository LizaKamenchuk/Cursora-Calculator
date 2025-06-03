package com.example.ai_userbackend.dto;

import com.example.ai_userbackend.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String catchPhrase;
    private String bs;

    public static CompanyDto fromCompany(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setName(company.getName());
        dto.setCatchPhrase(company.getCatchPhrase());
        dto.setBs(company.getBs());
        return dto;
    }
} 