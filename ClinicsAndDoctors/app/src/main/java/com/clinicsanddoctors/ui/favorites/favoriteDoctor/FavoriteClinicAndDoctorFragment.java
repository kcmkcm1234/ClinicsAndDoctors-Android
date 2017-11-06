package com.clinicsanddoctors.ui.favorites.favoriteDoctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.ClinicAdapter;
import com.clinicsanddoctors.adapters.DoctorAdapter;
import com.clinicsanddoctors.adapters.EmptyAdapter;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.ui.BaseClinicFragment;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.doctorProfile.DoctorProfileActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Daro on 23/10/2017.
 */

public class FavoriteClinicAndDoctorFragment extends BaseClinicFragment implements FavoriteContract.View {

    public static final String ARG_IS_DOCTOR = "ARG_IS_DOCTOR";
    private View view;
    private FavoritePresenter mPresenter;
    private RecyclerView mList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        mPresenter = new FavoritePresenter(this, getContext());
        mList = (RecyclerView) view.findViewById(R.id.mList);

        boolean isDoctor = getArguments().getBoolean(ARG_IS_DOCTOR, true);
        if (isDoctor)
            mPresenter.getDoctorFavorite();
        else
            mPresenter.getClinicFavorite();
        return view;
    }

    @Override
    public void showDoctorFavorite(List<Doctor> doctorList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(linearLayoutManager);
        if (doctorList != null && !doctorList.isEmpty()) {
            mList.setAdapter(new DoctorAdapter(doctorList, new DoctorAdapter.CallbackProvider() {
                @Override
                public void onProviderSelected(ClinicAndDoctor clinicAndDoctor) {
                    Intent intent = new Intent(getContext(), DoctorProfileActivity.class);
                    intent.putExtra(DoctorProfileActivity.ARG_DOCTOR, clinicAndDoctor);
                    startActivityForResult(intent, DoctorProfileActivity.REQUEST_CODE);
                }
            }));
        } else
            mList.setAdapter(new EmptyAdapter(getString(R.string.empty_favorite_doctors)));
    }

    @Override
    public void showClinicFavorite(List<Clinic> clinicList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(linearLayoutManager);
        if (clinicList != null && !clinicList.isEmpty()) {
            mList.setAdapter(new ClinicAdapter(clinicList, new ClinicAdapter.CallbackProvider() {
                @Override
                public void onProviderSelected(ClinicAndDoctor clinicAndDoctor) {
                    Intent intent = new Intent(getContext(), ClinicProfileActivity.class);
                    intent.putExtra(ClinicProfileActivity.ARG_CLINIC, clinicAndDoctor);
                    startActivityForResult(intent, ClinicProfileActivity.REQUEST_CODE);
                }
            }));
        } else
            mList.setAdapter(new EmptyAdapter(getString(R.string.empty_favorite_clinics)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DoctorProfileActivity.REQUEST_CODE) {
            if (data != null) {
                boolean mustRefresh = data.getBooleanExtra("UPDATE", false);
                if (mustRefresh) {
                    boolean isDoctor = getArguments().getBoolean(ARG_IS_DOCTOR, true);
                    if (isDoctor)
                        mPresenter.getDoctorFavorite();
                    else
                        mPresenter.getClinicFavorite();
                }
            }
        }
    }
}
