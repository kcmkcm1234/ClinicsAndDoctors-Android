package com.clinicsanddoctors.contracts;

public interface LoaderView {

    void showProgressDialog();

    void hideProgressDialog();

    void showErrorAlert(String message);
}
