package com.clinicsanddoctors.ui.review;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Review;

import java.util.List;

/**
 * Created by Daro on 20/10/2017.
 */

public interface ReviewContract {
    interface View extends LoaderView {
        void showReviews(List<Review> list);
    }

    interface Presenter {
        void getReviews(ClinicAndDoctor clinicAndDoctor);
    }
}
