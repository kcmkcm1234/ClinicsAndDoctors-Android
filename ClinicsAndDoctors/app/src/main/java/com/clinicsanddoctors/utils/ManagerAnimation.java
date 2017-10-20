package com.clinicsanddoctors.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Daro on 12/10/2017.
 */

public class ManagerAnimation {

    private static final int DURATION = 500;

    public static void translateFromRight(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(1500, 0, 0, 0);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void translateFromLeft(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(-1500, 0, 0, 0);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void translateToLeft(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(0, -1500, 0, 0);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void translateToRight(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(0, 1500, 0, 0);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void translateToTop(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(0, 0, 1000, 0);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void translateToBottom(final View view, CallbackAnimation callbackAnimation) {
        Animation animation = new TranslateAnimation(0, 0, 0, 1000);
        animation.setDuration(DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callbackAnimation != null)
                    callbackAnimation.onFinish(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public interface CallbackAnimation {
        void onFinish(View view);
    }

}
