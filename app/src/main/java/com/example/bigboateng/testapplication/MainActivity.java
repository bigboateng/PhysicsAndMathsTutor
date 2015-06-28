package com.example.bigboateng.testapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //this is the variable for the savedInstance.putInt value
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    //own custom toolbar
    private Toolbar mToolbar;
    //the view of navigation bar which actually contains the items
    private NavigationView mDrawer;
    //this is the layout which contains the navigation view
    private DrawerLayout mDrawerLayout;
    //toggle button in top left of app
    private ActionBarDrawerToggle mDrawerToggle;
    //stores the current selected item in app drawer for later when we save it
    private int mSelectedId;
    //this is boolean to check if first time using app - to show nav drawer
    private boolean mUserSawDrawer = false;
    //shated preference name for mUserSawDrawe boolean
    private String FIRST_TIME = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //linking all java obj to the xml files
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // action bar drawer toggle stuff
        //creating a new action bar toggle object
        mDrawerToggle =
                new ActionBarDrawerToggle(this,
                        mDrawerLayout,
                        mToolbar,
                        R.string.drawer_open,
                        R.string.drawer_close);
        //set on click listener for our drawer (method has been implemented)
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //syncState() to synchronize the indicator with the state of the
        //mDrawerLayout after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
        //check is saved instance state is null, if it is,
        //set mSelectedId to first item in nav view, else if there is a saved state
        //then get int from saved state using key 'SELECTED_ITEM_ID
        mSelectedId = savedInstanceState == null ? R.id.navigation_item_1 : savedInstanceState.getInt(SELECTED_ITEM_ID);
        //hence navigate to whatever mSelected Id is
        navigate(mSelectedId);

        //this checks if user has opened drawer for first time
        //if app is first opened, then open nav drawer
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerAsShown();
        } else {
            hideDrawer();
        }

    }

    //this function deals with navigation in the nav drawer
    //takes in an id, then does stuff from there
    private void navigate(int mSelectedId) {
        switch (mSelectedId) {
            case R.id.navigation_item_8:
                hideDrawer();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
        }

    }

    //android stuff, haven't really used it
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

    //Called by the system when the device configuration changes while the activity is running.
    //here we want to save and return everything back to user for new orientation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //this handles what happens when navigation item is selected
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //when you click on something, it should close the drawer
        menuItem.setChecked(true);
        //saves the item number that the user clicked
        //for when the user rotates the devices so it can be restored
        mSelectedId = menuItem.getItemId();
        //navigate to whatever the user clicked on
        navigate(mSelectedId);

        return true;
    }

    //this saves the item selection in the drawer for when the user rotates the screen
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saves the mSelectedId variable in outState, just like passing
        //bundles through an intent
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        //this is to close the drawer (if opened) when back is pressed
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            //hides the drawer
            hideDrawer();
        } else {
            super.onBackPressed();
        }
    }

    //function to hide the drawer
    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    //function to show the drawer
    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    //check if user has seen drawer before, else opens it
    private boolean didUserSeeDrawer() {
        //gets shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //it sharedPreferences is empty then default value is 'false' so automatically opens drawe
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        //return the boolean value for mUserSawDrawe
        return mUserSawDrawer;
    }

    //this just updates the sharedPreferences that the user has seen the drawer
    //it sets it to true
    private void markDrawerAsShown() {
        //gets shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //edit the shared preference - FIRST_TIME is the key
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

}
