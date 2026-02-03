package com.example.yumi.data.foods.repository;

import com.example.yumi.data.foods.datasources.remote.FoodRemoteDataSource;
import com.example.yumi.data.foods.datasources.remote.FoodRemoteDataSourceImpl;
import com.example.yumi.domain.foods.repository.FoodRepository;

public class FoodRepositoryImpl implements FoodRepository {
    private final FoodRemoteDataSource foodRemoteDataSource;

    public FoodRepositoryImpl(){
        foodRemoteDataSource = new FoodRemoteDataSourceImpl();
    }
}
