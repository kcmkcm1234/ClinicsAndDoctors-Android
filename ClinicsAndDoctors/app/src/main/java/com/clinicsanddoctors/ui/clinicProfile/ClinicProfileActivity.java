package com.clinicsanddoctors.ui.clinicProfile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.main.list.ListFragment;
import com.clinicsanddoctors.ui.rate.RateActivity;
import com.clinicsanddoctors.ui.start.StartActivity;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Daro on 13/10/2017.
 */

public class ClinicProfileActivity extends BaseClinicActivity implements ClinicProfileContract.View {

    public static final String ARG_CLINIC = "ARG_CLINIC";
    public static final int REQUEST_CODE = 10;
    private Clinic mClinic;
    private List<Category> categoryList;
    private ClinicProfilePresenter mPresenter;

    private TextView mInfoClinic;
    private ImageView mPhotoClinic;
    private boolean mMustRefresh = false;
    private AppCompatRatingBar mRate;
    private Menu mMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile);
        mClinic = getIntent().getParcelableExtra(ARG_CLINIC);
        mPresenter = new ClinicProfilePresenter(this, this);
        setupToolbar(getString(R.string.title_detail));

        mInfoClinic = (TextView) findViewById(R.id.mInfoClinic);
        mPhotoClinic = (ImageView) findViewById(R.id.mPhotoClinic);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);

        findViewById(R.id.mPhone).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mClinic.getPhoneNumber()));
            startActivity(intent);
        });

        findViewById(R.id.mHowToArrive).setOnClickListener(v -> {
            if (mClinic.getLatitude() != 0 && mClinic.getLongitude() != 0) {
                String latitude = "" + mClinic.getLatitude();
                String longitude = "" + mClinic.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                startActivity(intent);
            }
        });

        mRate.setRating(Float.parseFloat(mClinic.getRating()));
        if (mClinic.getPicture() != null && !mClinic.getPicture().isEmpty())
            Glide.with(this).load(mClinic.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhotoClinic);
        else
            mPhotoClinic.setImageResource(R.drawable.placeholder_clinic);

        String sCategory = "-";
        if (mClinic.getCategory() != null)
            sCategory = mClinic.getCategory().getName();

        if (sCategory.equalsIgnoreCase("all")) {
            sCategory = "-";
            if (mClinic.getCategoryList() != null && !mClinic.getCategoryList().isEmpty())
                sCategory = mClinic.getCategoryList().get(0).getName();
        }
        mInfoClinic.setText(setSpanBoldAndLight(mClinic.getName(), sCategory, mClinic.getAddress()));
        mPresenter.getCategory();
    }

    private SpannableStringBuilder setSpanBoldAndLight(String sClinicName, String sCategory, String sAddress) {

        if (sClinicName == null || sClinicName.isEmpty()) sClinicName = "-";
        if (sCategory == null || sCategory.isEmpty()) sCategory = "-";
        if (sAddress == null || sAddress.isEmpty()) sAddress = "-";

        CalligraphyTypefaceSpan typefaceName = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getResources().getAssets(), getString(R.string.font_compact_avenir_bold)));

        CalligraphyTypefaceSpan typefaceCategory = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getResources().getAssets(), getString(R.string.font_compact_avenir_medium)));

        CalligraphyTypefaceSpan typefaceAddress = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getResources().getAssets(), getString(R.string.font_compact_avenir_medium)));

        String formatInfo = sClinicName + "\n" + sCategory + "\n" + sAddress;
        SpannableStringBuilder spannableInfo = new SpannableStringBuilder(formatInfo);
        spannableInfo.setSpan(typefaceName, formatInfo.indexOf(sClinicName), formatInfo.indexOf(sClinicName) + sClinicName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(typefaceCategory, formatInfo.indexOf(sCategory), formatInfo.indexOf(sCategory) + sCategory.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableInfo.setSpan(typefaceAddress, formatInfo.indexOf(sAddress), formatInfo.indexOf(sAddress) + sAddress.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableInfo.setSpan(new RelativeSizeSpan(1.5f), formatInfo.indexOf(sClinicName), formatInfo.indexOf(sClinicName) + sClinicName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
        spannableInfo.setSpan(new RelativeSizeSpan(1.3f), formatInfo.indexOf(sCategory), formatInfo.indexOf(sCategory) + sCategory.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
        spannableInfo.setSpan(new RelativeSizeSpan(1.0f), formatInfo.indexOf(sAddress), formatInfo.indexOf(sAddress) + sAddress.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size

        return spannableInfo;
    }

    @Override
    public void showCategory(List<Category> categories) {
        categoryList = categories;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFragmentContainer, new ListFragment(null, null, null, false, mClinic))
                .commit();
    }

    @Override
    public void onSuccessAdd() {
        mMenu.getItem(1).setTitle(getString(R.string.menu_remove_favorite));
        mMustRefresh = true;
        Toast.makeText(this, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessRemove() {
        mMenu.getItem(1).setTitle(getString(R.string.menu_add_favorite));
        mMustRefresh = true;
        Toast.makeText(this, getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT).show();
    }

    public List<Category> getCategories() {
        return categoryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rate_clinic, menu);
        mMenu = menu;

        if (mClinic.isRated())
            mMenu.getItem(0).setVisible(false);
        else
            mMenu.getItem(0).setVisible(true);

        if (mClinic.isFavorite())
            mMenu.getItem(1).setTitle(getString(R.string.menu_remove_favorite));
        else
            mMenu.getItem(1).setTitle(getString(R.string.menu_add_favorite));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.mRate) {
            UserClient userClient = AppPreference.getUser(this);
            if (userClient != null) {
                Intent intent = new Intent(this, RateActivity.class);
                intent.putExtra(RateActivity.ARG_CLINIC_DOCTOR, mClinic);
                startActivityForResult(intent, RateActivity.REQUEST_CODE);
            } else
                startActivity(new Intent(this, StartActivity.class));

            return true;
        }

        if (id == R.id.mAddFavorite) {
            if (mMenu.getItem(1).getTitle().toString().equalsIgnoreCase(getString(R.string.menu_add_favorite)))
                mPresenter.addToFavorite(mClinic);
            else
                mPresenter.removeFromFavorite(mClinic);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMustRefresh) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("UPDATE", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RateActivity.REQUEST_CODE) {
            if (data != null) {
                boolean mustRefresh = data.getBooleanExtra("UPDATE", false);
                if (mustRefresh) {
                    mMustRefresh = true;
                }
            }
        }
    }
}
