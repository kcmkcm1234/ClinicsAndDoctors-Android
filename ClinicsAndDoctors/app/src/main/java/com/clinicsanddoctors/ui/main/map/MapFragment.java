package com.clinicsanddoctors.ui.main.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.dialog.PreviewClinicDialog;
import com.clinicsanddoctors.ui.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;

/**
 * Created by Daro on 27/07/2017.
 */

public class MapFragment extends Fragment implements ClusterManager.OnClusterClickListener<ClinicCluster>,
        GoogleMap.OnMarkerClickListener, MapContract.View {

    private View view;
    private GoogleMap mGoogleMap;
    private ClusterManager<ClinicCluster> mClusterManager;
    private MapPresenter mPresenter;
    private List<Clinic> mClinicList;
    private SweetAlertDialog mSweetAlertDialog;

    private Location mLocationMap = new Location("");
    private Category mCurrentCategory;
    private int positionTab = 0;
    private TabLayout mTabLayout;
    private boolean noReload = false;
    private List<Category> categories;

    private PreviewClinicDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            /*
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
                */
            noReload = true;
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            mPresenter = new MapPresenter(this, getActivity());
            mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
            /*
            if (mCurrentCategory == null)
                mCurrentCategory = new Category().setName("All").setId("0");
                */

            ((MainActivity) getActivity()).getCategories();
            initMap();
        } catch (InflateException e) {
            Log.e("ERROR_MAP", e.getMessage());
        }
        return view;
    }

    private void initMap() {
        SupportMapFragment mMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMap);
        mMap.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;
            mGoogleMap.setOnMarkerClickListener(this);
            mClusterManager = new ClusterManager<>(getContext(), googleMap);
            clearCluster();
            ShopRenderer shopRenderer = new ShopRenderer(getActivity(), mGoogleMap, mClusterManager);
            mClusterManager.setRenderer(shopRenderer);
            mClusterManager.setOnClusterClickListener(this);
            mGoogleMap.setOnCameraIdleListener(() -> {
                Log.i("new_location", "" + mGoogleMap.getCameraPosition().target.latitude + ", " +
                        mGoogleMap.getCameraPosition().target.longitude);
                mLocationMap.setLatitude(mGoogleMap.getCameraPosition().target.latitude);
                mLocationMap.setLongitude(mGoogleMap.getCameraPosition().target.longitude);
                getNearTattooShops(mLocationMap);
                mClusterManager.onCameraIdle();
            });
            setMarginTopMyLocationButton(mMap);

            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            mGoogleMap.setMyLocationEnabled(true);
                            RxLocation rxLocation = new RxLocation(getActivity());
                            rxLocation.location()
                                    .lastLocation()
                                    .subscribe(location -> {
                                        if (noReload) {
                                            clearCluster();
                                            //mPresenter.getClinics(mCurrentCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS, true);
                                            return;
                                        }
                                        ClinicsApplication.getInstance().setCurrentLocation(location);
                                        getNearTattooShops(location);
                                        setCameraPosition(location);
                                    });
                        }
                    });
        });
    }

    private void setMarginTopMyLocationButton(SupportMapFragment mMap) {
        View myLocationButton = mMap.getView().findViewById(0x2);
        if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {

            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            final int marginRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics());
            final int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                    getResources().getDisplayMetrics());
            rlp.setMargins(0, 0, marginRight, marginBottom);
        }
    }

    private void getNearTattooShops(Location location) {
        if (mCurrentCategory != null)
            mPresenter.getClinics(mCurrentCategory, location, ClinicServices.RadiusSearch.RADIUS, false);
    }

    private void setCameraPosition(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(12)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }

    @Override
    public boolean onClusterClick(Cluster<ClinicCluster> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        try {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (allInSamePlace(cluster) || (mGoogleMap.getCameraPosition().zoom >= 19 && allInSamePlace(cluster))
                || (mGoogleMap.getCameraPosition().zoom >= 21 && cluster.getItems().size() >= 2)) {
            mClinicList = new ArrayList<>();
            for (ClinicCluster item : cluster.getItems())
                mClinicList.add(item.getClinic());
            ((MainActivity) getActivity()).showListMode(true);
        }
        return true;
    }

    public void clearCluster() {
        mClusterManager.clearItems();
        mClusterManager.addItems(new ArrayList<>());
        mClusterManager.cluster();
    }

    private boolean allInSamePlace(Cluster<ClinicCluster> cluster) {
        if (cluster.getItems().size() >= 2) {
            LatLng latLng = null;
            for (ClusterItem item : cluster.getItems()) {
                if (latLng == null)
                    latLng = item.getPosition();
                else {
                    if (latLng.latitude != item.getPosition().latitude ||
                            latLng.longitude != item.getPosition().longitude)
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void showClinics(List<Clinic> clinicAndDoctors) {

        if (!isAdded()) return;

        this.mClinicList = clinicAndDoctors;
        //clearCluster();
        View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_clinics, null);
        mPresenter.addSubscription(Observable.just(clinicAndDoctors)
                .flatMapIterable(tattooShop -> tattooShop)
                .flatMap(clinic ->
                        mPresenter.getCustomMarker(view, clinic)
                                .map(bitmap -> new ClinicCluster(bitmap, clinic)))
                .toList()
                .subscribe(handyman -> {
                    mClusterManager.clearItems();
                    mClusterManager.addItems(handyman);
                    mClusterManager.cluster();
                }, this::onErrorShopMarker)
        );
    }

    private void onErrorShopMarker(Throwable throwable) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mClusterManager.onMarkerClick(marker);

        if (marker.getTag() != null) {
            dialog = new PreviewClinicDialog(getActivity(), new PreviewClinicDialog.CallbackDialog() {
                @Override
                public void onGo(Dialog dialog, ClinicAndDoctor clinicAndDoctor) {
                    String latitude = "" + clinicAndDoctor.getLatitude();
                    String longitude = "" + clinicAndDoctor.getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                    startActivity(intent);
                }

                @Override
                public void onInfo(Dialog dialog, ClinicAndDoctor clinicAndDoctor) {

                    Intent intent = new Intent(getContext(), ClinicProfileActivity.class);
                    intent.putExtra(ClinicProfileActivity.ARG_CLINIC, clinicAndDoctor);
                    startActivity(intent);
                    //dialog.dismiss();
                }

                @Override
                public void onHowToArrive(Dialog dialog, ClinicAndDoctor clinicAndDoctor) {
                    String latitude = "" + clinicAndDoctor.getLatitude();
                    String longitude = "" + clinicAndDoctor.getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                    startActivity(intent);
                }
            });
            dialog.setClinicAndDoctor((ClinicAndDoctor) marker.getTag());
            dialog.show();
        }

        return true;
    }

    public boolean isPreviewShowing() {
        if (dialog != null && dialog.isShowing())
            return true;
        return false;
    }

    public List<Clinic> getmClinicList() {
        return mClinicList != null ? mClinicList : new ArrayList<>();
    }

    @Override
    public void showProgressDialog() {
        mSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.app_name));
        mSweetAlertDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing())
            mSweetAlertDialog.hide();
    }

    @Override
    public void showErrorAlert(String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }

    public void showCategory(List<Category> categories) {
        if (!isAdded()) return;

        if (categories != null && !categories.isEmpty()) {
            this.categories = categories;
            for (Category category : categories)
                mTabLayout.addTab(mTabLayout.newTab().setText(category.getName()));

            TabLayout.Tab tab = mTabLayout.getTabAt(positionTab);
            tab.select();
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    positionTab = tab.getPosition();
                    mCurrentCategory = categories.get(positionTab);
                    mPresenter.getClinics(mCurrentCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS, true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            mCurrentCategory = categories.get(0);
            mPresenter.getClinics(mCurrentCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS, true);
        }
    }

    public Location getmLocationMap() {
        return mLocationMap;
    }

    public Category getmCurrentCategory() {
        return mCurrentCategory;
    }
}
