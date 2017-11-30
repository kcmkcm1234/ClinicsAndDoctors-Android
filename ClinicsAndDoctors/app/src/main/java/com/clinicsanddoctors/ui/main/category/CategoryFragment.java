package com.clinicsanddoctors.ui.main.category;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.DoctorAdapter;
import com.clinicsanddoctors.adapters.EmptyAdapter;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.ui.BaseClinicFragment;
import com.clinicsanddoctors.ui.doctorProfile.DoctorProfileActivity;

import java.util.List;

/**
 * Created by Daro on 09/08/2017.
 */

public class CategoryFragment extends BaseClinicFragment implements CategoryContract.View {

    private View view;
    private CategoryPresenter mPresenter;
    private RecyclerView mList;
    private Category mCategory;
    private Location mLocationMap;
    private List<Clinic> mClinicCluster;
    private Clinic mClinic;

    private TextView mDistance;
    private AppCompatSeekBar mSeekDistance;

    public CategoryFragment() {

    }

    @SuppressLint("ValidFragment")
    public CategoryFragment(Category category, Location location, List<Clinic> clinicCluster, Clinic clinic) {
        this.mCategory = category;
        this.mLocationMap = location;
        this.mClinicCluster = clinicCluster;
        this.mClinic = clinic;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        mPresenter = new CategoryPresenter(this, getContext());
        mList = (RecyclerView) view.findViewById(R.id.mList);
        mDistance = (TextView) view.findViewById(R.id.mDistance);
        mSeekDistance = (AppCompatSeekBar) view.findViewById(R.id.mSeekDistance);

        mDistance.setText(ClinicServices.RadiusSearch.RADIUS + "\n" + getString(R.string.kilometers_away));
        mSeekDistance.setMax(ClinicServices.RadiusSearch.RADIUS-1);
        mSeekDistance.setProgress((ClinicServices.RadiusSearch.RADIUS / ClinicServices.RadiusSearch.INTERVAL) + 1);
        mSeekDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = (seekBar.getProgress() * ClinicServices.RadiusSearch.INTERVAL) + 1;
                mDistance.setText(progress + "\n" + getString(R.string.km_away));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = (seekBar.getProgress() * ClinicServices.RadiusSearch.INTERVAL) + 1;
                mPresenter.getDoctors(mCategory, mLocationMap, progress);
            }
        });
        if (mClinicCluster != null && !mClinicCluster.isEmpty())
            showClinics(mClinicCluster);
        else {
            if (mClinic != null) {
                mPresenter.getDoctorsFromClinic(mClinic, mCategory);
                view.findViewById(R.id.mContainerSeekDistance).setVisibility(View.GONE);
            } else
                mPresenter.getDoctors(mCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS);
        }

        return view;
    }

    public void showClinics(List<Clinic> clinicsCluster) {

    }

    @Override
    public void showDoctors(List<Doctor> doctorsList) {
        if (!isAdded()) return;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(linearLayoutManager);
        if (doctorsList != null && !doctorsList.isEmpty())
            mList.setAdapter(new DoctorAdapter(doctorsList, new DoctorAdapter.CallbackProvider() {
                @Override
                public void onProviderSelected(ClinicAndDoctor clinicAndDoctor) {
                    Intent intent = new Intent(getContext(), DoctorProfileActivity.class);
                    intent.putExtra(DoctorProfileActivity.ARG_DOCTOR, clinicAndDoctor);
                    startActivity(intent);
                }
            }));
        else {
            if (mClinic != null)
                mList.setAdapter(new EmptyAdapter(getString(R.string.empty_doctors_in_clinic)));
            else
                mList.setAdapter(new EmptyAdapter(getString(R.string.empty_doctors)));
        }
    }
}
