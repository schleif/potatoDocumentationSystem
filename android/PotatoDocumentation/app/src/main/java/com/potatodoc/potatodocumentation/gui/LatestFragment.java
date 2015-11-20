package com.potatodoc.potatodocumentation.gui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.potatodoc.potatodocumentation.R;

/**
 * Created by fiel on 30.09.2015.
 */
public class LatestFragment extends Fragment {

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.latest_layout, container, false);
        return v;
    }
}
