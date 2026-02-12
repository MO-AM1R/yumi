package com.example.yumi.presentation.authentication.view.fragments;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentOnboardingBinding;
import com.example.yumi.presentation.authentication.AuthContract;
import com.example.yumi.presentation.authentication.presenter.OnboardingPresenter;
import com.example.yumi.presentation.custom.WormDotIndicator;
import com.example.yumi.utils.AnimatorUtils;
import java.util.Arrays;
import java.util.List;


public class OnboardingFragment extends Fragment implements AuthContract.OnboardingView {

    private FragmentOnboardingBinding binding;
    private OnboardingPresenter presenter;

    private ImageView imageView;
    private TextView title;
    private TextView subTitle;
    private TextView details;
    private WormDotIndicator indicator;
    private Button button;

    private List<String> titles;
    private List<String> detailsList;
    private List<String> subTitles;
    private List<Integer> imageResources;
    private List<String> buttonTexts;
    private AnimatorSet animatorSet;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new OnboardingPresenter(requireActivity().getApplication(), this);
        presenter.attachView(this);

        initViews();
        setupData();

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("indicator_index");
            indicator.selectDot(index, false);
        }

        bindData();
        setupClickListeners();
    }

    private void initViews() {
        indicator = binding.wormDotIndicator;
        imageView = binding.onBoardingImage;
        button = binding.onBoardingButton;
        title = binding.onBoardingTitle;
        subTitle = binding.onBoardingSubTitle;
        details = binding.onBoardingDetails;
    }

    private void setupClickListeners() {
        binding.skipBtn.setOnClickListener(v -> presenter.onSkipClicked());

        button.setOnClickListener(v -> {
            int currentIndex = indicator.getIndex();
            presenter.onNextClicked(currentIndex);
        });
    }

    @Override
    public void showPage(int index) {
        indicator.selectDot(index, true);
        bindData();
    }

    @Override
    public void navigateToLogin() {
        if (isAdded() && !isDetached()) {
            try {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.action_onboardingFragment_to_loginFragment);
            } catch (Exception e) {
                showError("Navigation error");
            }
        }
    }

    @Override
    public int getTotalPages() {
        return 3;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(String message) {
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

        imageResources = Arrays.asList(
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

    private void bindData() {
        int currentIndex = indicator.getIndex();

        imageView.setImageResource(imageResources.get(currentIndex));
        button.setText(buttonTexts.get(currentIndex));
        title.setText(titles.get(currentIndex));
        subTitle.setText(subTitles.get(currentIndex));
        details.setText(detailsList.get(currentIndex));

        startAnimations();
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

        TextView[] textViews = new TextView[]{title, subTitle, details};

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isAdded() && indicator != null) {
            outState.putInt("indicator_index", indicator.getIndex());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
        binding = null;
    }
}