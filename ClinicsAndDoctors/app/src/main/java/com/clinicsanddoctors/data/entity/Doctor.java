package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.clinicsanddoctors.data.remote.respons.DoctorResponse;

/**
 * Created by Daro on 27/07/2017.
 */

public class Doctor extends ClinicAndDoctor implements Parcelable {

    private Clinic clinic;
    private String nationality;

    public Doctor() {

    }

    public Doctor(DoctorResponse doctorResponse, Category category){
        super(doctorResponse, category);
        clinic = new Clinic(doctorResponse.getClinic());
        nationality = doctorResponse.getNationality();
    }

    public Doctor(ClinicAndDoctorResponse clinicAndDoctorResponse, Category category) {
        super(clinicAndDoctorResponse, category);
        clinic = new Clinic(clinicAndDoctorResponse.getClinic());
        nationality = clinicAndDoctorResponse.getNationality();
    }

    protected Doctor(Parcel in) {
        super(in);
        clinic = in.readParcelable(Clinic.class.getClassLoader());
        nationality = in.readString();
    }

    public Clinic getClinic() {
        return clinic;
    }

    public Doctor setClinic(Clinic clinic) {
        this.clinic = clinic;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public Doctor setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(clinic, flags);
        dest.writeString(nationality);
    }
}
