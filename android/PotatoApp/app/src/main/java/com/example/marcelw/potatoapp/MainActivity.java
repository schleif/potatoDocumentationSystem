package com.example.marcelw.potatoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startTour;
    private Button synchronize;
    private Button showRecords;

    private ScrollView TaskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskView = getTasks();

        startTour = (Button) findViewById(R.id.tour);
        showRecords = (Button) findViewById(R.id.records);
        synchronize = (Button) findViewById(R.id.synchronize);


    }

    public void startTour(View view) {

        Intent Task = new Intent(this, TaskActivity.class);
        startActivity(Task);
    }

    // DB query to get all outstandings tasks as an ScrollView
    public ScrollView getTasks() {


        //getConnection();
        if ( getConnection()) {
            // select Data

        }else {

            TextView Error = (TextView) findViewById(R.id.errorDB);
            Error.setVisibility(View.VISIBLE);
        }

        return TaskView;
    }

    // setup an DB connection
    public boolean getConnection() {

        return false;

    }
    public void synchornize(View view) {
        //synchronize local db with host-db
    }

    public void showRecords(View view) {

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
