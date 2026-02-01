package com.example.yumi.view.authentication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yumi.R;
import com.example.yumi.common.customviews.WormDotIndicator;
import com.example.yumi.common.utils.SharedPreferencesKeys;
import com.example.yumi.databinding.FragmentOnboardingBinding;
import com.example.yumi.common.utils.AnimatorUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class OnboardingFragment extends Fragment {
    private FragmentOnboardingBinding binding;
    private ImageView imageView;
    private TextView title;
    private TextView subTitle;
    private TextView details;
    private WormDotIndicator indicator;
    private Button button;
    private List<String> titles;
    private List<String> detailsList;
    private List<String> subTitles;
    private List<Integer> imageRecourses;
    private List<String> buttonTexts;
    private AnimatorSet animatorSet;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            binding = FragmentOnboardingBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentOnboardingBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        indicator = binding.wormDotIndicator;
        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("indicator_index");
            indicator.selectDot(index, false);
        }

        imageView = binding.onBoardingImage;
        button = binding.onBoardingButton;
        title = binding.onBoardingTitle;
        subTitle = binding.onBoardingSubTitle;
        details = binding.onBoardingDetails;

        setupData();
        binding();

        binding.skipBtn.setOnClickListener(v -> NavigateToNextPage());

        button.setOnClickListener(v -> {
            if (indicator.getIndex() == 2) {
                NavigateToNextPage();
                return;
            }

            indicator.selectDot(indicator.getIndex() + 1, true);
            binding();
        });
    }

    private void NavigateToNextPage() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences(SharedPreferencesKeys.PREF_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(SharedPreferencesKeys.KEY_ONBOARDING_COMPLETED, true).apply();

        View view = getView();
        if (view != null) {
            view.post(() -> {
                if (isAdded() && !isDetached()) {
                    try {
                        NavController navController = Navigation.findNavController(view);
                        if (navController.getCurrentDestination() != null) {
                            navController.navigate(R.id.action_onboardingFragment_to_loginFragment);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                    }
                }
            });
        }
    }

    private void startAnimations() {
        if (animatorSet == null) {
            setupAnimations();
        }

        animatorSet.start();
    }

    private void setupAnimations() {
        ObjectAnimator[] animators = new ObjectAnimator[7];
        int ind = 0;

        ObjectAnimator imageFadeIn = AnimatorUtils.getFadeInAnimation(imageView);
        animators[ind++] = imageFadeIn;

        TextView[] textViews = new TextView[]{
                title,
                subTitle,
                details,
        };

        for (TextView textView : textViews) {
            ObjectAnimator textFadeIn = AnimatorUtils.getFadeInAnimation(textView);
            ObjectAnimator textTransition = AnimatorUtils.getTranslateY(textView, 50f, 0f);

            animators[ind++] = textFadeIn;
            animators[ind++] = textTransition;
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setDuration(800);
    }

    private void setupData() {
        titles = Arrays.asList(
                getString(R.string.on_boarding_title_1),
                getString(R.string.on_boarding_title_2),
                getString(R.string.on_boarding_title_3)
        );

        detailsList = Arrays.asList(
                getString(R.string.on_boarding_details_1),
                getString(R.string.on_boarding_details_2),
                getString(R.string.on_boarding_details_3)
        );

        subTitles = Arrays.asList(
                getString(R.string.on_boarding_sub_title_1),
                getString(R.string.on_boarding_sub_title_2),
                getString(R.string.on_boarding_sub_title_3)
        );

        imageRecourses = Arrays.asList(
                R.drawable.onboarding_1,
                R.drawable.onboarding_2,
                R.drawable.onboarding_3
        );

        buttonTexts = Arrays.asList(
                getString(R.string.on_boarding_button_1),
                getString(R.string.on_boarding_button_2),
                getString(R.string.on_boarding_button_3)
        );
    }

    void binding() {
        int currentIndex = indicator.getIndex();

        imageView.setImageResource(imageRecourses.get(currentIndex));
        button.setText(buttonTexts.get(currentIndex));
        title.setText(titles.get(currentIndex));
        subTitle.setText(subTitles.get(currentIndex));
        details.setText(detailsList.get(currentIndex));
        startAnimations();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isAdded()) {
            outState.putInt("indicator_index", indicator.getIndex());
        }
    }
}