package com.clinicsanddoctors.ui.start;

import android.app.Activity;
import android.content.Intent;

import com.clinicsanddoctors.contracts.LoaderView;

/**
 * Created by Daro on 07/07/2017.
 */

public interface StartContract {

    interface View extends LoaderView {
        void onFacebookConnected();
    }

    interface Presenter {

        void attendOnClickFacebook(Activity activity);

        void attendFacebookResult(int requestCode, int resultCode, Intent data);

    }
}
