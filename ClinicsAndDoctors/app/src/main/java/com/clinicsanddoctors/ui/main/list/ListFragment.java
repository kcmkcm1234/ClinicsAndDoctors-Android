package com.clinicsanddoctors.ui.main.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.CategoryAdapter;
import com.clinicsanddoctors.adapters.SearchAdapter;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.clinicsanddoctors.ui.BaseClinicFragment;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.doctorProfile.DoctorProfileActivity;
import com.clinicsanddoctors.ui.main.MainActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 28/07/2017.
 */

public class ListFragment extends BaseClinicFragment implements ListContract.View, SearchView.OnSuggestionListener {

    private View view;
    private List<Clinic> mClinicCluster;
    private ListPresenter mPresenter;
    private ViewPager mViewPager;
    private ImageView mAdBanner;
    private TabLayout mTabLayout;

    private Location mLocationMap = new Location("");
    private Category mCurrentCategory;
    private boolean isFromCluster;
    private Clinic mClinic;

    private Toolbar mToolbar;
    private ImageView mSearch;

    private List<ClinicAndDoctorResponse> mSearchResults;
    private SearchAdapter mSearchAdapter;

    Menu search_menu;
    MenuItem item_search;

    public ListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListFragment(List<Clinic> clinicCluster, Location location, Category category, boolean isFromCluster, Clinic clinic) {
        mClinicCluster = clinicCluster;
        mLocationMap = location;
        mClinic = clinic;
        mCurrentCategory = category;
        this.isFromCluster = isFromCluster;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        mPresenter = new ListPresenter(this, getContext());

        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        mAdBanner = (ImageView) view.findViewById(R.id.mAdBanner);
        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);

        mToolbar = (Toolbar) view.findViewById(R.id.searchtoolbar);
        mSearch = (ImageView) view.findViewById(R.id.search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar,1,true,true);
                else
                    mToolbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
            }
        });
        mSearchResults = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(getActivity(), mSearchResults);
        setSearchtollbar();

        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).getCategories();
        else {
            showCategory(((ClinicProfileActivity) getActivity()).getCategories());
        }
        return view;
    }

    public void setSearchtollbar()
    {
//        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);
        if (mToolbar != null) {
            mToolbar.inflateMenu(R.menu.menu_search);
            search_menu=mToolbar.getMenu();

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar,1,true,false);
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
                        circleReveal(R.id.searchtoolbar,1,true,false);
                    }
                    else
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

    public void initSearchView()
    {
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
                if(newText.isEmpty())
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
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = mToolbar;//findViewById(viewID);

        int width=myView.getWidth();

//        if(posFromRight>0)
//            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
//        if(containsOverflow)
//            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();
    }

    @Override
    public void showCategory(List<Category> categories) {

        if (categories != null && !categories.isEmpty()) {
            int position = 0;

            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (mCurrentCategory != null && category.getId().equalsIgnoreCase(mCurrentCategory.getId()))
                    position = i;
                mTabLayout.addTab(mTabLayout.newTab().setText(category.getName()));
            }

            mViewPager.setAdapter(new CategoryAdapter(isFromCluster, getContext(), getFragmentManager(),
                    categories, mLocationMap, mClinicCluster,
                    mCurrentCategory, mClinic));
            mViewPager.setOffscreenPageLimit(categories.size());
            mViewPager.setCurrentItem(position, false);
            mTabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                TextView tabSimple = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_tab, null);
                tabSimple.setText(category.getName());
                mTabLayout.getTabAt(i).setCustomView(tabSimple);
            }
        }
    }

    @Override
    public void loadResults(List<ClinicAndDoctorResponse> clinicAndDoctorResponses) {
        mSearchResults = clinicAndDoctorResponses;
        mSearchAdapter.swapCursor(SearchAdapter.cursorFrom(mSearchResults));
    }

    private void openWebsite(String urlLink) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(urlLink));
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelTimer();
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Clinic clinic;
        Doctor doctor;
        ClinicAndDoctorResponse clinicAndDoctorResponse =  mSearchResults.get(position);
        Intent intent;
        if(clinicAndDoctorResponse.getClinic()!=null) {
            doctor = new Doctor(clinicAndDoctorResponse, new Category().setName("All").setId("0"));
            intent = new Intent(getContext(), DoctorProfileActivity.class);
            intent.putExtra(DoctorProfileActivity.ARG_DOCTOR, (ClinicAndDoctor)doctor);
        }
        else {
            clinic = new Clinic(clinicAndDoctorResponse, new Category().setName("All").setId("0"));
            intent = new Intent(getContext(), ClinicProfileActivity.class);
            intent.putExtra(ClinicProfileActivity.ARG_CLINIC, (ClinicAndDoctor)clinic);
        }

        startActivity(intent);
        return true;
    }
}
