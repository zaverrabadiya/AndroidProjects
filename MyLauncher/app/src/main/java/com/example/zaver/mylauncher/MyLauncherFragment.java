package com.example.zaver.mylauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zaver on 8/11/15.
 */
public class MyLauncherFragment extends Fragment {

    private RecyclerView myActivityRecyclerView;

    public static MyLauncherFragment newInstance() {
        return new MyLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_launcher, container, false);
        myActivityRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_my_launcher_recycler_view);
        myActivityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
