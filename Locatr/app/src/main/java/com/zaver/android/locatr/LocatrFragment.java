package com.zaver.android.locatr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zaver on 10/11/15.
 */
public class LocatrFragment extends Fragment {

    private static final String TAG = "LocatrFragment";
    private ImageView mImageView;
    private GoogleApiClient mClient;
    private ProgressBar mProgressbar;

    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locatr, container, false);

        mImageView = (ImageView) view.findViewById(R.id.image);

        mProgressbar = (ProgressBar) view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_locatr, menu);

        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                findImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findImage() {
        showProgressBar();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(0);

        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "Got a fix: " + location);
                new SearchTask().execute(location);
            }
        });
    }

    private void showProgressBar() {
        mImageView.setVisibility(View.GONE);
        mProgressbar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {
                while(mProgressbar.isShown()) {
                    mProgressbar.setIndeterminate(true);
                }
            }
        }).start();
    }

    private void hideProgressBar() {
        mProgressbar.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
    }

    //********************************** PRIVATE CLASS *********************************************
    private class SearchTask extends AsyncTask<Location, Void, Void> {

        private GalleryItem mGalleryItem;
        private Bitmap mBitmap;

        @Override
        protected Void doInBackground(Location... params) {
            FlickrFetcher fetcher = new FlickrFetcher();
            List<GalleryItem> items = fetcher.searchPhotos(params[0]);
            if(items.size() == 0)
                return null;

            mGalleryItem = items.get(0);

            try {
                byte[] bytes = fetcher.getUrlBytes(mGalleryItem.getUrl());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            catch (IOException ioe) {
                Log.i(TAG, "Unable to download bitmap", ioe);
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            if(mProgressbar.isShown()) {
                hideProgressBar();
            }

            mImageView.setImageBitmap(mBitmap);
        }
    }
}
