package com.ayysir.paek.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Constants;

import eu.chainfire.libsuperuser.Shell;

public class RebootActivity extends Activity {

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);

        ListView mList = (ListView) findViewById(R.id.reboot);

        mList.setOnItemClickListener(new LVItemClickListener());


        String[] strings = getResources().getStringArray(R.array.reboot);

        // Prepare the header view for our list
        mList.setAdapter(new ArrayAdapter<String>(this, R.layout.recovery_list_item,
                strings));


    }

    private void selectItem(int position) {
        switch (position) {

            case 0:
                //reboot
                try {
                    Toast.makeText(this, R.string.reboot, Toast.LENGTH_LONG).show();
                    Thread.sleep(1000); // pause a bit before rebooting into recovery
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Shell.SU.run("reboot");
                try {
                    Log.d(Constants.LOG_TAG, "Reboot Recovery selected");
                } catch (Exception except) {
                    Log.e(Constants.LOG_TAG, "Issue with button" + except.getMessage());
                }
                break;

            case 1:
                //soft reboot
                try {
                    Toast.makeText(this, R.string.soft_reboot, Toast.LENGTH_LONG).show();
                    Thread.sleep(1000); // pause a bit before rebooting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Shell.SU.run("pkill zygote");
                try {
                    Log.d(Constants.LOG_TAG, "Reboot selected");
                } catch (Exception except) {
                    Log.e(Constants.LOG_TAG, "Issue with button" + except.getMessage());
                }
                break;

            case 2:
                //reboot to recovery
                try {
                    Toast.makeText(this, R.string.reboot_recovery, Toast.LENGTH_LONG).show();
                    Thread.sleep(1000); // pause a bit before rebooting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Shell.SU.run("reboot recovery");
                try {
                    Log.d(Constants.LOG_TAG, "Reboot selected");
                } catch (Exception except) {
                    Log.e(Constants.LOG_TAG, "Issue with button" + except.getMessage());
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LVItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

            view.isHorizontalFadingEdgeEnabled();
        }
    }
}
