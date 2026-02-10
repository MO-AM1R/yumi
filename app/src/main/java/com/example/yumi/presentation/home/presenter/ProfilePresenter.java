package com.example.yumi.presentation.home.presenter;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.yumi.data.favorite.repository.FavoriteRepositoryImpl;
import com.example.yumi.data.user.repository.UserRepositoryImpl;
import com.example.yumi.domain.favorites.repository.FavoriteRepository;
import com.example.yumi.domain.user.model.User;
import com.example.yumi.domain.user.repository.UserRepository;
import com.example.yumi.presentation.base.BasePresenter;
import com.example.yumi.presentation.home.contract.ProfileContract;
import com.example.yumi.utils.LocaleHelper;
import com.example.yumi.utils.ThemeHelper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProfilePresenter extends BasePresenter<ProfileContract.View>
        implements ProfileContract.Presenter {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final Context applicationContext;

    public ProfilePresenter(Context context) {
        this.applicationContext = context;
        userRepository = new UserRepositoryImpl(context);
        favoriteRepository = new FavoriteRepositoryImpl(context);
    }

    private void onUserDetailsLoaded(User user, int favoriteMeals) {
        withView(v -> {
            v.hideLoading();
            v.showUserDetails(user);
            v.showUserFavoriteMealsCounter(favoriteMeals);
        });
    }

    @Override
    public void loadUserDetails() {
        withView(v -> {
            v.showLoading();

            User user = userRepository.getCurrentUser();
            Disposable disposable = favoriteRepository.getAllFavoriteIds()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            strings -> onUserDetailsLoaded(user, strings.size()),
                            throwable -> v.showError(throwable.getLocalizedMessage())
                    );

            compositeDisposable.add(disposable);
        });
    }

    @Override
    public void onLanguageChanged(String lang) {
        if (LocaleHelper.isSameLanguage(applicationContext, lang)) {
            return;
        }

        LocaleHelper.saveLanguage(applicationContext, lang);
        withView(ProfileContract.View::resetLanguage);
    }

    @Override
    public void onModeChanged(boolean checked) {
        ThemeHelper.saveTheme(applicationContext, checked);
        AppCompatDelegate.setDefaultNightMode(
                checked
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    @Override
    public void logout() {
        Disposable disposable = userRepository.signOut().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> withView(ProfileContract.View::onLogout)
                );

        compositeDisposable.add(disposable);
    }
}
