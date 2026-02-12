package com.example.yumi.data.firebase.firestore;
import com.example.yumi.domain.plan.models.DayMeals;
import com.example.yumi.domain.plan.models.MealPlan;
import com.example.yumi.domain.user.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class FirestoreServiceImpl implements FirestoreService {
    private static final String COLLECTION_USERS = "users";
    private static final String SUBCOLLECTION_MEAL_PLANS = "mealPlans";

    private static final String FIELD_UID = "uid";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_DISPLAY_NAME = "displayName";
    private static final String FIELD_FAVORITE_MEALS = "favoriteMeals";
    private static final String FIELD_BREAKFAST = "breakfast";
    private static final String FIELD_LUNCH = "lunch";
    private static final String FIELD_DINNER = "dinner";
    private static final String FIELD_SNACK = "snack";

    private final FirebaseFirestore firestore;

    public FirestoreServiceImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Completable createUser(User user) {
        return Completable.create(emitter -> {
            DocumentReference userDoc = firestore.collection(COLLECTION_USERS).document(user.getUid());

            userDoc.get().addOnSuccessListener(snapshot -> {
                Map<String, Object> userData = new HashMap<>();
                userData.put(FIELD_UID, user.getUid());
                userData.put(FIELD_EMAIL, user.getEmail());
                userData.put(FIELD_DISPLAY_NAME, user.getDisplayName());

                if (snapshot.exists()) {
                    userDoc.set(userData, SetOptions.merge())
                            .addOnSuccessListener(unused -> emitter.onComplete())
                            .addOnFailureListener(emitter::onError);
                } else {
                    userData.put(FIELD_FAVORITE_MEALS, new ArrayList<String>());
                    userDoc.set(userData)
                            .addOnSuccessListener(unused -> emitter.onComplete())
                            .addOnFailureListener(emitter::onError);
                }
            }).addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Single<User> getUser(String userId) {
        Single<User> userObservable = Single.create(emitter ->
                firestore.collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        emitter.onSuccess(documentToUser(doc));
                    } else {
                        emitter.onError(new Exception("User not found"));
                    }
                })
                .addOnFailureListener(emitter::onError));

        Single<MealPlan> plannedMealsObservable = Single.create(emitter ->
                firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_MEAL_PLANS)
                .get()
                .addOnSuccessListener(doc -> {
                    try {
                        List<DocumentSnapshot> days = doc.getDocuments();
                        MealPlan mealPlan = new MealPlan();

                        for (DocumentSnapshot day : days) {
                            mealPlan.setDayMeals(
                                    day.getId(),
                                    documentToDayMeals(day)
                            );
                        }
                        emitter.onSuccess(mealPlan);
                    } catch (Exception e) {
                        emitter.onError(new Exception("User not found"));
                    }
                })
                .addOnFailureListener(emitter::onError));

        return Single.zip(userObservable, plannedMealsObservable,
                (user, mealPlan) -> new User.Builder()
                .fromUser(user)
                .mealPlan(mealPlan)
                .build());
    }

    @Override
    public Completable syncUserData(User user) {
        String userId = user.getUid();
        Completable updateFavorites = setFavoriteMeals(userId, user.getFavoriteMealIds());

        List<Completable> mealPlanUpdates = new ArrayList<>();
        MealPlan mealPlan = user.getMealPlan();

        if (mealPlan != null && mealPlan.getDays() != null) {
            for (Map.Entry<String, DayMeals> entry : mealPlan.getDays().entrySet()) {
                String date = entry.getKey();
                date = date.replace("/", "-");

                DayMeals dayMeals = entry.getValue();
                mealPlanUpdates.add(setDayMeals(userId, date, dayMeals));
            }
        }

        return updateFavorites.andThen(Completable.merge(mealPlanUpdates));
    }

    @Override
    public Completable setFavoriteMeals(String userId, List<String> mealIds) {
        return Completable.create(emitter -> {
            Map<String, Object> updates = new HashMap<>();
            updates.put(FIELD_FAVORITE_MEALS, mealIds);

            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .update(updates)
                    .addOnSuccessListener(unused -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Completable setDayMeals(String userId, String date, DayMeals dayMeals) {
        if (dayMeals.isEmpty()) {
            return removeDayMeals(userId, date);
        }

        return Completable.create(emitter -> {
            Map<String, Object> data = dayMealsToMap(dayMeals);

            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(SUBCOLLECTION_MEAL_PLANS)
                    .document(date)
                    .set(data)
                    .addOnSuccessListener(unused -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Completable removeDayMeals(String userId, String date) {
        return Completable.create(emitter ->
                firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_MEAL_PLANS)
                .document(date)
                .delete()
                .addOnSuccessListener(unused -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }

    private User documentToUser(DocumentSnapshot doc) {
        @SuppressWarnings("unchecked")
        List<String> favorites = (List<String>) doc.get(FIELD_FAVORITE_MEALS);

        return new User.Builder()
                .uid(doc.getString(FIELD_UID))
                .email(doc.getString(FIELD_EMAIL))
                .displayName(doc.getString(FIELD_DISPLAY_NAME))
                .favoriteMealIds(favorites != null ? favorites : new ArrayList<>())
                .build();
    }

    private DayMeals documentToDayMeals(DocumentSnapshot doc) {
        String breakfast = doc.getString(FIELD_BREAKFAST);
        String lunch = doc.getString(FIELD_LUNCH);
        String dinner = doc.getString(FIELD_DINNER);
        String snack = doc.getString(FIELD_SNACK);

        return new DayMeals(breakfast, lunch, dinner, snack);
    }

    private Map<String, Object> dayMealsToMap(DayMeals dayMeals) {
        Map<String, Object> map = new HashMap<>();
        if (dayMeals.getBreakfast() != null) {
            map.put(FIELD_BREAKFAST, dayMeals.getBreakfast());
        }
        if (dayMeals.getLunch() != null) {
            map.put(FIELD_LUNCH, dayMeals.getLunch());
        }
        if (dayMeals.getDinner() != null) {
            map.put(FIELD_DINNER, dayMeals.getDinner());
        }
        if (dayMeals.getSnack() != null) {
            map.put(FIELD_SNACK, dayMeals.getSnack());
        }
        return map;
    }
}