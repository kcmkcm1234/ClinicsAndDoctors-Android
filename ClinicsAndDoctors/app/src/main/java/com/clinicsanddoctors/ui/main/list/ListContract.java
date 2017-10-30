package com.clinicsanddoctors.ui.main.list;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;

import java.util.List;

public interface ListContract {
    interface View extends LoaderView {
        void showCategory(List<Category> categories);
    }

    interface Presenter {
        void getCategory();
    }
}
