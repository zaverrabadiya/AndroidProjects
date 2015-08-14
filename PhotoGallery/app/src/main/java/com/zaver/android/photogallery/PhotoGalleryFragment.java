package com.zaver.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaver on 8/12/15.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
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
        setHasOptionsMenu(true);
        updateItems();
    }

    @Override
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mPhotoRecyclerView.setLayoutManager(mGridLayoutManager);

        mPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = mGridLayoutManager.getItemCount();
                lastVisibleItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!loading && (lastVisibleItem >= totalItemCount - 1)) {
                    ++pageNumber;
                    loading = true;
                    updateItems();
                }
            }
        });
        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int layoutWidth = mPhotoRecyclerView.getMeasuredWidth();
                int columns = (int) Math.floor(layoutWidth / 350);
                mGridLayoutManager.setSpanCount(columns);
                mGridLayoutManager.requestLayout();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QueryTextSubmit: "+ query);
                pageNumber = 1;
                updateItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QueryTextChange: " + newText);
                return false;
            }
        });
    }

    private void updateItems() {
        new FetchItemsTask().execute();
    }

    private void setupAdapter() {
        if(isAdded()) {
            mPhotoAdapter = new PhotoAdapter(mItems);
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
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
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindGalleryItem(GalleryItem galleryItem) {
                Picasso.with(getActivity())
                        .load(galleryItem.getUrl())
                        .placeholder(R.drawable.butterfly)
                        .into(mItemImageView);

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
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
            String query = "car";

            if(query == null) {
                return new FlickrFetcher().fetchRecentPhotos(pageNumber);
            } else {
                return new FlickrFetcher().searchPhotos(query, pageNumber);
            }
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
