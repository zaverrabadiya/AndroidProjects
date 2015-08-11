package com.zaver.android.beatbox;

import android.support.v4.app.Fragment;

/**
 * Created by Zaver on 8/10/15.
 */

public class BeatBoxActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
