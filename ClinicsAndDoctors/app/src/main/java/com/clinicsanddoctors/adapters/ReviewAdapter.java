package com.clinicsanddoctors.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;

import java.util.List;

/**
 * Created by Daro on 28/07/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mReviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.mReviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ArtistInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mReviewList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T data, int position);
    }

    private class ArtistInfoViewHolder extends ReviewAdapter.ViewHolder<Review> {

        private View mItemView;
        private ImageView mPhoto;
        private TextView mDescription, mName, mDate;

        public ArtistInfoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mPhoto = (ImageView) itemView.findViewById(R.id.mPhoto);
            mDescription = (TextView) itemView.findViewById(R.id.mDescription);
            mName = (TextView) itemView.findViewById(R.id.mName);
            mDate = (TextView) itemView.findViewById(R.id.mDate);
        }

        @Override
        public void bindItem(Review data, int position) {
            Glide.with(mPhoto.getContext())
                    .load(data.getUserClient().getPicture())
                    .placeholder(R.drawable.placeholder_clinic)
                    .into(mPhoto);
            mDescription.setText(data.getComment());
            mName.setText(data.getUserClient().getFullName());
            mDate.setText(data.getCreatedDateTime());
        }
    }

}
