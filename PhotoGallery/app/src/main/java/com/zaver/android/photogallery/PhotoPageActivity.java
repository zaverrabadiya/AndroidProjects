package com.zaver.android.photogallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * Created by Zaver on 9/13/15.
 */
public class PhotoPageActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return  i;
    }

    @Override
    public Fragment createFragment() {
        return PhotoPageFragment.newInstance(getIntent().getData());
    }
}
