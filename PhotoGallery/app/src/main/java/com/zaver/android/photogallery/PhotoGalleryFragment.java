package com.zaver.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaver on 8/12/15.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhtoRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();

    private int pageNumber = 1;
    private GridLayoutManager mGridLayoutManager;
    private int totalItemCount, lastVisibleItem;
    private boolean loading = false;
    int previousTotal = 0;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhtoRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mPhtoRecyclerView.setLayoutManager(mGridLayoutManager);

        mPhtoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = mGridLayoutManager.getItemCount();
                lastVisibleItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!loading && (lastVisibleItem >= totalItemCount - 1)) {
                    ++pageNumber;
                    loading = true;
                    new FetchItemsTask().execute();
                }
            }
        });
        mPhtoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int layoutWidth = mPhtoRecyclerView.getMeasuredWidth();
                int columns = (int) Math.floor(layoutWidth / 350);
                mGridLayoutManager.setSpanCount(columns);
                mGridLayoutManager.requestLayout();
            }
        });
        return view;
    }


    private void setupAdapter() {
        if(isAdded()) {
            mPhotoAdapter = new PhotoAdapter(mItems);
            mPhtoRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    private void notifyAdapter() {
        if(isAdded()) {
            mPhotoAdapter.notifyItemRangeChanged(previousTotal + 1, mItems.size());
        }
    }

    // *********************************************************************************************
    // ************************************** Private Classes **************************************

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetcher().fetchItems(pageNumber);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            if (pageNumber > 1) {
                previousTotal = mItems.size();
                mItems.addAll(items);
                notifyAdapter();
            } else {
                mItems = items;
                setupAdapter();
            }
            loading = false;
        }
    }
}
