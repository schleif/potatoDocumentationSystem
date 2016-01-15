package com.potatodoc.potatodocumentation.gui;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.data.DatabaseElements.field;
import com.potatodoc.potatodocumentation.data.localDB;
import com.potatodoc.potatodocumentation.data.localDBDataSource;



/**
 * Created by fiel on 30.09.2015.
 */
public class TourFragment extends Fragment {

    public static final String LOG_TAG = TourFragment.class.getSimpleName();
    private localDBDataSource dataSource;
    public String output = "";

    View v;

    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tour_layout, container, false);
        final Button tour = (Button) v.findViewById(R.id.tourStart);
        tour.setOnClickListener(new View.OnClickListener() {
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


        TextView outstandingTasks = (TextView) v.findViewById(R.id.outstandingTasks);
        // Implement a scrollbar into the textview
        outstandingTasks.setMovementMethod(new ScrollingMovementMethod());
        //create an database.
        localDBDataSource database = new localDBDataSource(getContext());
        database.open();
        //database.insertTask("test2", "2015-12-20", "2016-01-12");

        Log.d(LOG_TAG, "Insert wurde ausgeführt");
        Cursor query = database.queryForOutstandandingTasks();
        Log.d(LOG_TAG, "Begin der schleife , Anzahl der Datensätze = " + query.getCount());


         if (query.getCount() != 0) {

                outstandingTasks.setText("");
                 //outstandingTasks.setText("Heute sind noch "+ query.getCount() + " Aufgaben zu erledigen.");


         } else {
             outstandingTasks.setText("Keine Datensätze vorhanden");
         }




        return v;
    }



}
