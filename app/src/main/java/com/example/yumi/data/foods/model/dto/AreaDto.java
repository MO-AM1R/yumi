package com.example.yumi.data.foods.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class AreaDto {
    @SerializedName("strArea")
    private String name;

    public String getName() {
        return name;
    }

    public String getCountryCode(String areaName) {
        return areaName.substring(0, 2).toLowerCase(Locale.ROOT);
    }
}