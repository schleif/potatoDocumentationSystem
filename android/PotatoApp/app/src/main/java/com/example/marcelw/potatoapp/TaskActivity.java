package com.example.marcelw.potatoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    public void scannen(View view) {

        //Check if the Button was clicked
        if(view.getId()==R.id.scan) {

            //Start scan with the Camera with some Settings

            IntentIntegrator integrator = new IntentIntegrator(this);
            //integrator.setCaptureActivity(MainActivity.class);
            //integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //Only QR-Codes are allowed
            integrator.setPrompt("QR-Code scannen");
            //integrator.setCameraId(0);  // Use a specific camera of the device
            //integrator.setBeepEnabled(false);
            integrator.initiateScan();

        }
    }

    public void openMap(View view) {

    }

    public void save(View view) {
        //compare new Data with the last results and save it local
    }

    public void nextParcel(View view) {
        // check the Tour on the DB and open the next Parcel

        WarningDialogFragment Warning = new WarningDialogFragment();
        Warning.show(getFragmentManager(),"Warnung");

    }

    public void stopTour(View view) {

        StopTourWarningDialog Warning = new StopTourWarningDialog();
        Warning.show(getFragmentManager(),"Warnung");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


