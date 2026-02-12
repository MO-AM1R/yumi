package com.example.yumi.presentation.authentication.view.fragments;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentSplashBinding;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.authentication.presenter.SplashPresenter;
import com.example.yumi.presentation.authentication.view.activities.AuthenticationActivity;
import com.example.yumi.utils.AnimatorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;


@SuppressLint("CustomSplashScreen")
public class SplashFragment extends Fragment implements AuthContract.SplashView {
    private FragmentSplashBinding binding;
    private SplashPresenter presenter;
    private TextView[] foods;
    private final List<ObjectAnimator> foodAnimators = new ArrayList<>();
    private Random random = new Random();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SplashPresenter(requireActivity().getApplication(), this);
        presenter.attachView(this);

        foods = new TextView[]{
                binding.fruit1, binding.fruit2, binding.fruit3,
                binding.fruit4, binding.fruit5, binding.fruit6
        };

        setupBlurView(binding.splashBlurView, binding.splashScreen);
        setupAnimations();
    }

    private void setupAnimations() {
        AnimatorSet forkLogoAnimator = getForkLogoAnimator();
        AnimatorSet logoAnimator = getLogoAnimator();
        ObjectAnimator sloganAlpha = getSloganAlpha();
        ObjectAnimator progressAlpha = getProgressAlpha();

        beforeAnimation();

        AnimatorSet sequentialAnimator = new AnimatorSet();
        sequentialAnimator.playSequentially(forkLogoAnimator, logoAnimator,
                sloganAlpha, progressAlpha);

        sequentialAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                presenter.onAnimationsComplete();
            }
        });

        setupFoodsAnimation();
        sequentialAnimator.start();
    }

    @Override
    public void navigateToOnboarding() {
        if (isAdded() && !isDetached()) {
            markSplashSeen();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_onboardingFragment);
        }
    }

    @Override
    public void navigateToLogin() {
        if (isAdded() && !isDetached()) {
            markSplashSeen();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_loginFragment);
        }
    }

    @Override
    public void navigateToHome() {
        if (isAdded() && !isDetached()) {
            markSplashSeen();
            Navigation.findNavController(requireView())
                    .navigate(SplashFragmentDirections.actionSplashFragmentToHomeBaseActivity());
            requireActivity().finish();
        }
    }

    @Override
    public void showLoading() {
        binding.splashCircularProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.splashCircularProgress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
    }

    private void markSplashSeen() {
        if (getActivity() instanceof AuthenticationActivity) {
            ((AuthenticationActivity) getActivity()).markSplashSeen();
        }
    }

    private void setupFoodsAnimation() {
        for (TextView textView : foods) {
            startRandomFadeAnimation(textView);
        }
    }

    private void startRandomFadeAnimation(TextView textView) {
        if (!isAdded()) return;

        long duration = 1000 + random.nextInt(2300);
        long startDelay = random.nextInt(800);

        ObjectAnimator animator = AnimatorUtils.getFadeInAnimation(textView, 0f, 1f, 0f);
        animator.setDuration(duration);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAdded()) {
                    startRandomFadeAnimation(textView);
                }
            }
        });

        foodAnimators.add(animator);
        animator.start();
    }

    private void stopFoodsAnimation() {
        for (ObjectAnimator animator : foodAnimators) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
        foodAnimators.clear();
    }

    private ObjectAnimator getProgressAlpha() {
        return AnimatorUtils.getFadeInAnimation(binding.splashCircularProgress, 500);
    }

    @NonNull
    private ObjectAnimator getSloganAlpha() {
        return AnimatorUtils.getFadeInAnimation(binding.yumiSlogan, 500);
    }

    @NonNull
    private AnimatorSet getLogoAnimator() {
        ObjectAnimator logoTranslateY =
                AnimatorUtils.getTranslateY(binding.yumiLogo, 100f, 0f);

        ObjectAnimator logoAlpha = AnimatorUtils.getFadeInAnimation(binding.yumiLogo);

        AnimatorSet logoAnimator = new AnimatorSet();
        logoAnimator.playTogether(logoTranslateY, logoAlpha);
        logoAnimator.setDuration(1000);

        return logoAnimator;
    }

    @NonNull
    private AnimatorSet getForkLogoAnimator() {
        AnimatorSet scaleAnimatorSet = AnimatorUtils.getScaleAnimation(binding.logoGlassContainer);
        ObjectAnimator fadeIn = AnimatorUtils.getFadeInAnimation(binding.logoGlassContainer);

        AnimatorSet forkLogoAnimator = new AnimatorSet();
        forkLogoAnimator.playTogether(scaleAnimatorSet, fadeIn);
        forkLogoAnimator.setDuration(1300);
        return forkLogoAnimator;
    }

    private void beforeAnimation() {
        binding.yumiLogo.setAlpha(0f);
        binding.logoGlassContainer.setScaleX(0f);
        binding.logoGlassContainer.setScaleY(0f);
        binding.yumiSlogan.setAlpha(0f);
        binding.splashCircularProgress.setAlpha(0f);

        for (TextView textView : foods) {
            textView.setAlpha(0);
        }
    }

    private void setupBlurView(BlurView blurView, ViewGroup rootView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurView.setupWith(rootView, new RenderEffectBlur())
                    .setBlurRadius(10.0f)
                    .setBlurAutoUpdate(true);
        } else {
            blurView.setupWith(rootView)
                    .setBlurRadius(10.0f);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (presenter != null) {
            presenter.detachView();
        }

        stopFoodsAnimation();
        foods = null;
        random = null;
        binding = null;
    }
}