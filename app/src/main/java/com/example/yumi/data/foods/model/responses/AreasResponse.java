package com.example.yumi.data.foods.model.responses;
import com.example.yumi.data.foods.model.dto.AreaDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AreasResponse {

    @SerializedName("meals")
    private List<AreaDto> areas;

    public List<AreaDto> getAreas() {
        return areas;
    }
}