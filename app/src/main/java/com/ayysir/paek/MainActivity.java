/*
 * Copyright © 2014 Andre "ayysir" Saddler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ayysir.paek;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ayysir.paek.activities.SettingsActivity;
import com.ayysir.paek.activities.SplashActivity;
import com.ayysir.paek.adapter.NavDrawerListAdapter;
import com.ayysir.paek.dialogs.AboutFragment;
import com.ayysir.paek.dialogs.DownloadDialog;
import com.ayysir.paek.fragments.UpdateFragment;
import com.ayysir.paek.fragments.hosts.CPUHostFragment;
import com.ayysir.paek.fragments.hosts.OverviewFragment;
import com.ayysir.paek.model.NavDrawerItem;
import com.ayysir.paek.tools.GetKernelInfo;
import com.ayysir.paek.tools.Utils;

import java.util.ArrayList;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends FragmentActivity implements
        ActionBar.OnNavigationListener {

    private static final int Overview = 0;
    private static final int CPUINFO = 1;
    private static final int UPDATES = 2;
    private static final int PERIOD = 2000;
    private Context mContext;
    private OverviewFragment mOverviewFragment;
    private UpdateFragment mUpdateFragment;
    private CPUHostFragment mCPUHostFragment;
    /*
     * Pertains to navigation drawer
     */
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private int mPosition;
    // back key to exit
    private long lastPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        mContext = this;

        Intent intent = getIntent();
        onNewIntent(intent);

        //Execute Splash screen
        boolean splash = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("splash", true);
        if (splash) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent splash = new Intent(mContext, SplashActivity.class);
                    splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(splash, 1);
                }
            }, 500);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("splash", false)
                    .commit();
        }

        /* BEGIN
         *
         * START UP FUNCTIONS
         *
         */
        if (Utils.isPAEKinstalled()) {
            //new GetKernelInfo().execute();
        } else if (!Utils.isPAEKinstalled()) {
            new GetKernelInfo().execute();
        } else {
            //
        }

        //mount
        Shell.SU.run("busybox mount -o remount,rw /system");
        Shell.SU.run("busybox mount -o remount,rw /sys");
        /*
         * END
         */

        setContentView(R.layout.activity_main);

        /*
         * navSpinner (navigation dropdown menu that sit next to navigation drawer
         */

        ActionBar actionBar = getActionBar();

        // Hide the action bar title by setting to false
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(true);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);


        // nav drawer icons from resources
        TypedArray navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        assert navMenuIcons != null;
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // setting the nav drawer list adapter
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(Overview);
        }

        Utils.dismissNotification(this);
    }


    // navigation drawer dropdown menu
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // back key to exit
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        //unmount /sys
                        Shell.SU.run("busybox mount -o remount,ro /system");
                        Shell.SU.run("busybox mount -o remount,ro /sys");
                        finish();
                    } else if (getApplicationContext() != null) {
                        Toast.makeText(getApplicationContext(),
                                "Press again to exit.", Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                // settings
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case R.id.action_about:
                new AboutFragment().show(getFragmentManager(), getResources().getString(R.string.about));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        menu.findItem(R.id.action_check).setVisible(mPosition == 0);

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case Overview:
                if (mOverviewFragment == null)
                    mOverviewFragment = new OverviewFragment();
                fragment = mOverviewFragment;
                break;
            case CPUINFO:
                //Show warning dialog once
                /*boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
                if (firstrun) {
                    new CPUDialog().show(getFragmentManager(), "CPU Dialog");
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("firstrun", false)
                            .commit();
                }*/
                if (mCPUHostFragment == null)
                    mCPUHostFragment = new CPUHostFragment();
                fragment = mCPUHostFragment;
                break;
            case UPDATES:
                if (Utils.isPAEKinstalled()) {
                    if (mUpdateFragment == null)
                        mUpdateFragment = new UpdateFragment();
                    fragment = mUpdateFragment;
                } else if (Utils.isG2Suppported(mContext) || Utils.isN4Suppported(mContext)) {
                    Toast.makeText(this, R.string.unsupported_device, Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mDrawerList);
                    DownloadDialog DL = new DownloadDialog();
                    DL.show(getFragmentManager(), "CPU Dialog");
                } else {
                    Toast.makeText(this, R.string.unsupported_device, Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                break;

            default:
                break;
        }

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

            mPosition = position;
            invalidateOptionsMenu();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
}
