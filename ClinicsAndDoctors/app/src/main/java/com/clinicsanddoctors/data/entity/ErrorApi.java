package com.clinicsanddoctors.data.entity;

import com.clinicsanddoctors.data.remote.ClinicServices;
import com.google.gson.annotations.Expose;

public class ErrorApi implements ClinicServices.ApiError {

    @Expose
    String detail;
    @Expose
    String code;

    public ErrorApi() {
    }

    public ErrorApi(String error, String code) {
        this.detail = error;
        this.code = code;
    }

    @Override
    public String getError() {
        return detail;
    }

    @Override
    public String getCode() {
        return code;
    }

}

