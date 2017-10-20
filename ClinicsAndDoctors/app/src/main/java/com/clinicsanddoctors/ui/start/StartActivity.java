package com.clinicsanddoctors.ui.start;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.forgetPassword.ForgetPasswordActivity;
import com.clinicsanddoctors.ui.main.MainActivity;
import com.clinicsanddoctors.ui.signIn.SignInActivity;
import com.clinicsanddoctors.ui.signUp.SignUpActivity;

/**
 * Created by Daro on 30/08/2017.
 */

public class StartActivity extends BaseClinicActivity implements StartContract.View {

    private StartPresenter mPresenter;
    private TextView mHaveAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mPresenter = new StartPresenter(this, this);
        mHaveAccount = (TextView) findViewById(R.id.mHaveAccount);
        //getFirebaseToken();

        //findViewById(R.id.mAccessFacebook).setOnClickListener(v -> mPresenter.attendOnClickFacebook(this));
        findViewById(R.id.mAccessFacebook).setOnClickListener(v -> onFacebookConnected());
        findViewById(R.id.mSignIn).setOnClickListener(v -> openSigIn());
        findViewById(R.id.mForgotPassword).setOnClickListener(v -> forgotPassword());

        spannableOnClick(getString(R.string.not_account), "Register Here", mHaveAccount, registerSpan);
    }

    private void spannableOnClick(String sUnformatted, String sClickable, TextView textView, ClickableSpan clickableSpan) {
        SpannableString spannableString = new SpannableString(sUnformatted);
        int sizeFirstSpan = sUnformatted.indexOf(sClickable);
        spannableString.setSpan(clickableSpan, sizeFirstSpan, sUnformatted.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getResources().getAssets(), getString(R.string.font_compact_firasans_bold)));
        //spannableString.setSpan(typefaceSpan, sizeFirstSpan, formatted.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    ClickableSpan registerSpan = new ClickableSpan() {
        @Override
        public void onClick(View textView) {
            textView.invalidate();
            openSigUp();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.WHITE);
            ds.setUnderlineText(true);
            ds.setFakeBoldText(true);
        }
    };

    private void forgotPassword() {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }

    private void openSigIn() {
        startActivity(new Intent(StartActivity.this, SignInActivity.class));
    }

    private void openSigUp() {
        startActivity(new Intent(StartActivity.this, SignUpActivity.class));
    }

    @Override
    public void onFacebookConnected() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.attendFacebookResult(requestCode, resultCode, data);
    }
}
