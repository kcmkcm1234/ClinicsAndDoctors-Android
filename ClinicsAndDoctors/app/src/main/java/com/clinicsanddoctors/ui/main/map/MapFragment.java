package com.clinicsanddoctors.ui.main.map;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.SearchAdapter;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.clinicsanddoctors.ui.BaseClinicFragment;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.dialog.PreviewClinicDialog;
import com.clinicsanddoctors.ui.doctorProfile.DoctorProfileActivity;
import com.clinicsanddoctors.ui.main.MainActivity;
import com.google.android.gms.location.LocationRequest;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Daro on 27/07/2017.
 */

public class MapFragment extends BaseClinicFragment implements ClusterManager.OnClusterClickListener<ClinicCluster>,
        GoogleMap.OnMarkerClickListener, MapContract.View, SearchView.OnSuggestionListener {

    private View view;
    private GoogleMap mGoogleMap;
    private ClusterManager<ClinicCluster> mClusterManager;
    private MapPresenter mPresenter;
    private List<Clinic> mClinicList;

    private Location mLocationMap = new Location("");
    private Category mCurrentCategory;
    private int positionTab = 0;
    private TabLayout mTabLayout;
    private boolean noReload = false;
    private List<Category> categories;
    private Toolbar mToolbar;
    private ImageView mSearch;

    private List<ClinicAndDoctorResponse> mSearchResults;
    private SearchAdapter mSearchAdapter;

    Menu search_menu;
    MenuItem item_search;

    LocationRequest locationRequest;
    RxLocation rxLocation;
    private final CompositeDisposable disposable = new CompositeDisposable();


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
            mToolbar = (Toolbar) view.findViewById(R.id.searchtoolbar);
            mSearch = (ImageView) view.findViewById(R.id.search);
            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar, 1, true, true);
                    else
                        mToolbar.setVisibility(View.VISIBLE);

                    item_search.expandActionView();
                }
            });
            mSearchResults = new ArrayList<>();
            mSearchAdapter = new SearchAdapter(getActivity(), mSearchResults);
            setSearchtollbar();

            if (mCurrentCategory == null)
                mCurrentCategory = new Category().setName("All").setId("0");


            ((MainActivity) getActivity()).getCategories();
            initMap();
        } catch (InflateException e) {
            Log.e("ERROR_MAP", e.getMessage());
        }
        return view;
    }

    public void setSearchtollbar() {
//        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);
        if (mToolbar != null) {
            mToolbar.inflateMenu(R.menu.menu_search);
            search_menu = mToolbar.getMenu();

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    else
                        mToolbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    } else
                        mToolbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    public void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        searchView.setSuggestionsAdapter(mSearchAdapter);
        searchView.setOnSuggestionListener(this);

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_search);


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search clinics or doctors...");
        txtSearch.setHintTextColor(getResources().getColor(R.color.white));
        txtSearch.setTextColor(getResources().getColor(R.color.white));
//        txtSearch.setHintTextColor(Color.DKGRAY);
//        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    return false;
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                Log.i("query", "" + query);
                mSearchResults.clear();
                mPresenter.search(query);
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = mToolbar;//findViewById(viewID);

        int width = myView.getWidth();

//        if(posFromRight>0)
//            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
//        if(containsOverflow)
//            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();
    }

    private void initMap() {
        SupportMapFragment mMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMap);
        mMap.getMapAsync(googleMap -> {
//            locationRequest = LocationRequest.create()
//                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                    .setNumUpdates(1);
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

                new Handler().postDelayed(() -> {
                    if (mGoogleMap.getCameraPosition().target.latitude != 0 && mGoogleMap.getCameraPosition().target.longitude != 0) {
                        mLocationMap.setLatitude(mGoogleMap.getCameraPosition().target.latitude);
                        mLocationMap.setLongitude(mGoogleMap.getCameraPosition().target.longitude);
                        getNearTattooShops(mLocationMap);
                    }
                }, 100);
                mClusterManager.onCameraIdle();
            });
            setMarginTopMyLocationButton(mMap);

            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            rxLocation = new RxLocation(getActivity());
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            rxLocation.location()
                                    .lastLocation()
                                    .subscribe(location -> {
                                        if (noReload) {
                                            clearCluster();
                                            //mPresenter.getClinics(mCurrentCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS, true);
                                            return;
                                        }
                                        mGoogleMap.setMyLocationEnabled(true);
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

    @Override
    public void loadResults(List<ClinicAndDoctorResponse> clinicAndDoctorResponses) {
        mSearchResults = clinicAndDoctorResponses;
        mSearchAdapter.swapCursor(SearchAdapter.cursorFrom(mSearchResults));
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
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clinicAndDoctor.getPhoneNumber()));
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

    public void showCategory(List<Category> categories) {
        if (!isAdded()) return;

        if (categories != null && !categories.isEmpty()) {
            this.categories = categories;
            for (Category category : categories) {
                if (category != null)
                    mTabLayout.addTab(mTabLayout.newTab().setText(category.getName()));
            }
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

            //mCurrentCategory = categories.get(0);
            mPresenter.getClinics(mCurrentCategory, mLocationMap, ClinicServices.RadiusSearch.RADIUS, true);
        }
    }

    public Location getmLocationMap() {
        return mLocationMap;
    }

    public Category getmCurrentCategory() {
        return mCurrentCategory;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Clinic clinic;
        Doctor doctor;
        mToolbar.setVisibility(View.GONE);
        ClinicAndDoctorResponse clinicAndDoctorResponse = mSearchResults.get(position);
        Intent intent;
        if (clinicAndDoctorResponse.getClinic() != null) {
            doctor = new Doctor(clinicAndDoctorResponse, clinicAndDoctorResponse.getCategory());
            intent = new Intent(getContext(), DoctorProfileActivity.class);
            intent.putExtra(DoctorProfileActivity.ARG_DOCTOR, (ClinicAndDoctor) doctor);
        } else {
            clinic = new Clinic(clinicAndDoctorResponse, new Category().setName("All").setId("0"));
            intent = new Intent(getContext(), ClinicProfileActivity.class);
            intent.putExtra(ClinicProfileActivity.ARG_CLINIC, (ClinicAndDoctor) clinic);
        }

        startActivity(intent);
        return true;
    }
}
