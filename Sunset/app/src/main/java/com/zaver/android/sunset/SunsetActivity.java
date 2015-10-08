package com.zaver.android.sunset;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Zaver on 10/08/15.
 */

public class SunsetActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new SunsetFragment();
    }
}
