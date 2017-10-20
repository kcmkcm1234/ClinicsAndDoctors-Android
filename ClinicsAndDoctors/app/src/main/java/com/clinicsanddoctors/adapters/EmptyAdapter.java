package com.clinicsanddoctors.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinicsanddoctors.R;


/**
 * Created by Daro on 28/07/2017.
 */

public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.ViewHolder> {

    private String mLabelEmpty;

    public EmptyAdapter(String labelEmpty) {
        this.mLabelEmpty = labelEmpty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mLabelEmpty, position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T data, int position);
    }

    private class EmptyViewHolder extends EmptyAdapter.ViewHolder<String> {

        private TextView mEmptyLabel;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mEmptyLabel = (TextView) itemView.findViewById(R.id.mEmptyLabel);
        }

        @Override
        public void bindItem(String data, int position) {
            mEmptyLabel.setText(data);
        }
    }
}
