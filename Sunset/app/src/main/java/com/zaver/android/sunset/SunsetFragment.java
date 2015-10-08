package com.zaver.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Zaver on 10/8/15.
 */
public class SunsetFragment extends Fragment{

    // Views
    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    // Sky colors
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean isSunSet;
    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        isSunSet = false;
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSunSet) {
                    startSunsetAnimation();
                    isSunSet = true;
                } else {
                    startSunriseAnimation();
                    isSunSet = false;
                }
            }
        });
        return view;
    }

    private void startSunsetAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        startAnimation(sunYStart, sunYEnd, mBlueSkyColor, mSunsetSkyColor, mNightSkyColor);
    }

    private void startSunriseAnimation() {
        float sunYStart = mSkyView.getBottom();
        float sunYEnd = mSunView.getTop();

        startAnimation(sunYStart, sunYEnd, mNightSkyColor, mSunsetSkyColor, mBlueSkyColor);
    }

    private void startAnimation(float sunYStart, float sunYEnd, int skyColor, int skyToColor, int skyFinalColor) {
        // Moving the Sun
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        // Changine the Sky color
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", skyColor, skyToColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        // Night/Day Sky color
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", skyToColor, skyFinalColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();
    }
}
