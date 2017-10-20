package com.clinicsanddoctors.ui.clinicProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.main.list.ListFragment;
import com.clinicsanddoctors.ui.rate.RateActivity;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Daro on 13/10/2017.
 */

public class ClinicProfileActivity extends BaseClinicActivity implements ClinicProfileContract.View {

    public static final String ARG_CLINIC = "ARG_CLINIC";
    private Clinic mClinic;
    private List<Category> categoryList;
    private ClinicProfilePresenter mPresenter;

    private TextView mInfoClinic;
    private ImageView mPhotoClinic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile);
        mClinic = getIntent().getParcelableExtra(ARG_CLINIC);
        mPresenter = new ClinicProfilePresenter(this, this);
        setupToolbar(getString(R.string.title_detail));

        mInfoClinic = (TextView) findViewById(R.id.mInfoClinic);
        mPhotoClinic = (ImageView) findViewById(R.id.mPhotoClinic);

        if (mClinic.getPicture() != null && !mClinic.getPicture().isEmpty())
            Glide.with(this).load(mClinic.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhotoClinic);
        else
            mPhotoClinic.setImageResource(R.drawable.placeholder_clinic);

        String sCategory = "-";
        if (mClinic.getCategory() != null) sCategory = mClinic.getCategory().getName();
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

    public List<Category> getCategories() {
        return categoryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rate_clinic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.mRate) {
            Intent intent = new Intent(this, RateActivity.class);
            intent.putExtra(RateActivity.ARG_CLINIC_DOCTOR, mClinic);
            startActivity(new Intent(intent));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
