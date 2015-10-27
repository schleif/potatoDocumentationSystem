package com.example.marcelw.qr_code_scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class MainActivity extends CaptureActivity implements View.OnClickListener {

    private Button scanBut;
    private TextView scanTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBut = (Button)findViewById(R.id.scan_button);
        scanTxt = (TextView)findViewById(R.id.scan_text);

        scanBut.setOnClickListener(this);

    }

    /**
     *
     * @param v the view on which should "work"
     */
    public void onClick(View v) {

        //Check if the Button was clicked
        if(v.getId()==R.id.scan_button) {

            //Start scan with the Camera with some Settings

            IntentIntegrator integrator = new IntentIntegrator(this);
            //integrator.setCaptureActivity(MainActivity.class);
            //integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //Only QR-Codes are allowed
            integrator.setPrompt("Scan a QR-Code");
            //integrator.setCameraId(0);  // Use a specific camera of the device
            //integrator.setBeepEnabled(false);
            integrator.initiateScan();

        }


    }

    /**
     * Execute the scanresult or show a Errormessage
     * @param requestCode the request
     * @param resultCode the result
     * @param intent the intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //Response form the App
            // Only for test the content of the QR Code
            String scanContent = scanningResult.getContents();
            scanTxt.setText("CONTENT: " + scanContent);

        } else{

           //Popup with the Errormessage
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
