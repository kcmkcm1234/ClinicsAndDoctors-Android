package com.clinicsanddoctors.ui.review;

import android.content.Context;

import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 20/10/2017.
 */

public class ReviewPresenter implements ReviewContract.Presenter {
    private ReviewContract.View mView;
    private Context mContext;

    public ReviewPresenter(ReviewContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getReviews(Doctor doctor) {
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            reviewList.add(new Review());
        mView.showReviews(reviewList);
    }
}
