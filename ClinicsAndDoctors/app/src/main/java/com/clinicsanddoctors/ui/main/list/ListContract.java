package com.clinicsanddoctors.ui.main.list;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

import java.util.List;

public interface ListContract {
    interface View extends LoaderView {
        void showCategory(List<Category> categories);
        void loadResults(List<ClinicAndDoctorResponse> clinicAndDoctorResponses);
    }

    interface Presenter {
        void getCategory();
        void search(String query, Clinic clinic);
    }
}
