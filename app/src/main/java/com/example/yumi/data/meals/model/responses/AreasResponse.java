package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.AreaDto;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class AreasResponse {

    @SerializedName("meals")
    private final List<AreaDto> areas;

    public AreasResponse() {
        areas = new ArrayList<>();
    }

    public AreasResponse(List<AreaDto> areas) {
        this.areas = areas;
    }

    public List<AreaDto> getAreas() {
        return areas;
    }
}