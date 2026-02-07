package com.example.yumi.presentation.details.view.fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.yumi.R;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.yumi.databinding.FragmentMealDatailsBinding;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.custom.AddToPlanBottomSheet;
import com.example.yumi.presentation.details.presenter.MealDetailsPresenter;
import com.example.yumi.presentation.details.view.MealDetailsContract;
import com.example.yumi.presentation.details.view.adapter.IngredientsAdapter;
import com.example.yumi.presentation.details.view.adapter.InstructionsAdapter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;


public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {
    private static final String ARG_MEAL = "arg_meal";
    private static final String KEY_MEAL = "key_meal";
    private FragmentMealDatailsBinding binding;
    private MealDetailsPresenter presenter;
    private NavigationCallback navigationCallback;
    private Meal meal;
    private IngredientsAdapter ingredientsAdapter;
    private InstructionsAdapter instructionsAdapter;


    public MealDetailsFragment() {}

    public MealDetailsFragment(Meal meal) {
        this.meal = meal;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            meal = (Meal) savedInstanceState.getSerializable(KEY_MEAL);
        } else if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable(ARG_MEAL);
        }

        presenter = new MealDetailsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            binding = FragmentMealDatailsBinding.inflate(inflater);
            view = binding.getRoot();
        } else {
            binding = FragmentMealDatailsBinding.bind(view);
        }

        getLifecycle().addObserver(binding.youtubePlayerView);
        binding.youtubePlayerView.addYouTubePlayerListener(
                new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String id = meal.getYoutubeUrl().
                                substring(meal.getYoutubeUrl().indexOf("=") + 1);
                        youTubePlayer.loadVideo(id, 0);
                        youTubePlayer.pause();
                    }
                }
        );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        setupTabs();
        setupRecyclerViews();
        setupClickListeners();

        if (meal != null) {
            presenter.loadMealDetails(meal);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (meal != null) {
            outState.putSerializable(KEY_MEAL, meal);
        }
    }

    private void setupToolbar() {
        binding.backBtn.setOnClickListener(v -> {
            if (navigationCallback != null) {
                navigationCallback.popFragment();
            }
        });
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ingredients"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Instructions"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Video"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showIngredientsTab();
                        break;
                    case 1:
                        showInstructionsTab();
                        break;
                    case 2:
                        showVideoTab();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupRecyclerViews() {
        ingredientsAdapter = new IngredientsAdapter();
        binding.ingredientsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.ingredientsRecycler.setAdapter(ingredientsAdapter);

        instructionsAdapter = new InstructionsAdapter();
        binding.instructionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.instructionsRecycler.setAdapter(instructionsAdapter);
    }

    private void setupClickListeners() {
        binding.favoriteBtn.setOnClickListener(v -> {
            if (meal != null) {
                presenter.toggleFavorite(meal);
            }
        });

        binding.addToPlanBtn.setOnClickListener(v -> showAddToPlanBottomSheet());

        binding.playButtonContainer.setOnClickListener(v -> playYouTubeVideo());
    }

    private void showIngredientsTab() {
        binding.ingredientsRecycler.setVisibility(View.VISIBLE);
        binding.instructionsRecycler.setVisibility(View.GONE);
        binding.videoContainer.setVisibility(View.GONE);
    }

    private void showInstructionsTab() {
        binding.ingredientsRecycler.setVisibility(View.GONE);
        binding.instructionsRecycler.setVisibility(View.VISIBLE);
        binding.videoContainer.setVisibility(View.GONE);
    }

    private void showVideoTab() {
        binding.ingredientsRecycler.setVisibility(View.GONE);
        binding.instructionsRecycler.setVisibility(View.GONE);
        binding.videoContainer.setVisibility(View.VISIBLE);
    }

    private void playYouTubeVideo() {
        binding.videoCard.setVisibility(View.GONE);
        binding.youtubePlayerView.setVisibility(View.VISIBLE);

        binding.youtubePlayerView.getYouTubePlayerWhenReady(YouTubePlayer::play);
    }

    private void showAddToPlanBottomSheet() {
        AddToPlanBottomSheet bottomSheet = AddToPlanBottomSheet.newInstance();
        bottomSheet.setOnConfirmListener((date, mealType) -> {
            if (meal != null) {
                presenter.addToMealPlan(meal, date, mealType);
            }
        });
        bottomSheet.show(getChildFragmentManager(), "addToPlan");
    }

    @Override
    public void showMealDetails(Meal meal) {
        binding.mealName.setText(meal.getName());
        binding.mealCategory.setText(meal.getCategory());
        binding.mealArea.setText(meal.getArea());

        Glide.with(this)
                .load(meal.getThumbnailUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.mealImage);
    }

    @Override
    public void showIngredients(Meal meal) {
        ingredientsAdapter.setIngredients(meal.getIngredients());
    }

    @Override
    public void showInstructions(Meal meal) {
        String instructions = meal.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            String[] steps = instructions.split("\\r?\\n");
            instructionsAdapter.setInstructions(steps);
        }
    }

    @Override
    public void showVideoSection(String videoUrl) {
        binding.videoTitle.setText(
                getString(R.string.how_to_make_meal, meal.getName())
        );
    }

    @Override
    public void updateFavoriteStatus(boolean isFavorite) {

    }

    @Override
    public void showAddToPlanSuccess() {
        Toast.makeText(requireContext(), "Added to meal plan!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddToPlanError(String message) {
        Toast.makeText(requireContext(), "Failed to add: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddToFavoriteSuccess() {
        Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveFromFavoriteSuccess() {
        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.youtubePlayerView.release();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationCallback = null;
    }
}