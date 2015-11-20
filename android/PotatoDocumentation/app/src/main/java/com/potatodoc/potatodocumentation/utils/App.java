package com.potatodoc.potatodocumentation.utils;

import android.app.Application;
import android.content.Context;

import com.potatodoc.potatodocumentation.data.localDB;

/**
 * Created by fiel on 20.11.2015.
 * This Class is a help Class to get the Application Context from everywhere
 */
public class App extends Application {

    /**
     * The App Context
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // Init Database
        localDB dbhelper = new localDB(getContext());
    }

    /**
     * Use this Method to get the Context
     * @return Context of the Application
     */
    public static Context getContext(){
        return mContext;
    }
}
