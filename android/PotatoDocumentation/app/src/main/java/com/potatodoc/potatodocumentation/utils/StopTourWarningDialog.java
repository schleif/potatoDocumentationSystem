package com.potatodoc.potatodocumentation.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.gui.NavigationItem;


/**
 * Created by Marcel W on 23.12.2015.
 */
public class StopTourWarningDialog extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_warning)
                .setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        }

                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }



}
