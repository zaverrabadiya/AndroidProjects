package com.example.zavrab.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.zavrab.criminalintent.Utils.PictureUtils;

/**
 * Created by Zaver on 8/9/15.
 */
public class PhotoViewerFragment extends DialogFragment {

    private ImageView mPhotoView;
    private Bitmap mBitmap;
    private static final String ARG_PATH = "path";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path = getArguments().getString(ARG_PATH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mPhotoView = (ImageView) view.findViewById(R.id.dialog_photo_photoviewer);

        mBitmap = PictureUtils.getScaledBitmap(path, getActivity());
        mPhotoView.setImageBitmap(mBitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    public static PhotoViewerFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);

        PhotoViewerFragment fragment = new PhotoViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
