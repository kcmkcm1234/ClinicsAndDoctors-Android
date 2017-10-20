package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

/**
 * Created by Daro on 27/07/2017.
 */

public class Clinic extends ClinicAndDoctor implements Parcelable {

    private int cantDoctors;

    public Clinic() {
    }

    public Clinic(ClinicAndDoctorResponse clinicAndDoctorResponse) {
        super(clinicAndDoctorResponse);
    }

    public int getCantDoctors() {
        return cantDoctors;
    }

    public Clinic setCantDoctors(int cantDoctors) {
        this.cantDoctors = cantDoctors;
        return this;
    }

    protected Clinic(Parcel in) {
        super(in);
        cantDoctors = in.readInt();
    }

    public static final Creator<Clinic> CREATOR = new Creator<Clinic>() {
        @Override
        public Clinic createFromParcel(Parcel in) {
            return new Clinic(in);
        }

        @Override
        public Clinic[] newArray(int size) {
            return new Clinic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(cantDoctors);
    }
}
