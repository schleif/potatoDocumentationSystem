package com.potatodoc.potatodocumentation.gui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.utils.StopTourWarningDialog;
import com.potatodoc.potatodocumentation.utils.WarningDialogFragment;

/**
 * Created by Marcel W on 05.01.2015.
 */

public class TaskFragment extends Fragment {



    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.task_layout, container, false);
        final Button scan = (Button) v.findViewById(R.id.scan);
        final Button save = (Button) v.findViewById(R.id.save);
        final Button nextParcel = (Button) v.findViewById(R.id.nextParcel);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start scan with the Camera with some Settings
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                //integrator.setCaptureActivity(MainActivity.class);
                //integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //Only QR-Codes are allowed
                integrator.setPrompt("QR-Code scannen");
                //integrator.setCameraId(0);  // Use a specific camera of the device
                //integrator.setBeepEnabled(false);
                integrator.initiateScan();

                //TODO: resulthandlng -> new TaskFragment with appropriate parcel


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: compare new Data with the last results and save it local
            }
        });

        nextParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: check the Tour on the DB and open the next Parcel


                WarningDialogFragment Warning = new WarningDialogFragment();
                Warning.show(getFragmentManager(), "Warnung");
            }
        });
        return v;
    }

}