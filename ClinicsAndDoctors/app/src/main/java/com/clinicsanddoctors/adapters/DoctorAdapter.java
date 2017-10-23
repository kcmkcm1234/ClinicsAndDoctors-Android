package com.clinicsanddoctors.adapters;

import android.location.Location;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;

import java.util.List;
import java.util.Locale;

/**
 * Created by Daro on 28/07/2017.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<Doctor> mClinicAndDoctors;
    private CallbackProvider mCallbackProvider;

    public DoctorAdapter(List<Doctor> clinicAndDoctors, CallbackProvider callbackProvider) {
        this.mClinicAndDoctors = clinicAndDoctors;
        this.mCallbackProvider = callbackProvider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_provider, parent, false);
        return new ArtistInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mClinicAndDoctors.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mClinicAndDoctors.size();
    }

    public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T data, int position);
    }

    private class ArtistInfoViewHolder extends DoctorAdapter.ViewHolder<Doctor> {

        private View mItemView;
        private ImageView mPhoto;
        private TextView mNameDistance, mDoctorName;
        private AppCompatRatingBar mRate;

        public ArtistInfoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mPhoto = (ImageView) itemView.findViewById(R.id.mPhoto);
            mNameDistance = (TextView) itemView.findViewById(R.id.mNameDistance);
            mDoctorName = (TextView) itemView.findViewById(R.id.mDoctorName);
            mRate = (AppCompatRatingBar) itemView.findViewById(R.id.mRate);
        }

        @Override
        public void bindItem(Doctor data, int position) {

            if (data.getPicture() != null && !data.getPicture().isEmpty())
                Glide.with(mPhoto.getContext()).load(data.getPicture())
                        .dontAnimate().placeholder(R.drawable.placeholder_heart).into(mPhoto);
            else
                mPhoto.setImageResource(R.drawable.placeholder_heart);

            mDoctorName.setText(data.getName());
            mRate.setRating(Float.parseFloat(data.getRating()));
            Location currentLocation = ClinicsApplication.getInstance().getCurrentLocation();
            if (currentLocation != null) {
                Location locationProvider = new Location("");
                locationProvider.setLongitude(data.getClinic().getLongitude());
                locationProvider.setLatitude(data.getClinic().getLatitude());

                String distanceResult;
                String sbDistance = "";

                float distance = currentLocation.distanceTo(locationProvider);
                float meters = Float.parseFloat(String.format(Locale.US, "%.2f", distance));
                float miles = meters * 0.000621371f;
                float feets = meters * 3.28084f;

                if (miles < 0.1) {
                    distanceResult = String.format(Locale.US, "%.2f", feets);
                    sbDistance = distanceResult + " ft Away";
                } else {
                    distanceResult = String.format(Locale.US, "%.2f", miles);
                    sbDistance = distanceResult + " mi Away";
                }

                String info = data.getClinic().getName() + "\n" + sbDistance;
                SpannableStringBuilder spannableInfo = new SpannableStringBuilder(info);
                spannableInfo.setSpan(new ForegroundColorSpan(mNameDistance.getResources().getColor(R.color.gray))
                        , info.indexOf(sbDistance), info.indexOf(sbDistance) + sbDistance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mNameDistance.setText(spannableInfo, TextView.BufferType.SPANNABLE);
            } else {
                String sbDistance = "- mi Away";
                String info = data.getClinic().getName() + "\n" + sbDistance;
                SpannableStringBuilder spannableInfo = new SpannableStringBuilder(info);
                spannableInfo.setSpan(new ForegroundColorSpan(mNameDistance.getResources().getColor(R.color.gray))
                        , info.indexOf(sbDistance), info.indexOf(sbDistance) + sbDistance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mNameDistance.setText(spannableInfo, TextView.BufferType.SPANNABLE);
            }

            mItemView.setOnClickListener(v -> mCallbackProvider.onProviderSelected(data));
        }
    }

    public interface CallbackProvider {
        void onProviderSelected(ClinicAndDoctor clinicAndDoctor);
    }
}
