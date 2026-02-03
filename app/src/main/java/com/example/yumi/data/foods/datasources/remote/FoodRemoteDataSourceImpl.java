package com.example.yumi.data.foods.datasources.remote;
import com.example.yumi.data.foods.datasources.remote.api.FoodServiceAPI;
import com.example.yumi.data.network.RetrofitClient;


public class FoodRemoteDataSourceImpl implements FoodRemoteDataSource{
    private final FoodServiceAPI foodServiceAPI;

    public FoodRemoteDataSourceImpl(){
        foodServiceAPI = RetrofitClient.INSTANCE.getFoodApi();
    }
}
