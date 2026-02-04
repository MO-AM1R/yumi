package com.example.yumi.presentation.browsecountries;
import com.example.yumi.domain.meals.model.Area;
import com.example.yumi.presentation.base.BaseView;
import java.util.List;


public interface BrowseCountriesContract {
    interface View extends BaseView {
        void showAreas(List<Area> areas);
    }

    interface Presenter{
        void loadAreas();
    }
}
