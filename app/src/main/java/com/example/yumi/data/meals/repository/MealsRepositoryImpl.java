package com.example.yumi.data.meals.repository;

import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSource;
import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSourceImpl;
import com.example.yumi.domain.meals.repository.MealsRepository;

public class MealsRepositoryImpl implements MealsRepository {
    private final MealsRemoteDataSource mealsRemoteDataSource;

    public MealsRepositoryImpl(){
        mealsRemoteDataSource = new MealsRemoteDataSourceImpl();
    }
}
