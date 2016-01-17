package com.potatodoc.potatodocumentation.gui;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


import com.potatodoc.potatodocumentation.R;
import com.potatodoc.potatodocumentation.utils.StopTourWarningDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public static int positionGlobal;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.potatodoc.potatodocumentation.dataprovider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "default_account";
    // Instance fields
    Account mAccount;
    // A content resolver for accessing the provider
    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the dummy account
       mAccount = CreateSyncAccount(com.potatodoc.potatodocumentation.utils.App.getContext());

        // Get the content resolver for your app
        mResolver = getContentResolver();
        // Turn on automatic syncing for the default account and authority
        mResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Make the position accessible for the WarningDialog
     * @param p the currentpostion in the Navigationmenu
     */
    public void getPosition(int p) {
        positionGlobal = p;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        // if a task is edited , Warningdialog will be shown, otherwise it will be handle normally
        if (fragmentManager.findFragmentById(R.id.container) instanceof TaskFragment) {

            getPosition(position); // for the Wanringdialog
            StopTourWarningDialog Warning = new StopTourWarningDialog();
            Warning.show(getSupportFragmentManager(), "Warnung");

        } else {

            //Call onSectionAttached to update the ActionBar Title
            onSectionAttached(position + 1);

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

    }

    public void onSectionAttached(int number) {
        //Set the proper Title
        mTitle = getString(NavigationItem.getAllNavigationItems().get(number - 1).getName());
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Changes the title of the ActionBar of the Activity.
     * May be used to set the title from inside a fragment and/or update it when the back-button is pressed in the future.
     *
     * @param title The title to be set in teh ActionBar
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        //Change Title
        mTitle = title;

        restoreActionBar();
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

}
