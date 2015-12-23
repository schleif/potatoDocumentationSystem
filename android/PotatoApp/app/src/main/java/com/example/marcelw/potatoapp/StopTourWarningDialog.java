package com.example.marcelw.potatoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.marcelw.potatoapp.R;

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
                        /*while (!getActivity().isTaskRoot()) {
                            getActivity().finish();
                        }*/
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}