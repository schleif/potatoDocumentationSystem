package com.potatodoc.potatodocumentation.gui;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.potatodoc.potatodocumentation.R;



/**
 * Created by fiel on 30.09.2015.
 */
public class TourFragment extends Fragment {



    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tour_layout, container, false);
        final Button tour = (Button) v.findViewById(R.id.tourStart);
        tour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                TaskFragment Task = new TaskFragment();

                android.support.v4.app.FragmentManager fragment = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragment.beginTransaction();
                fragmentTransaction.replace(R.id.container, Task);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }


        });
        return v;
    }
}
