package com.clinicsanddoctors.ui.main.map;

import android.graphics.Bitmap;

import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Daro on 27/07/2017.
 */

public class ClinicCluster implements ClusterItem {

    private LatLng mPosition;
    private Clinic mClinic;
    private Bitmap mIcon;

    public ClinicCluster(Bitmap icon, Clinic clinic) {
        mIcon = icon;
        mClinic = clinic;
        mPosition = new LatLng(clinic.getLatitude(), clinic.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Clinic getClinic() {
        return mClinic;
    }

    public Bitmap getmIcon() {
        return mIcon;
    }
}
