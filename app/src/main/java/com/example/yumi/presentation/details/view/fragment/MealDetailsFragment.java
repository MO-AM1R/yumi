package com.example.yumi.presentation.details.view.fragment;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.yumi.utils.NetworkMonitor.INSTANCE;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentMealDetailsBinding;
import com.example.yumi.domain.meals.model.Meal;
import com.example.yumi.presentation.custom.AddToPlanBottomSheet;
import com.example.yumi.presentation.details.presenter.MealDetailsPresenter;
import com.example.yumi.presentation.details.view.MealDetailsContract;
import com.example.yumi.presentation.details.view.adapter.IngredientsAdapter;
import com.example.yumi.presentation.details.view.adapter.InstructionsAdapter;
import com.example.yumi.presentation.shared.callbacks.NavigationCallback;
import com.example.yumi.utils.NetworkMonitor;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;


public class MealDetailsFragment extends Fragment implements MealDetailsContract.View, NetworkMonitor.NetworkListener {
    private static final String ARG_MEAL = "arg_meal";
    private static final String KEY_MEAL = "key_meal";
    private FragmentMealDetailsBinding binding;
    private MealDetailsPresenter presenter;
    private NavigationCallback navigationCallback;
    private Meal meal;
    private IngredientsAdapter ingredientsAdapter;
    private InstructionsAdapter instructionsAdapter;


    public MealDetailsFragment() {}

    public MealDetailsFragment(Meal meal) {
        Log.d("TAG", "Meal");
        this.meal = meal;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    public static MealDetailsFragment newInstance(Meal meal) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        INSTANCE.addListener(this);

        if (INSTANCE.isConnected()) {
            onNetworkAvailable();
        } else {
            onNetworkLost();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        INSTANCE.removeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable(ARG_MEAL);
        }

        if (savedInstanceState != null) {
            meal = (Meal) savedInstanceState.getSerializable(KEY_MEAL);
        } else if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable(ARG_MEAL);
        }

        presenter = new MealDetailsPresenter(
                requireContext().getApplicationContext(),
                this
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);

        getLifecycle().addObserver(binding.youtubePlayerView);

        if (meal != null && meal.getYoutubeUrl() != null && !meal.getYoutubeUrl().isEmpty()) {
            binding.youtubePlayerView.addYouTubePlayerListener(
                    new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String id = meal.getYoutubeUrl()
                                    .substring(meal.getYoutubeUrl().indexOf("=") + 1);
                            youTubePlayer.loadVideo(id, 0);
                            youTubePlayer.pause();
                        }
                    }
            );
        }

        return binding.getRoot();
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireActivity().getString(R.string.ingredients)
        ));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireActivity().getString(R.string.instructions)
        ));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(
                requireActivity().getString(R.string.video)
        ));

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
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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
        if (presenter.isGuestMode()){
            showError(getString(R.string.you_are_in_guest_mode));
            return;
        }
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
        if (!isAdded() || binding == null) return;

        this.meal = meal;
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
        if (!isAdded() || binding == null) return;
        ingredientsAdapter.setIngredients(meal.getIngredients());
    }

    @Override
    public void showInstructions(Meal meal) {
        if (!isAdded() || binding == null) return;

        String instructions = meal.getInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            String[] steps = instructions.split("\\r?\\n");
            instructionsAdapter.setInstructions(steps);
        }
    }

    @Override
    public void showVideoSection(String videoUrl) {
        if (!isAdded() || binding == null || videoUrl == null || videoUrl.isEmpty()) return;
        binding.videoTitle.setText(getString(R.string.how_to_make_meal, meal.getName()));

        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                try {
                    String id = videoUrl.substring(videoUrl.indexOf("=") + 1);
                    youTubePlayer.cueVideo(id, 0);
                } catch (Exception e) {
                    Log.e("MealDetails", "Error parsing video ID");
                }
            }
        });
    }

    @Override
    public void updateFavoriteStatus(boolean isFavorite) {
        if (!isAdded() || binding == null) return;

        binding.favoriteIcon.setImageResource(
                isFavorite ? R.drawable.favorite_filled : R.drawable.favorite
        );
    }

    @Override
    public void showAddToPlanSuccess() {
        //TODO: show dialog
        if (!isAdded()) return;
    }

    @Override
    public void showAddToPlanError(String message) {
        //TODO: show dialog
        if (!isAdded()) return;
    }

    @Override
    public void showError(String message) {
        if (!isAdded()) return;
        Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        binding.loading.setVisibility(VISIBLE);
        binding.loading.setIndeterminate(true);
        binding.getRoot().setClickable(false);
        binding.getRoot().setFocusable(false);
        toggleView(GONE);
    }

    @Override
    public void hideLoading() {
        binding.loading.setVisibility(GONE);
        binding.loading.setIndeterminate(false);
        binding.getRoot().setClickable(true);
        binding.getRoot().setFocusable(false);
        toggleView(VISIBLE);
    }

    private void toggleView(int visible) {
        binding.nestedScrollView.setVisibility(visible);
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

    private void showNoInternetForVideo() {
        if (!isAdded() || binding == null) return;

        binding.playButtonContainer.setClickable(false);
        binding.playButtonContainer.setFocusable(false);

        binding.playVideoIcon.setImageResource(R.drawable.no_internet);

        binding.videoTitle.setText(R.string.no_internet_connection);
        binding.videoSubtitle.setText(
                R.string.no_internet_to_watch_the_video
        );

        binding.youtubePlayerView.setVisibility(View.GONE);
    }

    private void restoreVideoState(String videoTitle) {
        if (!isAdded() || binding == null) return;

        binding.playButtonContainer.setClickable(true);
        binding.playButtonContainer.setFocusable(true);

        binding.playVideoIcon.setImageResource(R.drawable.ic_play);

        binding.videoTitle.setText(videoTitle);
        binding.videoSubtitle.setText(
                R.string.watch_the_full_video_tutorial_for_this_recipe
        );
    }

    private void runIfUiReady(Runnable action) {
        if (!isAdded() || binding == null || getActivity() == null) return;
        getActivity().runOnUiThread(action);
    }

    @Override
    public void onNetworkLost() {
        runIfUiReady(this::showNoInternetForVideo);
    }

    @Override
    public void onNetworkAvailable() {
        runIfUiReady(() -> restoreVideoState(meal.getName()));
    }

}