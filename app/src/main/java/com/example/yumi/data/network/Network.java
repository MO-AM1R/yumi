package com.example.yumi.data.network;
import com.example.yumi.data.config.APIConfig;
import com.example.yumi.data.foods.datasources.remote.api.FoodServiceAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public enum Network {
    INSTANCE;
    private static Retrofit retrofit;
    private static FoodServiceAPI foodServiceAPI;


    public static Network getInstance() {
        return INSTANCE;
    }

    public void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(APIConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    synchronized public FoodServiceAPI getMoviesService() {
        if (foodServiceAPI == null) {
            foodServiceAPI = retrofit.create(FoodServiceAPI.class);
        }

        return foodServiceAPI;
    }
}
