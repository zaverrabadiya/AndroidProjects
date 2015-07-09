package com.example.zavrab.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by zavrab on 7/8/15.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
