package com.potatodoc.potatodocumentation.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Marcel W on 17.01.2016.
 */
public class AuthenticationService extends Service {
    private static final String TAG = "AuthenticatiorService";
    public static final String ACCOUNT_NAME = "Account";
    private Authenticator mAuthenticator;

    /**
     * Obtain a handle to the {@link android.accounts.Account} used for sync in this application.
     *
     * <p>It is important that the accountType specified here matches the value in your sync adapter
     * configuration XML file for android.accounts.AccountAuthenticator (often saved in
     * res/xml/syncadapter.xml). If this is not set correctly, you'll receive an error indicating
     * that "caller uid XXXXX is different than the authenticator's uid".

     */

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }



}
