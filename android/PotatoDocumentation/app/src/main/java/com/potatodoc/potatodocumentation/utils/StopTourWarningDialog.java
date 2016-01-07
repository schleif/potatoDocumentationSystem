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

    private CharSequence mTitle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_warning)
                .setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int position = com.potatodoc.potatodocumentation.gui.MainActivity.positionGlobal;

                        //Call onSectionAttached to update the ActionBar Title
                        onSectionAttached(position + 1);

                        // update the main content by replacing fragments
                        FragmentManager fragmentManager = getFragmentManager();

                        //Selecting selected Fragment
                        Fragment fragment = NavigationItem.getAllNavigationItems().get(position).getFragment();

                        //Make the backstack work right
                        String backStackName = fragment.getClass().getName();

                        Boolean isPopped = fragmentManager.popBackStackImmediate(backStackName, 0);

                        if (!isPopped) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container, fragment);
                            fragmentTransaction.addToBackStack(backStackName);
                            fragmentTransaction.commit();


                        }


                        }

                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void onSectionAttached(int number) {
        //Set the proper Title
        mTitle = getString(NavigationItem.getAllNavigationItems().get(number - 1).getName());
    }


}
