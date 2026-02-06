package com.example.yumi.data.meals.repository;
import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSource;
import com.example.yumi.data.meals.datasources.remote.MealsRemoteDataSourceImpl;
import com.example.yumi.data.meals.model.mapper.AreaMapper;
import com.example.yumi.data.meals.model.mapper.CategoryMapper;
import com.example.yumi.data.meals.model.mapper.IngredientMapper;
import com.example.yumi.data.meals.model.mapper.MealMapper;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.domain.meals.model.Category;
import com.example.yumi.domain.meals.model.Ingredient;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.domain.meals.repository.MealsRepository;
import java.util.Collections;
import java.util.List;
import io.reactivex.rxjava3.core.Single;


public class MealsRepositoryImpl implements MealsRepository {
    private final MealsRemoteDataSource mealsRemoteDataSource;

    public MealsRepositoryImpl() {
        mealsRemoteDataSource = new MealsRemoteDataSourceImpl();
    }

    @Override
    public Single<Meal> getDayMeal() {
        return mealsRemoteDataSource.geDayMeal().map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return null;

                    return MealMapper.mapToDomain(mealsResponse.getMeals().get(0));
                }
        );
    }

    @Override
    public Single<List<Meal>> getRandomMeals(String letter) {
        return mealsRemoteDataSource.searchMealsByFirstLetter(letter).map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return Collections.emptyList();

                    return MealMapper.mapToDomainList(mealsResponse.getMeals());
                }
        );
    }

    @Override
    public Single<Meal> getMealById(String mealId) {
        return mealsRemoteDataSource.getMealById(mealId).map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return null;

                    return MealMapper.mapToDomain(mealsResponse.getMeals().get(0));
                }
        );
    }

    @Override
    public Single<Meal> searchMealsByName(String mealName) {
        return null;
    }

    @Override
    public Single<List<Meal>> searchMealsByFirstLetter(String firstLetter) {
        return null;
    }

    @Override
    public Single<List<Meal>> filterMealsByCategory(String category) {
        return mealsRemoteDataSource.filterMealsByCategory(category).map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return null;

                    return MealMapper.mapToDomainList(mealsResponse.getMeals());
                }
        );
    }

    @Override
    public Single<List<Meal>> filterMealsByArea(String area) {
        return mealsRemoteDataSource.filterMealsByArea(area).map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return null;

                    return MealMapper.mapToDomainList(mealsResponse.getMeals());
                }
        );
    }

    @Override
    public Single<List<Meal>> filterMealsByIngredient(String ingredient) {
        return mealsRemoteDataSource.filterMealsByIngredient(ingredient).map(
                mealsResponse -> {
                    if (mealsResponse.getMeals().isEmpty())
                        return null;

                    return MealMapper.mapToDomainList(mealsResponse.getMeals());
                }
        );
    }

    @Override
    public Single<List<Category>> getAllCategories() {
        return mealsRemoteDataSource.getAllCategories().map(
                mealsResponse -> {
                    if (mealsResponse.getCategories().isEmpty())
                        return Collections.emptyList();

                    return CategoryMapper.mapToDomainList(mealsResponse.getCategories());
                }
        );
    }

    @Override
    public Single<List<Area>> getAllAreas() {
        return mealsRemoteDataSource.getAllAreas().map(
                mealsResponse -> {
                    if (mealsResponse.getAreas().isEmpty())
                        return Collections.emptyList();

                    return AreaMapper.mapToDomainList(mealsResponse.getAreas());
                }
        );
    }

    @Override
    public Single<List<Ingredient>> getAllIngredients() {
        return mealsRemoteDataSource.getAllIngredients().map(
                mealsResponse -> {
                    if (mealsResponse.getIngredients().isEmpty())
                        return Collections.emptyList();

                    return IngredientMapper.mapToDomainList(mealsResponse.getIngredients());
                }
        );
    }
}
