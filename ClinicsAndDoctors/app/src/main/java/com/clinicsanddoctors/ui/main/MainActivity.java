package com.clinicsanddoctors.ui.main;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.favorites.FavoriteFragment;
import com.clinicsanddoctors.ui.main.list.ListFragment;
import com.clinicsanddoctors.ui.main.map.MapFragment;
import com.clinicsanddoctors.ui.privacyPolicy.PrivacyPolicyFragment;
import com.clinicsanddoctors.ui.profile.ProfileFragment;
import com.clinicsanddoctors.ui.start.StartActivity;
import com.clinicsanddoctors.ui.termsOfUse.TermsOfUseFragment;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseClinicActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private Toolbar mToolbar;
    private int currentSection;
    private NavigationView navigationView;
    private Menu menu;
    private SweetAlertDialog mSweetAlertDialog;
    private MainPresenter mainPresenter;

    private MapFragment mapFragment;
    private boolean showAdvertising = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainPresenter = new MainPresenter(this, this);
        setContentView(R.layout.activity_main);
        setupToolbar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFragmentContainer, mapFragment = new MapFragment())
                .commit();
        currentSection = R.id.mNavHome;
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.mTitle);
        ImageView mSlogan = (ImageView) mToolbar.findViewById(R.id.mSlogan);
        mTitle.setText(getString(R.string.app_name));
        mTitle.setVisibility(View.GONE);
        mSlogan.setVisibility(View.VISIBLE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                */

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //must check if there is an update
                hideSoftKeyboard();
                if (getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer) instanceof ProfileFragment) {
                    if (((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer)).mustUpdate()) {
                        View headerView = navigationView.getHeaderView(0);
                        setUserData((ImageView) headerView.findViewById(R.id.mPhoto),
                                (TextView) headerView.findViewById(R.id.mHelloUser));
                    }
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.findViewById(R.id.mLogout).setOnClickListener(v -> mainPresenter.logout());
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(v -> goToProfile());
        setUserData((ImageView) headerView.findViewById(R.id.mPhoto), (TextView) headerView.findViewById(R.id.mHelloUser));


        UserClient userClient = AppPreference.getUser(this);
        if (userClient != null) {
            navigationView.findViewById(R.id.mLogout).setVisibility(View.VISIBLE);
            headerView.findViewById(R.id.mSeeProfile).setVisibility(View.VISIBLE);
        } else {
            navigationView.findViewById(R.id.mLogout).setVisibility(View.GONE);
            headerView.findViewById(R.id.mSeeProfile).setVisibility(View.GONE);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void goToProfile() {

        UserClient userClient = AppPreference.getUser(this);
        if (userClient != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            if (currentSection == 0) return;

            this.menu.getItem(0).setVisible(false); //map mode
            this.menu.getItem(1).setVisible(false); //list mode
            this.menu.getItem(2).setVisible(true); //edit
            this.menu.getItem(3).setVisible(false); //add request

            changeFragmentSection(getString(R.string.menu_title_profile), new ProfileFragment());
            currentSection = 0;
        } else {
            startActivity(new Intent(this, StartActivity.class));
        }

    }

    private void setUserData(ImageView imageView, TextView textView) {
        UserClient userClient = AppPreference.getUser(this);
        if (userClient != null) {
            Glide.with(this)
                    .load(userClient.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.placeholder_clinic)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(imageView);
            textView.setText(userClient.getCompleteName());
        } else
            textView.setText(getString(R.string.login_register));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer) instanceof ListFragment) {
                this.menu.getItem(0).setVisible(false);
                this.menu.getItem(1).setVisible(true);
                super.onBackPressed();
            } else {
                Intent backtoHome = new Intent(Intent.ACTION_MAIN);
                backtoHome.addCategory(Intent.CATEGORY_HOME);
                backtoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(backtoHome);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_mode_list) {
            showListMode(false);
            return true;
        }

        if (id == R.id.ic_mode_map) {
            this.menu.getItem(0).setVisible(false);
            this.menu.getItem(1).setVisible(true);
            onBackPressed();
            return true;
        }

        if (id == R.id.ic_edit_profile) {
            this.menu.getItem(2).setVisible(false);
            ((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer)).showEditView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showListMode(boolean fromCluster) {

        this.menu.getItem(0).setVisible(true);
        this.menu.getItem(1).setVisible(false);

        Location location = mapFragment.getmLocationMap();
        Category category = mapFragment.getmCurrentCategory();

        mapFragment.clearCluster();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_left_out,
                        R.anim.card_flip_right_in, R.anim.card_flip_left_out)
                .replace(R.id.mFragmentContainer, new ListFragment(mapFragment.getmClinicList(), location, category, fromCluster, null))
                .addToBackStack(null)
                .commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mNavHome:
                if (id == currentSection) break;
                this.menu.getItem(0).setVisible(false);
                this.menu.getItem(1).setVisible(true);
                this.menu.getItem(2).setVisible(false); //edit
                this.menu.getItem(3).setVisible(false); //add request
                changeFragmentSection(getString(R.string.app_name), mapFragment = new MapFragment());
                break;

            case R.id.mNavFavorite:
                UserClient userClient = AppPreference.getUser(this);
                if (userClient != null) {
                    if (id == currentSection) break;
                    this.menu.getItem(0).setVisible(false); //map mode
                    this.menu.getItem(1).setVisible(false); //list mode
                    this.menu.getItem(2).setVisible(false); //edit
                    this.menu.getItem(3).setVisible(false); //add request
                    changeFragmentSection(getString(R.string.menu_title_favorite), new FavoriteFragment());
                } else
                    startActivity(new Intent(this, StartActivity.class));

                break;
            case R.id.mNavTerms:
                this.menu.getItem(0).setVisible(false); //map mode
                this.menu.getItem(1).setVisible(false); //list mode
                this.menu.getItem(2).setVisible(false); //edit
                this.menu.getItem(3).setVisible(false); //add request
                if (id == currentSection) break;
                changeFragmentSection(getString(R.string.menu_title_terms_use), new TermsOfUseFragment());
                break;
            case R.id.mNavPrivacyPolicy:
                if (id == currentSection) break;
                this.menu.getItem(0).setVisible(false); //map mode
                this.menu.getItem(1).setVisible(false); //list mode
                this.menu.getItem(2).setVisible(false); //edit
                this.menu.getItem(3).setVisible(false); //add request
                changeFragmentSection(getString(R.string.menu_title_privacy_policy), new PrivacyPolicyFragment());
                break;

        }
        currentSection = id;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragmentSection(String title, Fragment fragment) {
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.mTitle);
        ImageView mSlogan = (ImageView) mToolbar.findViewById(R.id.mSlogan);
        mTitle.setText(title);
        if (title.equalsIgnoreCase(getString(R.string.app_name))) {
            mTitle.setVisibility(View.GONE);
            mSlogan.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mSlogan.setVisibility(View.GONE);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFragmentContainer, fragment)
                .commit();
    }

    public void getCategories() {
        mainPresenter.getCategory();
    }

    @Override
    public void showCategory(List<Category> categories) {
        if (getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer) instanceof ListFragment) {
            ((ListFragment) getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer)).showCategory(categories);
        } else {
            ((MapFragment) getSupportFragmentManager().findFragmentById(R.id.mFragmentContainer)).showCategory(categories);
        }
    }


    @Override
    public void onSuccessLogout() {
        AppPreference.deleteUserData(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
