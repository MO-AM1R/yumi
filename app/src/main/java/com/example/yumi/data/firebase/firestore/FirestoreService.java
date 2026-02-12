package com.example.yumi.data.firebase.firestore;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.user.model.User;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public interface FirestoreService {
    Completable createUser(User user);

    Single<User> getUser(String userId);

    Completable setFavoriteMeals(String userId, List<String> mealIds);

    Completable setDayMeals(String userId, String date, DayMeals dayMeals);

    Completable removeDayMeals(String userId, String date);

    Completable syncUserData(User user);
}
