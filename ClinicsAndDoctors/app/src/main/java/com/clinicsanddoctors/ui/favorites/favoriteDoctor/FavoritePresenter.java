package com.clinicsanddoctors.ui.favorites.favoriteDoctor;

import android.content.Context;
import android.location.Location;

import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.Doctor;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Daro on 23/10/2017.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View mView;
    private Context mContext;

    public FavoritePresenter(FavoriteContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getDoctorFavorite() {

        Location location = ClinicsApplication.getInstance().getCurrentLocation();
        LatLng latLng1 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng latLng2 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng latLng3 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));

        Clinic clinic1 = (Clinic) new Clinic().setCantDoctors(30).setAddress("Dallas, TX, United State").setName("Mayo Clinic").setLatitude(latLng1.latitude).setLongitude(latLng1.longitude).setRating("3");
        Clinic clinic2 = (Clinic) new Clinic().setCantDoctors(10).setAddress("Dallas, TX, United State").setName("Infinix Clinic").setPicture("https://pbs.twimg.com/profile_images/531871789672980482/N_mXF1j0.png").setLatitude(latLng2.latitude).setLongitude(latLng2.longitude).setRating("3");
        Clinic clinic3 = (Clinic) new Clinic().setCantDoctors(20).setAddress("Dallas, TX, United State").setName("Infi-Health").setLatitude(latLng3.latitude).setLongitude(latLng3.longitude).setRating("3");

        Category category1 = new Category();
        category1.setName("Cardiology");

        List<Doctor> doctors = new ArrayList<>();
        doctors.add((Doctor) new Doctor().setNationality("Australian").setClinic(clinic1).setName("Dr. Mery Clough").setCategory(category1).setPicture("https://bestdoctors.com/wp-content/uploads/2016/11/Doctor-with-Tablet.jpg").setRating("3"));
        doctors.add((Doctor) new Doctor().setNationality("Australian").setClinic(clinic1).setName("M.D. Bryant Word").setCategory(category1).setPicture("http://www.doctormateos.com/wp-content/uploads/2014/11/drmateos0.png").setRating("5"));
        doctors.add((Doctor) new Doctor().setNationality("Australian").setClinic(clinic1).setName("Dr. Selma Godoy").setCategory(category1).setPicture("http://www.omagg.com/wp-content/uploads/2017/07/Doctor.jpg").setRating("2.5"));
        doctors.add((Doctor) new Doctor().setNationality("Australian").setClinic(clinic2).setName("Dr. James Arthur").setCategory(category1).setPicture("").setRating("4"));
        doctors.add((Doctor) new Doctor().setNationality("Australian").setClinic(clinic3).setName("Dr. Doug Caudell").setCategory(category1).setPicture("http://www.clinicacemtro.com/media/djcatalog2/images/item/0/manuel-leyes-vince_f.jpg").setRating("3.8"));

        mView.showDoctorFavorite(doctors);
    }

    @Override
    public void getClinicFavorite() {
        Location location = ClinicsApplication.getInstance().getCurrentLocation();
        List<Clinic> clinicList = new ArrayList<>();
        LatLng latLng1 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng latLng2 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng latLng3 = getRandomLocation(new LatLng(location.getLatitude(), location.getLongitude()));

        clinicList.add((Clinic) new Clinic().setCantDoctors(30).setCategory(new Category().setName("Medial Center")).setAddress("Dallas, TX, United State").setName("Mayo Clinic").setLatitude(latLng1.latitude).setLongitude(latLng1.longitude).setRating("3"));
        clinicList.add((Clinic) new Clinic().setCantDoctors(10).setCategory(new Category().setName("Medial Center")).setAddress("Dallas, TX, United State").setName("Infinix Clinic").setPicture("https://pbs.twimg.com/profile_images/531871789672980482/N_mXF1j0.png").setLatitude(latLng2.latitude).setLongitude(latLng2.longitude).setRating("3"));
        clinicList.add((Clinic) new Clinic().setCantDoctors(20).setCategory(new Category().setName("Medial Center")).setAddress("Dallas, TX, United State").setName("Infi-Health").setLatitude(latLng3.latitude).setLongitude(latLng3.longitude).setRating("3"));

        mView.showClinicFavorite(clinicList);
    }

    private LatLng getRandomLocation(LatLng point) {

        int radius = 1000;
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for (int i = 0; i < 10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;
            Random random = new Random();
            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }
}
