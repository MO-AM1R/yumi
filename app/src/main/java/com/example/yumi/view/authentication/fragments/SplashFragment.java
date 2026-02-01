package com.example.yumi.view.authentication.fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.yumi.common.utils.SharedPreferencesKeys.KEY_ONBOARDING_COMPLETED;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import com.example.yumi.R;
import com.example.yumi.common.utils.SharedPreferencesKeys;
import com.example.yumi.databinding.FragmentSplashBinding;
import com.example.yumi.common.utils.AnimatorUtils;
import com.example.yumi.view.authentication.AuthenticationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;


@SuppressLint("CustomSplashScreen")
public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private AtomicBoolean isOperationComplete = new AtomicBoolean(false);
    private ExecutorService executorService;
    private TextView[] foods;
    private final List<ObjectAnimator> foodAnimators = new ArrayList<>();
    private Random random = new Random();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null){
            binding = FragmentSplashBinding.inflate(inflater);
            view = binding.getRoot();
        }else{
            binding = FragmentSplashBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BlurView blurView = binding.splashBlurView;
        ViewGroup rootView = binding.splashScreen;
        foods = new TextView[]{
                binding.fruit1, binding.fruit2, binding.fruit3,
                binding.fruit4, binding.fruit5, binding.fruit6
        };

        setupBlurView(blurView, rootView);
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
                startLoadingWithTimeout();
            }
        });

        setupFoodsAnimation();
        sequentialAnimator.start();
    }

    private void startLoadingWithTimeout() {
        executorService = Executors.newSingleThreadExecutor();

        long MAX_LOADING_TIME_MS = 2000;
        mainHandler.postDelayed(this::onLoadingComplete, MAX_LOADING_TIME_MS);
        executorService.execute(this::performNetworkOperation);
    }

    private void performNetworkOperation() {
        //TODO: check user logged in before
        //TODO: check user saw on boarding before
        if (isOperationComplete.compareAndSet(false, true)) {
            mainHandler.post(this::onLoadingComplete);
        }
    }

    private void onLoadingComplete() {
        if (!isOperationComplete.compareAndSet(false, true)) {
            // If already true, check if this is the first call to actually proceed
            // This handles the race condition between timeout and operation completion
        }

        // Remove any pending timeout callbacks
        mainHandler.removeCallbacksAndMessages(null);

        ((AuthenticationActivity) getActivity()).markSplashSeen();
        Log.d("SplashActivity", "Loading complete, proceeding to next screen");
        navigateToNextScreen();
    }

    private void navigateToNextScreen() {
        Log.d("SplashActivity", "Navigating to next screen...");

        SharedPreferences prefs
                = getActivity().getSharedPreferences(SharedPreferencesKeys.PREF_NAME, MODE_PRIVATE);

        boolean onboarding = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);

        if (onboarding){
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_loginFragment);
        }else{
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_onboardingFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mainHandler.removeCallbacksAndMessages(null);
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }

        mainHandler = null;
        isOperationComplete = null;
        executorService = null;
        foods = null;
        random = null;
        stopFoodsAnimation();
    }

    private void setupFoodsAnimation() {
        for (TextView textView : foods) {
            startRandomFadeAnimation(textView);
        }
    }

    private void startRandomFadeAnimation(TextView textView) {
        long duration = 1000 + random.nextInt(2300);
        long startDelay = random.nextInt(800);

        ObjectAnimator animator = AnimatorUtils.getFadeInAnimation(textView, 0f, 1f, 0f);
        animator.setDuration(duration);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startRandomFadeAnimation(textView);
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

        for (TextView textView: foods){
            textView.setAlpha(0);
        }
    }

    private void setupBlurView(BlurView blurView, ViewGroup rootView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurView.setupWith(rootView, new RenderEffectBlur())
                    .setBlurRadius((float) 10.0)
                    .setBlurAutoUpdate(true);
        } else {
            blurView.setupWith(rootView)
                    .setBlurRadius((float) 10.0);
        }
    }
}