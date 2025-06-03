package com.example.ai_userbackend.dto;

import com.example.ai_userbackend.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoDto geo;

    public static AddressDto fromAddress(Address address) {
        AddressDto dto = new AddressDto();
        dto.setStreet(address.getStreet());
        dto.setSuite(address.getSuite());
        dto.setCity(address.getCity());
        dto.setZipcode(address.getZipcode());
        
        if (address.getGeo() != null) {
            dto.setGeo(GeoDto.fromGeo(address.getGeo()));
        }
        
        return dto;
    }
} 