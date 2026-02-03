package com.example.yumi.data.meals.model.mapper;
import com.example.yumi.data.meals.model.dto.AreaDto;
import com.example.yumi.domain.meals.model.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class AreaMapper {
    public static Area mapToDomain(AreaDto dto) {
        if (dto == null) {
            return null;
        }

        String areaName = dto.getName() != null ? dto.getName() : "";

        return new Area(areaName);
    }

    public static List<Area> mapToDomainList(List<AreaDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Area> areas = new ArrayList<>();
        for (AreaDto dto : dtoList) {
            Area area = mapToDomain(dto);
            if (area != null) {
                areas.add(area);
            }
        }
        return areas;
    }

    public static String getCountryCode(String areaName) {
        return areaName.substring(0, 2).toLowerCase(Locale.ROOT);
    }
}