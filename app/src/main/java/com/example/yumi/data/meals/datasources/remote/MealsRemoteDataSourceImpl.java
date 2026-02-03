package com.example.yumi.data.meals.datasources.remote;
import com.example.yumi.data.meals.datasources.remote.api.MealsServiceAPI;
import com.example.yumi.data.network.RetrofitClient;


public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {
    private final MealsServiceAPI mealsServiceAPI;

    public MealsRemoteDataSourceImpl(){
        mealsServiceAPI = RetrofitClient.INSTANCE.getFoodApi();
    }
}
