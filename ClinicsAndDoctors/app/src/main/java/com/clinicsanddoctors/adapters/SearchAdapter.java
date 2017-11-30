package com.clinicsanddoctors.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

import java.util.List;

/**
 * Created by leandro on 10/06/16.
 */
public class SearchAdapter extends CursorAdapter {

    public SearchAdapter(Context context, List<ClinicAndDoctorResponse> items) {
        super(context, cursorFrom(items), false);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView photo = (ImageView) view.findViewById(R.id.mPhoto);
        TextView  title = (TextView) view.findViewById(R.id.mDoctorName);
        TextView subtitle = (TextView) view.findViewById(R.id.mNameDistance);

        String url = cursor.getString(1);
        String name = cursor.getString(2);
        String subtext = cursor.getString(3);

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_clinic)
                .into(photo);

        title.setText(name);
        subtitle.setText(subtext);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public static Cursor cursorFrom(List<ClinicAndDoctorResponse> items) {
        MatrixCursor cursor = new MatrixCursor(new String[]{ BaseColumns._ID, "photo", "name", "subtext" });
        for (int i = 0; i<items.size(); i++) {
            ClinicAndDoctorResponse  p = items.get(i);
            cursor.addRow(new Object[]{ i, p.getPicture(), p.getName(), p.getPhoneNumber() });
        }
        return cursor;
    }
}
