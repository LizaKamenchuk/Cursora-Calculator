package com.example.ai_userbackend.dto;

import com.example.ai_userbackend.entity.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoDto {
    private Long id;
    private String lat;
    private String lng;

    public static GeoDto fromGeo(Geo geo) {
        GeoDto dto = new GeoDto();
        dto.setLat(geo.getLat());
        dto.setLng(geo.getLng());
        return dto;
    }
} 