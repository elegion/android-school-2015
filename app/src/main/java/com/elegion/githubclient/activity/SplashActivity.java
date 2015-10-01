package com.elegion.githubclient.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.R;

/**
 * @author Artem Mochalov.
 */
public class SplashActivity extends BaseActivity {

    private static final int ANIMATION_DURATION = 1500;
    private final AnimatorSet mAnimatorSet = new AnimatorSet();
    private ImageView mAppLogoImageView;
    private final Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            attemptEnter();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            beforeStartViewAnimation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        mAppLogoImageView = (ImageView) findViewById(R.id.img_app_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAnimatorSet != null && !mAnimatorSet.isRunning()) {
            startInitialAnimation();
        }
    }

    @Override
    protected void onPause() {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        super.onPause();
    }


    private void startInitialAnimation() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAppLogoImageView, View.ALPHA, 0, 1);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mAppLogoImageView, View.SCALE_X, 0, 1);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mAppLogoImageView, View.SCALE_Y, 0, 1);

        mAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setDuration(ANIMATION_DURATION);
        mAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mAnimatorSet.addListener(mAnimatorListener);
        mAnimatorSet.start();
    }

    private void beforeStartViewAnimation() {
        mAppLogoImageView.setAlpha(0f);
        mAppLogoImageView.setScaleX(0);
        mAppLogoImageView.setScaleY(0);
    }

    private void attemptEnter() {
        if (!TextUtils.isEmpty(AppDelegate.getSettings().getString(AppDelegate.ACCESS_TOKEN, null))) {
            startActivity(UserActivity.class, true);
        } else {
            startActivity(LoginActivity.class, true);
        }
    }
}
