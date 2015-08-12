package com.example.zaver.mylauncher;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Zaver on 8/11/15.
 */

public class MyLauncherActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return MyLauncherFragment.newInstance();
    }
}
