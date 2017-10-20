package com.clinicsanddoctors.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.AdvertisingPopOver;

/**
 * Created by Daro on 10/08/2017.
 */

public class AdvertisingDialog extends Dialog {

    private Context mContext;
    private AdvertisingPopOver mAdvertisingPopOver;
    private ImageView mPhotoAdd;

    public AdvertisingDialog(@NonNull Context context, AdvertisingPopOver advertisingPopOver) {
        super(context);
        this.mContext = context;
        this.mAdvertisingPopOver = advertisingPopOver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_advertising);
        mPhotoAdd = (ImageView) findViewById(R.id.mPhotoAdd);

        if (mAdvertisingPopOver.getImage() != null && !mAdvertisingPopOver.getImage().isEmpty())
            Glide.with(getContext()).load(mAdvertisingPopOver.getImage()).into(mPhotoAdd);

        mPhotoAdd.setOnClickListener(v -> openWebsite(mAdvertisingPopOver.getLink()));
        findViewById(R.id.mClose).setOnClickListener(v -> dismiss());
    }

    private void openWebsite(String urlLink) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(urlLink));
        mContext.startActivity(i);
    }
}
