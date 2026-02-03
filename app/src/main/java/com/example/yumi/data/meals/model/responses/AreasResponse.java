package com.example.yumi.data.meals.model.responses;
import com.example.yumi.data.meals.model.dto.AreaDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AreasResponse {

    @SerializedName("meals")
    private List<AreaDto> areas;

    public List<AreaDto> getAreas() {
        return areas;
    }
}